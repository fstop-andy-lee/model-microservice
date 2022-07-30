package tw.com.firstbank.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import tw.com.firstbank.MainServiceApp;
import tw.com.firstbank.entity.Master;
import tw.com.firstbank.model.MasterDto;
import tw.com.firstbank.repository.MasterRepository;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(classes = MainServiceApp.class)
@ActiveProfiles("develop")
public class MasterServiceTest {

  @Autowired
  MasterRepository masterRepo;
  
  @Autowired
  MasterService masterService;
  
  //@Test
  public void testSave() {
    String id = "10-110-1234";
    Integer amt = 100;
    Integer status = 1;
    
    MasterDto dto = new MasterDto();
    dto.setId(id);
    dto.setBalance(amt.toString());
    dto.setStatus(status);
    
    masterService.sagaUpdate(dto);
    
    Master m = masterRepo.findById(id).get();
    assertTrue(m.getStatus() == status && m.getBalance().intValue() == amt);
    assertTrue(m.getHoldMark() == null);
  }
  
  //@Test(expected = IllegalStateException.class)
  public void expectedIllegalStateException() {
    // given
    String id = "10-110-1234";
    MasterDto dto = new MasterDto();
    dto.setId(id);
    
    // and locked
    Master m = masterRepo.findById(id).get();
    m.setHoldMark("1");
    masterRepo.save(m);
    
    // when
    masterService.sagaUpdate(dto);
    
    // then
  }
  
}
