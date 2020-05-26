package com.ke.schedule.server.console.controller;

import com.ke.schedule.basic.support.KobUtils;
import com.ke.schedule.server.core.common.Attribute;
import com.ke.schedule.server.core.common.FtlPath;
import com.ke.schedule.server.core.model.db.LogCollect;
import com.ke.schedule.server.core.model.db.LogOpt;
import com.ke.schedule.server.core.model.db.ProjectUser;
import com.ke.schedule.server.core.model.db.TaskRecord;
import com.ke.schedule.server.core.model.oz.ResponseData;
import com.ke.schedule.server.core.repository.LogOptRepository;
import com.ke.schedule.server.core.repository.TaskRecordRepository;
import com.ke.schedule.server.core.service.LoggerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: zhaoyuguang
 * @Date: 2018/8/24 下午9:14
 */
@RequestMapping("/logger")
@Controller
public @Slf4j
class LoggerController {

    @Resource(name = "loggerService")
    private LoggerService loggerService;

    @Resource
    private TaskRecordRepository taskRecordRepository;

    @Resource
    private LogOptRepository logOptRepository;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 任务记录view
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/task_record.htm")
    public String taskWaiting(Model model) {
        Date now = new Date();
        model.addAttribute("trigger_time_start", sdf.format(KobUtils.addHour(now, -24)));
        model.addAttribute("trigger_time_end", sdf.format(now));
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String jobUuid = request.getParameter("job_uuid");
        model.addAttribute("job_uuid", KobUtils.isEmpty(jobUuid) ? "" : jobUuid);
        model.addAttribute(Attribute.INDEX_SCREEN, FtlPath.TASK_RECORD_PATH);
        return FtlPath.INDEX_PATH;
    }

    /**
     * 任务日志List
     *
     * @return
     */
    @RequestMapping(value = "/task_record_list.json")
    @ResponseBody
    public ResponseData taskWaitingList(@RequestParam int pageNum, @RequestParam int pageSize) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String triggerTimeStart = request.getParameter("trigger_time_start");
        String triggerTimeEnd = request.getParameter("trigger_time_end");

        ProjectUser projectUser = (ProjectUser) request.getSession().getAttribute(Attribute.PROJECT_SELECTED);

        String jobUuid = request.getParameter("job_uuid");


        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Specification<TaskRecord> specification = (Specification<TaskRecord>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(projectUser.getProjectCode())) {
                Predicate predicate = criteriaBuilder.equal(root.get("projectCode").as(String.class), projectUser.getProjectCode());
                predicates.add(predicate);
            }
            if (!StringUtils.isEmpty(jobUuid)) {
                Predicate predicate = criteriaBuilder.equal(root.get("jobUuid").as(String.class), jobUuid);
                predicates.add(predicate);
            }
            try {
                if (!StringUtils.isEmpty(triggerTimeStart)) {
                    Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("triggerTime").as(Long.class), sdf.parse(triggerTimeStart).getTime());
                    predicates.add(predicate);
                }
                if (!StringUtils.isEmpty(triggerTimeEnd)) {
                    Predicate predicate = criteriaBuilder.lessThanOrEqualTo(root.get("triggerTime").as(Long.class), sdf.parse(triggerTimeEnd).getTime());
                    predicates.add(predicate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                log.error("sdf_error", e);
// todo                return ResponseData.error("时间输入有误");
            }
            if (predicates.size() == 0) {
                return null;
            }
            return criteriaBuilder.or(predicates.toArray(new Predicate[]{}));
        };
        Page<TaskRecord> page = taskRecordRepository.findAll(specification, pageable);
        return ResponseData.success(page.getTotalElements(), page.getContent());
    }

    /**
     * 任务记录view
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/log_collect.htm")
    public String logCollect(Model model) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String taskUuid = request.getParameter("task_uuid");
        model.addAttribute("task_uuid", KobUtils.isEmpty(taskUuid) ? "" : taskUuid);
        model.addAttribute(Attribute.INDEX_SCREEN, FtlPath.LOG_COLLECT_PATH);
        return FtlPath.INDEX_PATH;
    }

    /**
     * 任务记录List
     *
     * @return
     */
    @RequestMapping(value = "/log_collect_list.json")
    @ResponseBody
    public ResponseData logCollectList(@RequestParam int pageNum, @RequestParam int pageSize) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ProjectUser projectUser = (ProjectUser) request.getSession().getAttribute(Attribute.PROJECT_SELECTED);
        String paramTaskUuid = request.getParameter("task_uuid");
        String taskUuid = null;
        if (!KobUtils.isEmpty(paramTaskUuid)) {
            taskUuid = paramTaskUuid;
        }
        Long count = loggerService.selectLogCollectCountByProjectCodeAndTaskUuid(projectUser.getProjectCode(), taskUuid);
        if (count == 0) {
            return ResponseData.success(0);
        }

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        List<LogCollect> logCollectList = loggerService.selectLogCollectPageByProjectAndTaskUuid(projectUser.getProjectCode(), taskUuid, pageable);
        return ResponseData.success(count, logCollectList);
    }

    /**
     * 操作记录view
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/log_opt.htm")
    public String optLog(Model model) {
        model.addAttribute(Attribute.INDEX_SCREEN, FtlPath.LOG_OPT_PATH);
        return FtlPath.INDEX_PATH;
    }

    /**
     * 操作记录List
     *
     * @return
     */
    @RequestMapping(value = "/log_opt_list.json")
    @ResponseBody
    public ResponseData logOptList(@RequestParam int pageNum, @RequestParam int pageSize) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Integer costTime = KobUtils.isEmpty(request.getParameter("cost_time")) ?
                null : Integer.valueOf(request.getParameter("cost_time"));

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Specification<LogOpt> specification = (Specification<LogOpt>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (costTime != null) {
                Predicate predicate = criteriaBuilder.greaterThan(root.get("costTime").as(Long.class), (long) costTime);
                predicates.add(predicate);
            }
            if (predicates.size() == 0) {
                return null;
            }
            return criteriaBuilder.or(predicates.toArray(new Predicate[]{}));
        };
        Page<LogOpt> page = logOptRepository.findAll(specification, pageable);

        return ResponseData.success(page.getTotalElements(), page.getContent());
    }
}
