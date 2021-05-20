package tw.com.firstbank;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(classes = SwiftMessagingApp.class)
@ActiveProfiles("develop")
@Sql({"/swift.sql"})
public class DemoTest {

  @Test
  public void test() {
    
  }
  
}
