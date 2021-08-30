package tw.com.firstbank.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import tw.com.firstbank.SwiftMessagingApp;
import tw.com.firstbank.adapter.repository.SwiftMessageRepository;
import tw.com.firstbank.domain.type.SwiftMessageStatus;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(classes = SwiftMessagingApp.class)
@ActiveProfiles("develop")
public class SwiftMessageRepositoryTest {

  @Autowired
  private SwiftMessageRepository repo;
  
  @Test
  public void test() {
    System.out.println(repo.findById("1").get().getStatus());
    Assert.assertTrue(repo.findById("1").get().getStatus() == SwiftMessageStatus.INACTIVE);    
  }

}
