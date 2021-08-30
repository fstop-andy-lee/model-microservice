package tw.com.firstbank.adapter.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import tw.com.firstbank.entity.SwiftMessageLog;

/**
 * POST /msglogs
 * PUT /msglogs/{id}
 * GET /msglogs/{id}
 * DELETE /msglogs/{id}
 * 
 */
//@RepositoryRestResource
@RepositoryRestResource(collectionResourceRel = "msglogs", path = "msglogs")
public interface SwiftMessageRepository extends JpaRepository<SwiftMessageLog, String> {

  @Query(value = "select * from swift_message where status = 0"
      + " order by id"
      + " LIMIT :records OFFSET 0"
      , nativeQuery = true)
  public List<SwiftMessageLog> findInactive(@Param("records") Integer records);
  
  
}
