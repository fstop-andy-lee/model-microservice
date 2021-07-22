package tw.com.firstbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import tw.com.firstbank.entity.ServiceLog2;
import tw.com.firstbank.entity.ServiceLogKey;

//@Repository
@RepositoryRestResource(collectionResourceRel = "log2s", path = "log2s")
public interface ServiceLog2Repository  extends JpaRepository<ServiceLog2, ServiceLogKey> {

}
