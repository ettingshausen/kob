package com.ke.schedule.server.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ke.schedule.basic.constant.TaskRecordStateConstant;
import com.ke.schedule.basic.constant.ZkPathConstant;
import com.ke.schedule.basic.model.*;
import com.ke.schedule.basic.support.KobUtils;
import com.ke.schedule.basic.support.UuidUtils;
import com.ke.schedule.server.core.common.CronExpression;
import com.ke.schedule.server.core.common.NodeHashLoadBalance;
import com.ke.schedule.server.core.common.OffsetBasedPageRequest;
import com.ke.schedule.server.core.model.db.JobCron;
import com.ke.schedule.server.core.model.db.ProjectUser;
import com.ke.schedule.server.core.model.db.TaskRecord;
import com.ke.schedule.server.core.model.db.TaskWaiting;
import com.ke.schedule.server.core.model.oz.BatchType;
import com.ke.schedule.server.core.model.oz.RetryType;
import com.ke.schedule.server.core.repository.JobCronRepository;
import com.ke.schedule.server.core.repository.ProjectUserRepository;
import com.ke.schedule.server.core.repository.TaskRecordRepository;
import com.ke.schedule.server.core.repository.TaskWaitingRepository;
import com.ke.schedule.server.core.service.AlarmService;
import com.ke.schedule.server.core.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * @Author: zhaoyuguang
 * @Date: 2018/7/30 下午2:51
 */
@Service(value = "scheduleService")
public @Slf4j
class ScheduleServiceImpl implements ScheduleService {

    @Resource
    private ProjectUserRepository projectUserRepository;
    @Resource
    private CuratorFramework curator;
    @Resource
    private AlarmService alarmService;
    @Value("${kob-schedule.zk-prefix}")
    private String zp;
    @Value("${kob-schedule.mysql-prefix}")
    private String mp;
    @Resource
    private JobCronRepository jobCronRepository;
    @Resource
    private TaskRecordRepository taskRecordRepository;
    @Resource
    private TaskWaitingRepository taskWaitingRepository;

    /**
     * 生成通用等待推送任务
     *
     * @param jobCron              cron类型作业
     * @param serverIdentification server节点标识
     * @return 等待推送任务
     */
    private static TaskWaiting createCommonCronTaskWaiting(JobCron jobCron, String serverIdentification) {
        String taskUuid = UuidUtils.builder(UuidUtils.AbbrType.TW);
        TaskWaiting taskWaiting = new TaskWaiting();
        taskWaiting.setProjectCode(jobCron.getProjectCode());
        taskWaiting.setProjectName(jobCron.getProjectName());
        taskWaiting.setJobUuid(jobCron.getJobUuid());
        taskWaiting.setJobType(JobType.CRON.name());
        taskWaiting.setJobCn(jobCron.getJobCn());
        taskWaiting.setTaskKey(jobCron.getTaskKey());
        taskWaiting.setTaskRemark(jobCron.getTaskRemark());
        taskWaiting.setTaskType(TaskType.NONE.name());
        taskWaiting.setLoadBalance(jobCron.getLoadBalance());
        taskWaiting.setTaskUuid(taskUuid);
        taskWaiting.setAncestor(true);
        taskWaiting.setRelationTaskUuid(taskUuid);
        taskWaiting.setRetryType(jobCron.getRetryType());
        taskWaiting.setRely(jobCron.getRely());
        taskWaiting.setUserParams(jobCron.getUserParams());
        InnerParams innerParams = new InnerParams();
        innerParams.setCronTaskGenerateNode(serverIdentification);
        taskWaiting.setInnerParams(JSONObject.toJSONString(innerParams));
        taskWaiting.setCronExpression(jobCron.getCronExpression());
        taskWaiting.setTimeoutThreshold(jobCron.getTimeoutThreshold());
        taskWaiting.setRetryCount(jobCron.getRetryCount());
        taskWaiting.setFailover(jobCron.getFailover());
        taskWaiting.setBatchType(jobCron.getBatchType());
        return taskWaiting;
    }


