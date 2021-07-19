package tw.com.firstbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.com.firstbank.entity.ServiceLog0;
import tw.com.firstbank.entity.ServiceLogKey;

@Repository
public interface ServiceLog0Repository  extends JpaRepository<ServiceLog0, ServiceLogKey> {

}
