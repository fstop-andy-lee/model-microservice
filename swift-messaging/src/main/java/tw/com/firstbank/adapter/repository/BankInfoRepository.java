package tw.com.firstbank.adapter.repository;

import java.util.Optional;

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
public interface BankInfoRepository extends JpaRepository<BankInfo, String> {
  Optional<BankInfo> findByBicAndIsCorr(String bic, Boolean isCorr);
}