    @Override
    public List<JobCron> findRunningCronJob(String mp) {
        return jobCronRepository.findJobCronsBySuspendEquals(false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCronWaitingTaskForTime(String serverIdentification, JobCron jobCron, boolean appendPreviousTask, Integer intervalMin, Date now) {
        String cronExpression = jobCron.getCronExpression();
        CronExpression cron;
        Long lastGenerateTriggerTime = jobCron.getLastGenerateTriggerTime();
        boolean timeAfterSetNow = lastGenerateTriggerTime == null || (!appendPreviousTask && now.getTime() > lastGenerateTriggerTime);
        Date timeAfter = timeAfterSetNow ? now : new Date(lastGenerateTriggerTime);
        try {
            cron = new CronExpression(cronExpression);
        } catch (ParseException e) {
            log.error("cronExpression parse error cronExpression:" + cronExpression, e);
            return;
        }
        Date end = KobUtils.addMin(now, intervalMin);
        List<TaskWaiting> cronTaskWaitingList = new ArrayList<>();
        while (true) {
            Date nextTriggerTime = cron.getTimeAfter(timeAfter);
            if (nextTriggerTime == null || !nextTriggerTime.before(end)) {
                break;
            } else {
                TaskWaiting tw = createCommonCronTaskWaiting(jobCron, serverIdentification);
                tw.setTriggerTime(nextTriggerTime.getTime());
                cronTaskWaitingList.add(tw);
            }
            timeAfter = nextTriggerTime;
        }
        if (!CollectionUtils.isEmpty(cronTaskWaitingList)) {

            jobCron.setLastGenerateTriggerTime(timeAfter.getTime());
            JobCron res = null;
            try {
                res = jobCronRepository.save(jobCron);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (res == null) {
                log.error("job_cron data has change uuid:" + jobCron.getJobUuid());
                throw new RuntimeException("job_cron data has change uuid:" + jobCron.getJobUuid());
            }
            taskWaitingRepository.saveAll(cronTaskWaitingList);
        }
    }

    @Override
    public Boolean lockPushTask(TaskWaiting tw, String cluster, String serverIdentification) {
        return null;
    }

    @Override
    public void fireOverstockTask(List<TaskContext.Path> overstockTask) {
        overstockTask.forEach(t -> {
            try {
                curator.delete().forPath(t.getPath());
                taskRecordRepository.updateStateByTaskUuid(t.getTaskUuid(), TaskRecordStateConstant.STACKED_RECYCLING);
            } catch (Exception e) {
                log.error("er", e);
            }
        });
    }

    @Override
    public int selectCountExpireTaskRecord(long now, String cluster) {
        return (int) taskRecordRepository.countExpireTaskRecord(now);
    }

    @Override
    public List<TaskRecord> selectListExpireTaskRecord(int start, int limit, String cluster) {
        Pageable pageable = new OffsetBasedPageRequest(start, limit);
        return taskRecordRepository.findAll(null, pageable).getContent();
    }

    @Override
    public void handleExpireTask(TaskRecord taskExpire, String cluster) {
        //todo  后期需要判断zk 任务是否在运行
        taskRecordRepository.updateStateByTaskUuid(taskExpire.getTaskUuid(), TaskRecordStateConstant.EXECUTE_EXPIRE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleTaskLog(LogContext context, TaskRecord taskRecord) {
        TaskRecord params = taskRecordRepository.findByTaskUuid(context.getTaskUuid());
        boolean executeWanted = false;
        if (TaskRecordStateConstant.RECEIVE_SUCCESS == context.getState()) {
            executeWanted = true;
            params.setClientIdentification(context.getClientIdentification());
            params.setConsumptionTime(new Date(context.getLogTime()));
            params.setState(context.getState());
        }
        if (TaskRecordStateConstant.RUNNER_START == context.getState()) {
            executeWanted = true;
            alarmService.run(taskRecord);
            params.setExecuteStartTime(new Date(context.getLogTime()));
            params.setState(context.getState());
        }
        if (TaskRecordStateConstant.EXECUTE_SUCCESS == context.getState()
                || TaskRecordStateConstant.EXECUTE_FAIL == context.getState()
                || TaskRecordStateConstant.EXECUTE_EXCEPTION == context.getState()) {
            alarmService.end(taskRecord);
            executeWanted = true;
            params.setComplete(true);
            params.setExecuteEndTime(new Date(context.getLogTime()));
            params.setState(context.getState());
        }
        Boolean needAppendRetry = false;
        String appendRetryTaskUuid = null;
        if (TaskRecordStateConstant.EXECUTE_FAIL == context.getState()) {
            if (taskRecord.getAncestor() && RetryType.FAIL.name().equals(taskRecord.getRetryType())) {
                appendRetryTaskUuid = UuidUtils.builder(UuidUtils.AbbrType.AR);
                InnerParams innerParams = taskRecord.getInnerParamsBean();
                innerParams.setAppendRetryTaskUuid(appendRetryTaskUuid);
                params.setInnerParams(JSONObject.toJSONString(innerParams));
                needAppendRetry = true;
                executeWanted = true;
            }
        }
        if (!KobUtils.isEmpty(context.getMsg())) {
            executeWanted = true;
            params.setMsg(context.getMsg());
        }
        if (executeWanted) {
            taskRecordRepository.save(params);
        }
        if (needAppendRetry) {
            handleRetryFailTask(context, taskRecord, appendRetryTaskUuid);
        }
    }

    /**
     * 处理失败重试任务 这里代码一把唆了 后期需要方法共用
     *
     * @param logContext          日志内容
     * @param taskRecord          任务记录
     * @param appendRetryTaskUuid 追加重试任务的task_uuid
     */
    private void handleRetryFailTask(LogContext logContext, TaskRecord taskRecord, String appendRetryTaskUuid) {
        TaskRecord retryTask = createRetryTaskRecord(logContext, taskRecord, appendRetryTaskUuid);
        taskRecordRepository.save(retryTask);
        TaskContext context = new TaskContext();
        context.getData().setProjectCode(retryTask.getProjectCode());
        context.getData().setJobUuid(retryTask.getJobUuid());
        context.getData().setJobCn(retryTask.getJobCn());
        context.getPath().setTaskUuid(retryTask.getTaskUuid());
        context.getPath().setTaskKey(retryTask.getTaskKey());
        context.getPath().setTriggerTime(retryTask.getTriggerTime());
        context.getPath().setDesignatedNode(retryTask.getInnerParamsBean().getDesignatedNode());
        context.getPath().setRecommendNode(retryTask.getInnerParamsBean().getRecommendNode());
        context.getPath().setTryToExclusionNode(retryTask.getInnerParamsBean().getTryToExclusionNode());
        context.getData().setUserParam(JSONObject.parseObject(retryTask.getUserParams()));
        String projectTaskPath = ZkPathConstant.clientTaskPath(mp, context.getData().getProjectCode());
        int state = TaskRecordStateConstant.PUSH_SUCCESS;
        TaskRecord params = taskRecordRepository.findByTaskUuid(appendRetryTaskUuid);
        try {
            curator.create().withMode(CreateMode.PERSISTENT).forPath(projectTaskPath + ZkPathConstant.BACKSLASH + JSONObject.toJSONString(context));
        } catch (Exception e) {
            log.error("pushTask_error 推送zk事件异常", e);
            state = TaskRecordStateConstant.PUSH_FAIL;
            params.setComplete(true);
        }
        params.setState(state);
        params.setTaskUuid(appendRetryTaskUuid);
        taskRecordRepository.save(params);
    }

    /**
     * 生成 追加重试任务记录
     *
     * @param context             日志内容
     * @param failTaskRecord      失败任务记录
     * @param appendRetryTaskUuid 最佳任务记录的task_uuid
     * @return 追加重试任务记录
     */
    private TaskRecord createRetryTaskRecord(LogContext context, TaskRecord failTaskRecord, String appendRetryTaskUuid) {
        TaskRecord tr = new TaskRecord();
        tr.setProjectCode(failTaskRecord.getProjectCode());
        tr.setProjectName(failTaskRecord.getProjectName());
        tr.setJobUuid(failTaskRecord.getJobUuid());
        tr.setJobType(failTaskRecord.getJobType());
        tr.setJobCn(failTaskRecord.getJobCn());
        tr.setTaskKey(failTaskRecord.getTaskKey());
        tr.setTaskRemark(failTaskRecord.getTaskRemark());
        tr.setTaskType(TaskType.RETRY_FAIL.name());
        tr.setTaskUuid(appendRetryTaskUuid);
        tr.setRelationTaskUuid(failTaskRecord.getTaskUuid());
        tr.setLoadBalance(LoadBalanceType.RANDOM.name());
        tr.setRetryType(RetryType.NONE.name());
        tr.setBatchType(BatchType.NONE.name());
        tr.setRely(false);
        tr.setAncestor(false);
        tr.setUserParams(failTaskRecord.getUserParams());
        InnerParams innerParams = new InnerParams();
        innerParams.setTryToExclusionNode(context.getClientIdentification());
        //todo  我觉得不需要 pushNode
        tr.setTimeoutThreshold(failTaskRecord.getTimeoutThreshold());
        tr.setState(TaskRecordStateConstant.WAITING_PUSH);
        tr.setComplete(false);
        tr.setInnerParams(JSONObject.toJSONString(innerParams));
        tr.setRetryCount(0);
        tr.setTriggerTime(System.currentTimeMillis());
        return tr;
    }

    @Override
    public Long selectCronJobCountByProjectCode(String projectCode) {
        return jobCronRepository.countByProjectCode(projectCode);
    }


    @Override
    public void saveJobRealTime(TaskWaiting taskWaiting) {
        taskWaitingRepository.save(taskWaiting);
    }

    @Override
    public void startJobCron(String jobUuid, Boolean suspend, String projectCode) {
        JobCron toSave = jobCronRepository.findByProjectCodeAndJobUuid(projectCode, jobUuid);
        toSave.setLastGenerateTriggerTime(null);
        toSave.setSuspend(!suspend);
        jobCronRepository.save(toSave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void suspendJobCron(String jobUuid, Boolean suspend, String projectCode) {

        JobCron toSave = jobCronRepository.findByProjectCodeAndJobUuid(projectCode, jobUuid);
        toSave.setLastGenerateTriggerTime(null);
        toSave.setSuspend(!suspend);
        jobCronRepository.save(toSave);

        taskWaitingRepository.deleteByJobUuidAndProjectCode(jobUuid, projectCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delJobCron(String jobUuid, String projectCode) {
        jobCronRepository.deleteJobCronByJobUuidAndProjectCode(jobUuid, projectCode);
        taskWaitingRepository.deleteByJobUuidAndProjectCode(jobUuid, projectCode);
    }

    @Override
    public int triggerTaskWaiting(String taskUuid, String projectCode) {
        return taskWaitingRepository.triggerTaskWaiting(System.currentTimeMillis(), taskUuid, projectCode);
    }

    @Override
    public void delTaskWaiting(String taskUuid, String projectCode) {
        taskWaitingRepository.deleteByTaskUuidAndProjectCode(taskUuid, projectCode);
    }

    @Override
    public void saveJobCron(JobCron jobCron) {
        jobCronRepository.save(jobCron);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editJobCron(JobCron editJobCron) {

        JobCron toSave = jobCronRepository.findByProjectCodeAndJobUuid(editJobCron.getProjectCode(), editJobCron.getJobUuid());
        toSave.setLastGenerateTriggerTime(null);
        jobCronRepository.save(toSave);
        taskWaitingRepository.deleteByJobUuidAndProjectCode(editJobCron.getJobUuid(), editJobCron.getProjectCode());
    }

    @Override
    public Set<String> selectServiceProjectCodeSet() {
        List<ProjectUser> projectList = projectUserRepository.findProjectUserByOwner(true);
        Set<String> serviceProjectCodeSet = new HashSet<>();
        if (!KobUtils.isEmpty(projectList)) {
            for (ProjectUser projectUser : projectList) {
                if ("service".equals(projectUser.getProjectMode())) {
                    serviceProjectCodeSet.add(projectUser.getProjectCode());
                }
            }
        }
        return serviceProjectCodeSet;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean pushTask(TaskWaiting tw, String identification) {
        int deleteCount = taskWaitingRepository.deleteByTaskUuid(tw.getTaskUuid());
        if (deleteCount != 1) {
            log.error("server_admin_code_error_100:删除等待任务数量不为1");
            return false;
        }

        TaskRecord taskRecord = createCommonTaskRecord(tw, identification);
        if (tw.getRely()) {
            TaskRecord lastTask = taskRecordRepository.findTop1ByJobUuidOrderByJobUuidDesc(tw.getJobUuid()).get(0);
            if (lastTask == null && TaskRecordStateConstant.isComplete(lastTask.getState())) {
                taskRecord.setState(TaskRecordStateConstant.RELY_UNDO);
                taskRecord.setComplete(true);
                taskRecordRepository.save(taskRecord);
                return false;
            }
        }

        taskRecordRepository.save(taskRecord);
//        if (insertCount != 1) {
//            log.error("server_admin_code_error_101:插入任务记录数量不为1");
//            throw new RuntimeException("server_code_error_101:插入任务记录数量不为1");
//        }

        TaskContext context = new TaskContext();
        context.getData().setProjectCode(tw.getProjectCode());
        context.getData().setJobUuid(tw.getJobUuid());
        context.getData().setJobCn(tw.getJobCn());
        context.getPath().setTaskUuid(tw.getTaskUuid());
        context.getPath().setTaskKey(tw.getTaskKey());
        context.getPath().setTriggerTime(tw.getTriggerTime());
        context.getPath().setDesignatedNode(tw.getInnerParamsBean().getDesignatedNode());
        context.getPath().setRecommendNode(tw.getInnerParamsBean().getRecommendNode());
        context.getPath().setTryToExclusionNode(tw.getInnerParamsBean().getTryToExclusionNode());
        context.getData().setUserParam(JSONObject.parseObject(tw.getUserParams()));
        String projectTaskPath = ZkPathConstant.clientTaskPath(zp, context.getData().getProjectCode());
        int state = TaskRecordStateConstant.PUSH_SUCCESS;
        TaskRecord params = taskRecordRepository.findByTaskUuid(tw.getTaskUuid());
        try {
            alarmService.send(taskRecord);
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(projectTaskPath + ZkPathConstant.BACKSLASH + context.getZkPath(), JSONObject.toJSONString(context.getData()).getBytes());
        } catch (Exception e) {
            log.error("pushTask_error 推送zk事件异常", e);
            state = TaskRecordStateConstant.PUSH_FAIL;
            params.setComplete(true);
        }
        params.setState(state);
        params.setTaskUuid(tw.getTaskUuid());
        taskRecordRepository.save(params);
//        taskRecordRepository.updateStateByTaskUuid(tw.getTaskUuid(), state);
        return true;
    }

    private TaskRecord createCommonTaskRecord(TaskWaiting tw, String serverIdentification) {
        TaskRecord tr = new TaskRecord();
        tr.setProjectCode(tw.getProjectCode());
        tr.setProjectName(tw.getProjectName());
        tr.setJobUuid(tw.getJobUuid());
        tr.setJobType(tw.getJobType());
        tr.setJobCn(tw.getJobCn());
        tr.setTaskKey(tw.getTaskKey());
        tr.setTaskRemark(tw.getTaskRemark());
        tr.setTaskType(TaskType.NONE.name());
        tr.setTaskUuid(tw.getTaskUuid());
        tr.setRelationTaskUuid(tw.getTaskUuid());
        tr.setLoadBalance(tw.getLoadBalance());
        tr.setRetryType(tw.getRetryType());
        tr.setBatchType(tw.getBatchType());
        tr.setRely(tw.getRely());
        tr.setAncestor(true);
        tr.setUserParams(tw.getUserParams());
        tr.setClientIdentification("");
        InnerParams innerParams = KobUtils.isEmpty(tw.getInnerParams()) ? new InnerParams() : JSONObject.parseObject(tw.getInnerParams(), InnerParams.class);
        innerParams.setTaskPushNode(serverIdentification);
        if (LoadBalanceType.NODE_HASH.name().equals(tw.getLoadBalance())) {
            List<String> clientNodePathList = null;
            try {
                clientNodePathList = curator.getChildren().forPath(ZkPathConstant.clientNodePath(zp, tw.getProjectCode()));
            } catch (Exception e) {
                //todo
                log.error("e", e);
            }
            List<String> nodeList = new ArrayList<>();
            if (!KobUtils.isEmpty(clientNodePathList)) {
                for (String child : clientNodePathList) {
//                  todo what mean  ClientPath clientPath = JSONObject.parseObject(child, ClientPath.class);
                    nodeList.add(child);
                }
            }
            innerParams.setRecommendNode(NodeHashLoadBalance.doSelect(nodeList, tw.getJobUuid()));
        }

        tr.setCronExpression(tw.getCronExpression());
        tr.setTimeoutThreshold(tw.getTimeoutThreshold());
        tr.setState(TaskRecordStateConstant.WAITING_PUSH);
        tr.setInnerParams(JSONObject.toJSONString(innerParams));
        tr.setRetryCount(tw.getRetryCount());
        tr.setTriggerTime(tw.getTriggerTime());
        return tr;
    }

}
