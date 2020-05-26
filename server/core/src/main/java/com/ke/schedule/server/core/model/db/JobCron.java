package com.ke.schedule.server.core.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据库实体类 对应数据表 kob_job_cron_$zp 等待执行表
 *
 * @author zhaoyuguang
 * @date 2018/7/30 下午3:28
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class JobCron implements Serializable {
    private static final long serialVersionUID = 5949017760533759060L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String projectCode;

    private String projectName;

    private String jobUuid;

    private String jobType;

    private String jobCn;

    private String taskKey;

    private String taskRemark;

    private String taskType;

    private String loadBalance;

    private Boolean suspend;

    private String batchType;

    private String retryType;

    private Boolean rely;

    private String userParams;

    private String innerParams;

    private String cronExpression;

    private Long lastGenerateTriggerTime;

    private Integer timeoutThreshold;

    private Integer retryCount;

    private Boolean failover;

    @Version
    private Integer version;

    @CreationTimestamp
    private Date gmtCreated;
    @UpdateTimestamp
    private Date gmtModified;

}
