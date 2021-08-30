package tw.com.firstbank.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import tw.com.firstbank.entity.RmtAdvice;

@RepositoryRestResource
public interface RmtAdviceRepository extends JpaRepository<RmtAdvice, String> {

}
