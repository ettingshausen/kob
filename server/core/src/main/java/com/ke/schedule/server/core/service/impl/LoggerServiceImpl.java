package com.ke.schedule.server.core.service.impl;

import com.ke.schedule.server.core.model.db.LogCollect;
import com.ke.schedule.server.core.model.db.LogOpt;
import com.ke.schedule.server.core.model.db.TaskRecord;
import com.ke.schedule.server.core.repository.LogCollectRepository;
import com.ke.schedule.server.core.repository.LogOptRepository;
import com.ke.schedule.server.core.repository.TaskRecordRepository;
import com.ke.schedule.server.core.service.LoggerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 日志service
 *
 * @Author: zhaoyuguang
 * @Date: 2018/8/24 下午9:15
 */
@Service("loggerService")
public class LoggerServiceImpl implements LoggerService {

    @Value("${kob-schedule.mysql-prefix}")
    private String mp;
    @Resource
    private LogCollectRepository logCollectRepository;
    @Resource
    private LogOptRepository logOptRepository;

    @Resource
    private TaskRecordRepository taskRecordRepository;


    @Override
    public long selectTaskRecordCountByParam(Map<String, Object> param) {

        Long triggerTimeStart = (Long) param.get("triggerTimeStart");
        Long triggerTimeEnd = (Long) param.get("triggerTimeEnd");

        Specification<TaskRecord> specification = (Specification<TaskRecord>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(param.get("projectCode"))) {
                Predicate predicate = criteriaBuilder.equal(root.get("projectCode").as(String.class), param.get("projectCode"));
                predicates.add(predicate);
            }
            if (!StringUtils.isEmpty(param.get("jobUuid"))) {
                Predicate predicate = criteriaBuilder.equal(root.get("jobUuid").as(String.class), param.get("jobUuid"));
                predicates.add(predicate);
            }
            if (triggerTimeStart != null) {
                Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("triggerTime").as(Long.class), triggerTimeStart);
                predicates.add(predicate);
            }
            if (triggerTimeEnd != null) {
                Predicate predicate = criteriaBuilder.lessThanOrEqualTo(root.get("triggerTime").as(Long.class), triggerTimeEnd);
                predicates.add(predicate);
            }
            if (predicates.size() == 0) {
                return null;
            }
            return criteriaBuilder.or(predicates.toArray(new Predicate[]{}));
        };
        return taskRecordRepository.count(specification);
    }


    @Override
    public Long selectLogCollectCountByProjectCodeAndTaskUuid(String projectCode, String taskUuid) {
        if (StringUtils.isEmpty(taskUuid)) {
            return logCollectRepository.countLogCollectByProjectCode(projectCode);
        } else {

            return logCollectRepository.countLogCollectByProjectCodeAndTaskUuid(projectCode, taskUuid);
        }
    }

    @Override
    public List<LogCollect> selectLogCollectPageByProjectAndTaskUuid(String projectCode, String taskUuid, Pageable pageable) {

        if (StringUtils.isEmpty(taskUuid)) {
            return logCollectRepository.findLogCollectByProjectCode(projectCode, pageable).getContent();
        } else {
            return logCollectRepository.findLogCollectByProjectCodeAndTaskUuid(projectCode, taskUuid, pageable).getContent();
        }
    }

    @Override
    public void saveLogOpt(LogOpt logOpt) {
        logOptRepository.save(logOpt);
    }


}
