package tw.com.firstbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import tw.com.firstbank.entity.BankInfo;

/**
 * POST /bankinfos
 * PUT /bankinfos/{id}
 * GET /bankinfos/{id}
 * DELETE /bankinfos/{id}
 * 
 */
@RepositoryRestResource
//@RepositoryRestResource(collectionResourceRel = "masters", path = "masters")
public interface BankInfoRepository extends JpaRepository<BankInfo, String> {

}
