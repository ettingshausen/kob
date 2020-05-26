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
 * @Author: zhaoyuguang
 * @Date: 2018/9/13 下午1:34
 */
@Entity
public @NoArgsConstructor
@Getter
@Setter
class LogOpt implements Serializable {
    private static final long serialVersionUID = -7080428279319600936L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userCode;
    private String userName;
    private String optUrl;
    @Lob
    @Column
    private String request;
    private String response;
    private Long costTime;
    @CreationTimestamp
    private Date gmtCreated;
    @UpdateTimestamp
    private Date gmtModified;
}
