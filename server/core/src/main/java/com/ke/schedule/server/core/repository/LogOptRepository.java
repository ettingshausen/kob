package com.ke.schedule.server.core.repository;

import com.ke.schedule.server.core.model.db.LogOpt;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author ettingshausen
 */
public interface LogOptRepository extends CrudRepository<LogOpt, Long>, JpaSpecificationExecutor<LogOpt> {


}
