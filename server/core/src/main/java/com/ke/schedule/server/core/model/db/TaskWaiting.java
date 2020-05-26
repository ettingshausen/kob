package com.ke.schedule.server.core.model.db;

import com.alibaba.fastjson.JSONObject;
import com.ke.schedule.basic.model.InnerParams;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据库实体类 对应数据表 kob_task_waiting_$zp 等待执行表
 *
 * @Author: zhaoyuguang
 * @Date: 2018/7/30 上午11:58
 */
@Entity
public @NoArgsConstructor
@Getter
@Setter
class TaskWaiting implements Serializable {
    private static final long serialVersionUID = -859835960095783295L;
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

    private String taskUuid;

    private Boolean ancestor;

    private String relationTaskUuid;

    private String retryType;

    private Boolean rely;

    private Integer retryCount;

    private Boolean failover;

    private String loadBalance;

    private String batchType;

    @Lob
    @Column
    private String userParams;

    @Lob
    @Column
    private String innerParams;

    private String cronExpression;

    private Integer timeoutThreshold;

    private Long triggerTime;
    @Version
    private Integer version;
    @CreationTimestamp
    private Date gmtCreated;
    @UpdateTimestamp
    private Date gmtModified;

    public InnerParams getInnerParamsBean() {
        return JSONObject.parseObject(this.innerParams, InnerParams.class);
    }
}
