package tw.com.firstbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.com.firstbank.entity.ServiceLog1;
import tw.com.firstbank.entity.ServiceLogKey;

@Repository
public interface ServiceLog1Repository  extends JpaRepository<ServiceLog1, ServiceLogKey> {

}
