package com.ke.schedule.server.core.repository;

import com.ke.schedule.server.core.model.db.JobCron;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author ettingshausen
 */
public interface JobCronRepository extends CrudRepository<JobCron, Long> {
    /**
     * 根据状态查询cron类型作业
     *
     * @param suspend 暂停状态 暂停true|运行false
     * @return
     */
    List<JobCron> findJobCronsBySuspendEquals(Boolean suspend);

    /**
     * 通过projectCode 查询cron作业数量根据项目标识
     *
     * @param projectCode
     * @return
     */
    Long countByProjectCode(String projectCode);

    /**
     * 通过projectCode 分页查询cron类型作业信息
     *
     * @param projectCode
     * @param pageable
     * @return
     */
    Page<JobCron> findJobCronsByProjectCode(String projectCode, Pageable pageable);


    /**
     * 删除任务
     *
     * @param jobUuid
     * @param projectCode
     */
    void deleteJobCronByJobUuidAndProjectCode(String jobUuid, String projectCode);


    /**
     * 根据 projectCode jobUuid 查询
     *
     * @param projectCode
     * @param jobUuid
     * @return
     */
    JobCron findByProjectCodeAndJobUuid(String projectCode, String jobUuid);


}
