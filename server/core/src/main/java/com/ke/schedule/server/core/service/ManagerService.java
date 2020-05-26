package com.ke.schedule.server.core.service;

import com.ke.schedule.server.core.model.db.ProjectUser;
import com.ke.schedule.server.core.model.db.User;

import java.util.List;

/**
 * 项目管理service
 *
 * @Author: zhaoyuguang
 * @Date: 2018/9/5 上午10:25
 */

public interface ManagerService {



    /**
     * 根据UserCode获取User
     *
     * @param userCode 用户标识
     * @return 用户
     */
    User selectUserByUserCode(String userCode);

    /**
     * 保存项目用户
     *
     * @param projectUser 项目标识
     * @return 影响行数
     */
    void insertProjectUser(ProjectUser projectUser);

    /**
     * 删除用户
     *
     * @param projectCode 项目标识
     * @param id          删除id
     */
    void deleteProjectUser(String projectCode, String id);
}
