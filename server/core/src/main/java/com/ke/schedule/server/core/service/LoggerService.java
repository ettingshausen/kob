package com.ke.schedule.server.core.service;


import com.ke.schedule.server.core.model.db.LogCollect;
import com.ke.schedule.server.core.model.db.LogOpt;
import com.ke.schedule.server.core.model.db.TaskRecord;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 日志service
 *
 * @Author: zhaoyuguang
 * @Date: 2018/8/24 下午9:15
 */
public interface LoggerService {

    /**
     * 查询任务记录数量
     *
     * @param param 查询参数
     * @return 查询总量
     */
    long selectTaskRecordCountByParam(Map<String, Object> param);


    /**
     * 查询日志收集数量
     *
     * @param projectCode 项目标识
     * @param taskUuid    任务唯一标识
     * @return 查询总量
     */
    Long selectLogCollectCountByProjectCodeAndTaskUuid(String projectCode, String taskUuid);

    /**
     * 查询日志收集
     *
     * @param projectCode 项目标识
     * @param taskUuid    任务唯一标识
     * @param pageable
     * @return 日志收集记录
     */
    List<LogCollect> selectLogCollectPageByProjectAndTaskUuid(String projectCode, String taskUuid, Pageable pageable);

    /**
     * 保存操作日志
     *
     * @param logOpt 操作日志
     * @return 影响行数
     */
    void saveLogOpt(LogOpt logOpt);


}
