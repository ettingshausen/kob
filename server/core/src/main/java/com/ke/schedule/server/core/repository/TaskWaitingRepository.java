package com.ke.schedule.server.core.repository;

import com.ke.schedule.server.core.model.db.TaskWaiting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author ettingshausen
 */
public interface TaskWaitingRepository extends CrudRepository<TaskWaiting, Long> {

    List<TaskWaiting> findTop100TaskWaitingsByTriggerTimeLessThanEqual(long triggerTime);

    int deleteByTaskUuid(String taskUuid);

    Page<TaskWaiting> findTaskWaitingsByProjectCode(String projectCode, Pageable pageable);

    void deleteByJobUuidAndProjectCode(String jobUuid, String projectCode);

    /**
     * 触发等待执行的任务
     *
     * @param triggerTime
     * @param taskUuid
     * @param projectCode
     * @return
     */
    @Modifying
    @Query("update TaskWaiting set triggerTime = :triggerTime where taskUuid = :taskUuid and projectCode = :projectCode")
    int triggerTaskWaiting(@Param("triggerTime") Long triggerTime,
                           @Param("taskUuid") String taskUuid,
                           @Param("projectCode") String projectCode);

    /**
     * 删除等待推送任务
     *
     * @param taskUuid
     * @param projectCode
     * @return
     */
    void deleteByTaskUuidAndProjectCode(String taskUuid, String projectCode);
}
