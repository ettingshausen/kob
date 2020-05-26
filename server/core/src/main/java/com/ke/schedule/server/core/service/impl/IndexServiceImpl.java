package com.ke.schedule.server.core.service.impl;

import com.ke.schedule.server.core.model.db.ProjectUser;
import com.ke.schedule.server.core.model.db.User;
import com.ke.schedule.server.core.repository.ProjectUserRepository;
import com.ke.schedule.server.core.repository.UserRepository;
import com.ke.schedule.server.core.service.IndexService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设计权限相关的所有业务功能接口实现类
 *
 * @Author: zhaoyuguang
 * @Date: 2018/7/26 上午11:15
 */
@Service("indexService")
public class IndexServiceImpl implements IndexService {

    @Value("${kob-schedule.mysql-prefix}")
    private String mp;
    @Resource
    private ProjectUserRepository projectUserRepository;
    @Resource
    private UserRepository userRepository;

    @Override
    public User selectUserByCodeAndPwd(String code, String pwd) {
        return userRepository.findByCodeAndPwd(code, pwd);
    }

    @Override
    public void initProject(String userCode, String userName, String configuration, String projectCode, String projectName) {
        ProjectUser projectUser = new ProjectUser();
        projectUser.setUserCode(userCode);
        projectUser.setUserName(userName);
        projectUser.setProjectCode(projectCode);
        projectUser.setProjectName(projectName);
        projectUser.setConfiguration(configuration);
        projectUser.setProjectMode("service");
        projectUser.setOwner(true);
        projectUserRepository.save(projectUser);
    }


    @Override
    public boolean existProject(String projectCode) {
        return projectUserRepository.countByProjectCode(projectCode) != 0;
    }

    @Override
    public List<ProjectUser> selectProjectUserByUserCode(String code) {
        return projectUserRepository.findProjectUserByUserCode(code);
    }

    @Override
    public List<ProjectUser> selectProject() {
        return projectUserRepository.findProjectUserByOwner(true);
    }
}
