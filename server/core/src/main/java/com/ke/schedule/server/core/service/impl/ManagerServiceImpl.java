package com.ke.schedule.server.core.service.impl;

import com.ke.schedule.server.core.model.db.ProjectUser;
import com.ke.schedule.server.core.model.db.User;
import com.ke.schedule.server.core.repository.ProjectUserRepository;
import com.ke.schedule.server.core.repository.UserRepository;
import com.ke.schedule.server.core.service.ManagerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 项目管理service
 *
 * @Author: zhaoyuguang
 * @Date: 2018/9/5 上午10:26
 */
@Service("managerService")
public class ManagerServiceImpl implements ManagerService {

    @Resource
    private ProjectUserRepository projectUserRepository;
    @Resource
    private UserRepository userRepository;

    @Override
    public User selectUserByUserCode(String userCode) {
        return userRepository.findByCode(userCode);
    }

    @Override
    public void insertProjectUser(ProjectUser projectUser) {
        projectUserRepository.save(projectUser);
    }

    @Override
    public void deleteProjectUser(String projectCode, String id) {
        ProjectUser projectUser = new ProjectUser();
        projectUser.setProjectCode(projectCode);
        projectUser.setId(Integer.valueOf(id));
        projectUserRepository.delete(projectUser);
    }
}
