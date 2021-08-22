package tw.com.firstbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import tw.com.firstbank.entity.RmtCbRpt3;

@RepositoryRestResource
public interface RmtCbRpt3Repository extends JpaRepository<RmtCbRpt3, Integer> {

}
