package com.ke.schedule.server.core.repository;

import com.ke.schedule.server.core.model.db.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author ettingshausen
 */
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据 code & pwd 查询用户
     *
     * @param code
     * @param pwd
     * @return
     */
    User findByCodeAndPwd(String code, String pwd);

    /**
     * 根据 code 查询用户
     *
     * @param code
     * @return
     */
    User findByCode(String code);
}
