package tw.com.firstbank.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import tw.com.firstbank.entity.RmtCbQta;

@RepositoryRestResource
public interface RmtCbQtaRepository extends JpaRepository<RmtCbQta, String> {

}
