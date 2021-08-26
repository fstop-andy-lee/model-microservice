package tw.com.firstbank.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import tw.com.firstbank.SwiftMessagingApp;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(classes = SwiftMessagingApp.class)
@ActiveProfiles("develop")
@Sql({"/schema.sql", "/data.sql"})
public class SwiftServiceTest {

}
