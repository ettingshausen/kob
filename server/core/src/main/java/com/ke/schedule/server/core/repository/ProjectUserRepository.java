package com.ke.schedule.server.core.repository;

import com.ke.schedule.server.core.model.db.ProjectUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author ettingshausen
 */
public interface ProjectUserRepository extends CrudRepository<ProjectUser, Long>, JpaSpecificationExecutor<ProjectUser> {

    /**
     * 查询 查询项目用户数量
     *
     * @param projectCode
     * @return
     */
    Long countByProjectCode(String projectCode);

    /**
     * 分页
     *
     * @param projectCode
     * @param pageable
     * @return
     */
    Page<ProjectUser> findProjectUserByProjectCode(String projectCode, Pageable pageable);

    /**
     * 获取当前用户所有项目
     *
     * @param userCode
     * @return
     */
    List<ProjectUser> findProjectUserByUserCode(String userCode);

    /**
     * 获取当前项目的所有用户
     *
     * @param projectCode
     * @return
     */
    List<ProjectUser> findProjectUserByProjectCode(String projectCode);


    /**
     * 获取接入项目，通过owner=1查询而出 这里不想考虑极端并发出现一个项目有两个owner
     *
     * @param isOwner
     * @return
     */
    List<ProjectUser> findProjectUserByOwner(Boolean isOwner);
}
