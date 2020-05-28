package com.ke.schedule.server.core.repository;

import com.ke.schedule.server.core.model.db.TaskRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * @author ettingshausen
 */
public interface TaskRecordRepository extends CrudRepository<TaskRecord, Long>, JpaSpecificationExecutor<TaskRecord> {


    /**
     * 根据taskUuid查找
     *
     * @param taskUuid
     * @return
     */
    TaskRecord findByTaskUuid(String taskUuid);

    @Query(value = "select count (r) from TaskRecord r  where r.complete = false and r.triggerTime +r.timeoutThreshold*1000 < :now ")
    long countExpireTaskRecord(@Param("now") long now);

    List<TaskRecord> findTop1ByJobUuidOrderByJobUuidDesc(String jobUuid);

    @Modifying
    @Query("update TaskRecord set state = :state , complete = 1 where taskUuid = :taskUuid ")
    void updateStateByTaskUuid(@Param("taskUuid") String taskUuid, @Param("state") int state);
}
