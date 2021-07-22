package tw.com.firstbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import tw.com.firstbank.entity.ServiceLog1;
import tw.com.firstbank.entity.ServiceLogKey;

//@Repository
@RepositoryRestResource(collectionResourceRel = "log1s", path = "log1s")
public interface ServiceLog1Repository  extends JpaRepository<ServiceLog1, ServiceLogKey> {

}
