package tw.com.firstbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

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

}
