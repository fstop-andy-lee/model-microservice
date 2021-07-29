package tw.com.firstbank.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import tw.com.firstbank.Service2App;
import tw.com.firstbank.entity.Temp;
import tw.com.firstbank.model.MasterDto;
import tw.com.firstbank.repository.TempRepository;


@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(classes = Service2App.class)
@ActiveProfiles("develop")
public class TempServiceTest {

  @Autowired
  TempRepository tempRepo;

  @Autowired
  TempService tempService;
  
  @Test
  public void testSave() {
    String id = "10-110-1234";
    Integer amt = 100;
    Integer status = 1;
    
    MasterDto dto = new MasterDto();
    dto.setId(id);
    dto.setBalance(amt.toString());
    dto.setStatus(status);
    
    tempService.sagaUpdate(dto);
    
    Temp m = tempRepo.findById(id).get();
    assertTrue(m.getStatus() == status);
    assertTrue(m.getHoldMark() == null);
    
    dto.setCompensate(true);
    tempService.sagaUpdate(dto);

    m = tempRepo.findById(id).get();
    assertTrue(m.getStatus() != status);
    assertTrue(m.getHoldMark() == null);
    
  }
}
