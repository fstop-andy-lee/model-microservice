package tw.com.firstbank.adapter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import tw.com.firstbank.domain.type.VerifyStatus;
import tw.com.firstbank.entity.InwardRmt;

/**
 * POST /inwardRmts
 * PUT /inwardRmts/{id}
 * GET /inwardRmts/{id}
 * DELETE /inwardRmts/{id}
 * 
 */
@RepositoryRestResource
public interface InwardRmtRepository extends JpaRepository<InwardRmt, String> {
    
  List<InwardRmt> findByVerifyStatus(VerifyStatus verifyStatus);
  
  @Query(value = "select * from inward_rmt where status = 5 and verify_status = 9"
      + " order by id"
      , nativeQuery = true)
  List<InwardRmt> findVerified();
  
  @Query(value = "select * from inward_rmt where status = 0 and verify_status = 1"
      + " order by id"
      , nativeQuery = true)
  List<InwardRmt> findVerifyPending();

}
