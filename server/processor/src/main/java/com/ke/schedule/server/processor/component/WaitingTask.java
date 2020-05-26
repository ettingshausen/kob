package com.ke.schedule.server.processor.component;

import com.alibaba.fastjson.JSONObject;
import com.ke.schedule.basic.constant.ZkPathConstant;
import com.ke.schedule.basic.model.TaskContext;
import com.ke.schedule.basic.support.NamedThreadFactory;
import com.ke.schedule.server.core.common.AdminConstant;
import com.ke.schedule.server.core.model.db.TaskWaiting;
import com.ke.schedule.server.core.repository.TaskWaitingRepository;
import com.ke.schedule.server.core.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author zhaoyuguang
 */
@Component
public @Slf4j
class WaitingTask {

    private static final ScheduledExecutorService WAITING_TASK_EXECUTOR = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("waiting-task", true));

    @Resource(name = "scheduleService")
    private ScheduleService scheduleService;
    @Resource
    private CuratorFramework curator;
    @Resource
    private ServerContext context;

    @Value("${kob-schedule.zk-prefix}")
    private String zp;
    @Value("${kob-schedule.mysql-prefix}")
    private String mp;
    @Resource
    private TaskWaitingRepository taskWaitingRepository;


    void initialize() {
        WAITING_TASK_EXECUTOR.scheduleAtFixedRate(() -> pushWaitingTask(), 2000, 1000, TimeUnit.MILLISECONDS);
    }

    private void pushWaitingTask() {
        LockConsumer.INSTANCE.lock(pushWaitingTask0(), curator, context.getNode().getIdentification(), ZkPathConstant.serverWaitPath(zp)).accept(null);
    }

    private Consumer<Object> pushWaitingTask0() {
        return o -> {
            long now = System.currentTimeMillis();
            List<TaskWaiting> taskWaitingList = taskWaitingRepository.findTop100TaskWaitingsByTriggerTimeLessThanEqual(now);
            if (!CollectionUtils.isEmpty(taskWaitingList)) {
                taskWaitingList.forEach(e -> {
                    recoveryOverstockTask(e.getProjectCode());
                    boolean finish = scheduleService.pushTask(e, context.getNode().getIdentification());
                });
            }
        };
    }

    private void recoveryOverstockTask(String projectCode) {
        try {
            int random100 = new Random().nextInt(AdminConstant.ONE_HUNDRED);
            if (random100 < 10) {
                List<String> taskPathList = curator.getChildren().forPath(ZkPathConstant.clientTaskPath(zp, projectCode));
                if (!CollectionUtils.isEmpty(taskPathList) && taskPathList.size() > 40) {
                    log.error("send qx");
                    List<TaskContext.Path> paths = new ArrayList<>();
                    for (String s : taskPathList) {
                        TaskContext.Path path = JSONObject.parseObject(s, TaskContext.Path.class);
                        path.setPath(s);
                        paths.add(path);
                    }
                    Collections.sort(paths);
                    List<TaskContext.Path> overstockTask = paths.subList(0, paths.size() - 30);
                    scheduleService.fireOverstockTask(overstockTask);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
