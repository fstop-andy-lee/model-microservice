package tw.com.firstbank.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import tw.com.firstbank.Service1App;
import tw.com.firstbank.entity.ServiceLog0;
import tw.com.firstbank.entity.ServiceLogKey;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(classes = Service1App.class)
@ActiveProfiles("develop")
public class ServiceLogTest {

  @Autowired
  ServiceLog0Repository log0Repo;
  
  @Test
  public void testRepo() {
    String id = UUID.randomUUID().toString();
    Integer seq = 0;
    
    ServiceLog0 log = new ServiceLog0();
    log.setId(id);
    log.setSeq(seq);
    log.setTs(LocalDateTime.now());
    log0Repo.save(log);
    
    ServiceLogKey key = new ServiceLogKey(id, seq);
    
    Optional<ServiceLog0> opt = log0Repo.findById(key);
    System.out.println(opt.get().toString());
  }
  
}
