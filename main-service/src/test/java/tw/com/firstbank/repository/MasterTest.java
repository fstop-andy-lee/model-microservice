package tw.com.firstbank.repository;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import tw.com.firstbank.MainServiceApp;
import tw.com.firstbank.entity.Master;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(classes = MainServiceApp.class)
@ActiveProfiles("develop")
//@Sql({"/swift.sql"})
public class MasterTest {

  @Autowired
  MasterRepository masterRepo;
  
  @Test
  public void testList() {
    
    masterRepo.findAll().forEach(x -> System.out.println(x.toString()));
    
  }
  
  @Test
  public void testSave() {
    Master m = masterRepo.findAll().get(0);
    m.setHoldMark(1);
    masterRepo.save(m);
    
    Master e = new Master();
    e.setHoldMark(1);
    Example<Master> example = Example.of(e);
    Optional<Master> opt = masterRepo.findOne(example);
    e = opt.get();
    System.out.println(e.toString());
  }
  
  
}
