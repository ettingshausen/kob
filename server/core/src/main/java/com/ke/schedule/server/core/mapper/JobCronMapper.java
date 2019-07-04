package com.ke.schedule.server.core.mapper;

import com.ke.schedule.server.core.model.db.JobCron;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * mybatis mapper 操作数据库表_job_cron cron作业表
 *
 * @Author: zhaoyuguang
 * @Date: 2018/7/30 下午3:22
 */
@Mapper
public interface JobCronMapper {

    String COLUMN = " id, project_code, project_name, job_uuid, job_type, job_cn, task_key, task_remark, task_type, load_balance, " +
            "suspend, batch_type, retry_type, rely, user_params, inner_params, cron_expression, last_generate_trigger_time, " +
            "timeout_threshold, retry_count, failover, version, gmt_created, gmt_modified ";

    String TABLE = " ${prefix}_job_cron ";

    /**
     * 单条保存cron类型作业
     *
     * @param jobCron cron类型作业
     * @param prefix 集群名称
     * @return 影响结果集条数
     */
    @Insert("insert into " + TABLE +
            "(project_code, project_name, job_uuid, job_type, job_cn, task_key, " +
            "task_remark, task_type, load_balance, suspend, batch_type, retry_type, " +
            "rely, user_params, inner_params, cron_expression, timeout_threshold, " +
            "retry_count, failover) " +
            "values " +
            "(#{jc.projectCode}, #{jc.projectName}, #{jc.jobUuid}, #{jc.jobType},  #{jc.jobCn}, #{jc.taskKey}," +
            "#{jc.taskRemark}, #{jc.taskType}, #{jc.loadBalance}, #{jc.suspend}, #{jc.batchType}, #{jc.retryType}," +
            "#{jc.rely}, #{jc.userParams}, #{jc.innerParams}, #{jc.cronExpression}, #{jc.timeoutThreshold}," +
            "#{jc.retryCount}, #{jc.failover}) ")
    int insertOne(@Param("jc") JobCron jobCron, @Param("prefix") String prefix);

    @Update("<script>" +
            "   update " + TABLE +
            "   set last_generate_trigger_time = #{timeAfter}, version = version + 1 " +
            "   where job_uuid = #{jobUuid} and cron_expression = #{cronExpression} and version = #{version} " +
            "   <choose>" +
            "      <when test='lastGenerateTriggerTime != null'> " +
            "          and last_generate_trigger_time = #{lastGenerateTriggerTime} " +
            "       </when> " +
            "       <otherwise> " +
            "           and last_generate_trigger_time is null " +
            "       </otherwise> " +
            "   </choose>" +
            "</script>")
    int updateRunningJobCronLastGenerateTriggerTime(@Param("jobUuid") String jobUuid,
                                                    @Param("cronExpression") String cronExpression,
                                                    @Param("lastGenerateTriggerTime") Long lastGenerateTriggerTime,
                                                    @Param("timeAfter") Long timeAfter,
                                                    @Param("version") Integer version,
                                                    @Param("prefix") String prefix);

    /**
     * 根据状态查询cron类型作业
     *
     * @param suspend 暂停状态 暂停true|运行false
     * @param prefix 集群名称
     * @return
     */
    @Select("select " + COLUMN +
            "from " + TABLE +
            "where suspend = #{suspend} ")
    List<JobCron> findCronJobBySuspend(@Param("suspend") boolean suspend, @Param("prefix") String prefix);

    /**
     * 通过projectCode 查询cron作业数量根据项目标识
     *
     * @param projectCode
     * @param prefix
     * @return
     */
    @Select("select count(1) " +
            "from " + TABLE +
            "where project_code = #{projectCode} ")
    int selectCountByProjectCode(@Param("projectCode") String projectCode, @Param("prefix") String prefix);

    /**
     * 通过projectCode 分页查询cron类型作业信息
     *
     * @param projectCode
     * @param start
     * @param limit
     * @param prefix
     * @return
     */
    @Select("with t as (select " + COLUMN +
            ",ROW_NUMBER() OVER (order by suspend asc, id desc ) AS RowNum " +
            "from  " + TABLE +
            "where project_code = #{projectCode} )" +
            "select * from t where t.RowNum >= #{start} and t.RowNum < #{start} + #{limit} " )
    List<JobCron> selectPageJobCronByProject(@Param("projectCode") String projectCode,
                                             @Param("start") Integer start,
                                             @Param("limit") Integer limit,
                                             @Param("prefix") String prefix);

    /**
     * 修改suspend状态
     *
     * @param exclamationSuspend
     * @param jobUuid
     * @param projectCode
     * @param suspend
     * @param prefix
     * @return
     */
    @Update("update " + TABLE +
            "set suspend = #{exclamationSuspend}, last_generate_trigger_time = null, version = version + 1 " +
            "where job_uuid = #{jobUuid} and project_code = #{projectCode} and suspend = #{suspend} ")
    int updateSuspend(@Param("exclamationSuspend") Boolean exclamationSuspend,
                      @Param("jobUuid") String jobUuid,
                      @Param("projectCode") String projectCode,
                      @Param("suspend") Boolean suspend,
//             todo         @Param("version") Integer version,
                      @Param("prefix") String prefix);

    /**
     * 删除任务
     *
     * @param jobUuid
     * @param projectCode
     * @param prefix
     */
    @Delete("delete from " + TABLE +
            "where job_uuid = #{jobUuid} " +
            "and project_code = #{projectCode} ")
    void deleteByJobUuidAndProjectCode(@Param("jobUuid") String jobUuid,
                                       @Param("projectCode") String projectCode,
                                       @Param("prefix") String prefix);

    /**
     * 更新cron作业
     *
     * @param taskRemark
     * @param cronExpression
     * @param userParams
     * @param jobUuid
     * @param projectCode
     * @param prefix
     * @return
     */
    @Update("update " + TABLE +
            "set task_remark = #{taskRemark}, cron_expression = #{cronExpression}, user_params = #{userParams}, " +
            "last_generate_trigger_time = null, version = version + 1 " +
            "where job_uuid = #{jobUuid} and project_code = #{projectCode} ")
    int updateOne(@Param("taskRemark") String taskRemark,
                  @Param("cronExpression") String cronExpression,
                  @Param("userParams") String userParams,
                  @Param("jobUuid") String jobUuid,
                  @Param("projectCode") String projectCode,
//             todo         @Param("version") Integer version,
                  @Param("prefix") String prefix);
}
