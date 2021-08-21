package tw.com.firstbank.service;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.com.firstbank.domain.type.SwiftMessageStatus;
import tw.com.firstbank.entity.Bafotr;
import tw.com.firstbank.entity.InwardRmt;
import tw.com.firstbank.entity.SwiftMessageLog;
import tw.com.firstbank.repository.BafotrRepository;
import tw.com.firstbank.repository.InwardRmtRepository;
import tw.com.firstbank.repository.SwiftMessageRepository;

/**
 * Any self-invocation calls will not start any transaction
 * Only public methods should be annotated with @Transactional
 * 
 */
@Service
public class RepositoryHelperImpl implements RepositoryHelper {
  @Autowired
  private SwiftMessageRepository swiftMsgRepo;
  
  @Autowired
  private InwardRmtRepository inwardRmtRepo;
  
  @Autowired
  private BafotrRepository bafotrRepo;
  
  private String generateId() {
    return UUID.randomUUID().toString();
  }
  
  public List<SwiftMessageLog> findInactiveMsgs(Integer records) {
    return swiftMsgRepo.findInactive(records);
  }
  
  public String saveInactiveSwiftMessageLog(String content) {
    String id = generateId();
    SwiftMessageLog msgLog = new SwiftMessageLog();
    msgLog.setId(id);
    msgLog.setMsg(content);
    msgLog.setStatus(SwiftMessageStatus.INACTIVE);
    swiftMsgRepo.save(msgLog);
    return id;
  }
  
  public void markPending(SwiftMessageLog msgLog) {
    msgLog.setStatus(SwiftMessageStatus.PENDING);
    swiftMsgRepo.save(msgLog);
  }
  
  public void markDone(SwiftMessageLog msgLog) {
    msgLog.setStatus(SwiftMessageStatus.DONE);
    swiftMsgRepo.save(msgLog);
  }
  
  public void saveBafotr(Bafotr otr) {
    bafotrRepo.save(otr);
  }
  
  public void addBafotr(Bafotr otr) {
    Bafotr baf = null;
    
    if (otr.getId() == null) {
      otr.setId(otr.getBic() + ":" + otr.getCcy());
    } 
    
    if (bafotrRepo.findById(otr.getId()).isPresent()) {
      baf = bafotrRepo.findById(otr.getId()).get();
      baf.setCrAmt(baf.getCrAmt().add(otr.getCrAmt()));
    } else {
      baf = otr;
    }
    
    saveBafotr(baf);
  }
  
  @Transactional
  public void parseComplete(SwiftMessageLog msg, InwardRmt rmt, Bafotr otr) {
    saveInwardRmt(rmt);
    markDone(msg);
    addBafotr(otr);
  }
  
  @Transactional
  public void parsePending(SwiftMessageLog msg) {
    markPending(msg);
  }
  
  public void saveInwardRmt(InwardRmt rmt) {
    inwardRmtRepo.save(rmt);
  }
}
