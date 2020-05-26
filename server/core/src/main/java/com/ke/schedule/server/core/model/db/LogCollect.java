package com.ke.schedule.server.core.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zhaoyuguang
 * @Date: 2018/8/18 下午8:46
 */
@Entity
public @NoArgsConstructor
@Getter
@Setter
class LogCollect implements Serializable {
    private static final long serialVersionUID = 1664242063604500253L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer state;
    private String logUuid;
    private String projectCode;
    private String taskUuid;
    private String ip;
    private String clientIdentification;
    private Date logTime;
    private String msg;
    @CreationTimestamp
    private Date gmtCreated;
    @UpdateTimestamp
    private Date gmtModified;
}
