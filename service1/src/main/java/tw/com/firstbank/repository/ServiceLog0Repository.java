package tw.com.firstbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import tw.com.firstbank.entity.ServiceLog0;
import tw.com.firstbank.entity.ServiceLogKey;

//@Repository
@RepositoryRestResource(collectionResourceRel = "log0s", path = "log0s")
public interface ServiceLog0Repository  extends JpaRepository<ServiceLog0, ServiceLogKey> {

}
