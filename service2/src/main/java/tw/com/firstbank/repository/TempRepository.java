package tw.com.firstbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import tw.com.firstbank.entity.Temp;

/**
 * POST /temps
 * PUT /temps/{id}
 * GET /temps/{id}
 * DELETE /temps/{id}
 * 
 */
@RepositoryRestResource
public interface TempRepository extends JpaRepository<Temp, String> {

}
