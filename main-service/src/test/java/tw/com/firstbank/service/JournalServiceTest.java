package tw.com.firstbank.service;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import tw.com.firstbank.MainServiceApp;
import tw.com.firstbank.entity.Journal;
import tw.com.firstbank.entity.JournalKey;
import tw.com.firstbank.model.MasterDto;
import tw.com.firstbank.repository.JournalRepository;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(classes = MainServiceApp.class)
@ActiveProfiles("develop")
public class JournalServiceTest {
  @Autowired
  JournalRepository journalRepo;
  
  @Autowired
  JournalService journalService;
  
  @Test
  public void testSave() {
    String id = "10-110-1234";
    Integer amt = 100;
    Integer status = 1;
    
    MasterDto dto = new MasterDto();
    dto.setId(id);
    dto.setBalance(amt.toString());
    dto.setStatus(status);
    
    journalService.sagaUpdate(dto);    
    Optional<Journal> opt = journalRepo.findById(new JournalKey(dto.getUuid(), dto.getSeq()));
    assertTrue(opt.isPresent());
    
    dto.setCompensate(true);
    journalService.sagaUpdate(dto);
    opt = journalRepo.findById(new JournalKey(dto.getUuid(), dto.getSeq()));
    assertTrue(opt.isPresent() == false);
  }
  
}
