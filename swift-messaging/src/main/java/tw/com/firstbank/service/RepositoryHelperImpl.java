package tw.com.firstbank.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.com.firstbank.domain.type.SwiftMessageStatus;
import tw.com.firstbank.entity.SwiftMessageLog;
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
  
  public void markDone(String msgKey) {
    Optional<SwiftMessageLog> opt = swiftMsgRepo.findById(msgKey);
    SwiftMessageLog msgLog = opt.get();
    markDone(msgLog);
  }
  
  public void markPending(SwiftMessageLog msgLog) {
    msgLog.setStatus(SwiftMessageStatus.PENDING);
    swiftMsgRepo.save(msgLog);
  }
  
  public void markDone(SwiftMessageLog msgLog) {
    msgLog.setStatus(SwiftMessageStatus.DONE);
    swiftMsgRepo.save(msgLog);
  }
    
  @Transactional
  public void parseComplete(SwiftMessageLog msg) {
    markDone(msg);
  }
    
  @Transactional
  public void parsePending(SwiftMessageLog msg) {
    markPending(msg);
  }
  
}
