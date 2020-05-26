package com.ke.schedule.server.core.repository;

import com.ke.schedule.server.core.model.db.LogCollect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * @author ettingshausen
 */
public interface LogCollectRepository extends CrudRepository<LogCollect, Long> {

    /**
     * 分页查询日志总量
     *
     * @param projectCode 项目标识
     * @return 总量
     */
    Long countLogCollectByProjectCode(String projectCode);

    /**
     * 分页查询日志总量
     *
     * @param projectCode 项目标识
     * @param taskUuid    项目标识
     * @return 总量
     */
    Long countLogCollectByProjectCodeAndTaskUuid(String projectCode, String taskUuid);

    /**
     * 分页查询日志
     *
     * @param projectCode 项目标识
     * @param pageable    分页入参
     * @return 日志列表
     */
    Page<LogCollect> findLogCollectByProjectCode(String projectCode, Pageable pageable);

    /**
     * 分页查询日志
     *
     * @param projectCode 项目标识
     * @param taskUuid    任务标识
     * @param pageable    分页入参
     * @return 日志列表
     */
    Page<LogCollect> findLogCollectByProjectCodeAndTaskUuid(String projectCode, String taskUuid, Pageable pageable);
}
