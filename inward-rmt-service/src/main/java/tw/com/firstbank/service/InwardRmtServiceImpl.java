package tw.com.firstbank.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.adapter.channel.InwardRmtChannel;
import tw.com.firstbank.adapter.gateway.AmlGateway;
import tw.com.firstbank.entity.Bafotr;
import tw.com.firstbank.entity.InwardRmt;
import tw.com.firstbank.entity.Master;
import tw.com.firstbank.entity.RmtAdvice;
import tw.com.firstbank.model.InwardRmtDto;


@Slf4j
@Service
public class InwardRmtServiceImpl implements InwardRmtService, InwardRmtChannel {

  @Autowired
  private RepositoryHelper repoHelper;
  
  @Autowired
  private AmlGateway amlGateway;
  
  public Integer processInwardRmt(InwardRmtDto dto) {
    Integer ret = 0;
    
    ret = processInwardRmtThenAmlThenAdvice(from(dto));
    //ret = processAmlThenInwardRmtThenAdvice(from(dto));
    
    return ret;
  }
  
  private InwardRmt from(InwardRmtDto src) {
    InwardRmt target = new InwardRmt();
    BeanUtils.copyProperties(src, target);
    
    // status, verify status
    
    return target;
  }
  
  private Integer processInwardRmtThenAmlThenAdvice(InwardRmt rmt) {
    Integer ret = 0;
    try {
      
      // 取姓名
      Master master = repoHelper.findMasterByAcct(rmt.getBenefAcct());
      if (master != null) {
        rmt.setBenefName(master.getName());
      }
            
      repoHelper.parseComplete(rmt, addBafotr(rmt));
      
      // check aml
      if (amlGateway.screenByApi(rmt.getBenefName()) > 0) {
        log.debug("AML HIT");
        repoHelper.markVerifyPending(rmt);
      } else {
        log.debug("AML OK");                
        repoHelper.markVerifyDone(rmt);
        // print rmt advice & notice
        printRmtAdvice(rmt);
        // wating for payment
        repoHelper.markPayment(rmt);
        
        repoHelper.payment(rmt);
        
        return 1;
      }
      
      // 異常
      
    } catch(Exception e) {
      log.error(e.getMessage(), e);
    }    
    
    return ret;
  }
  
  @SuppressWarnings("unused")
  private Integer processAmlThenInwardRmtThenAdvice(InwardRmt rmt) {
    Integer ret = 0;
    try {      
      
      
      // 取姓名
      Master master = repoHelper.findMasterByAcct(rmt.getBenefAcct());
      if (master != null) {
        rmt.setBenefName(master.getName());
      }
      
      // check aml
      if (amlGateway.screenByApi(rmt.getBenefName()) > 0) {
        log.debug("AML HIT");
        repoHelper.markVerifyPending(rmt);
      } else {
        repoHelper.markVerifyDone(rmt);
      }
            
      repoHelper.parseComplete(rmt, addBafotr(rmt));
      
      if (rmt.isVerifyDone()) {
        // print rmt advice & notice
        printRmtAdvice(rmt);
        // wating for payment
        repoHelper.markPayment(rmt);
         
        repoHelper.payment(rmt);
        
        return 1;
      }
      
      // 異常
      
    } catch(Exception e) {
      log.error(e.getMessage(), e);
    }    
    
    return ret;
  }
  
  private void printRmtAdvice(InwardRmt rmt) {
    RmtAdvice advice = new RmtAdvice();
    BeanUtils.copyProperties(rmt, advice);
    
    advice.setStatus(0);
    repoHelper.saveRmtAdvice(advice);
  }
  
  private Bafotr addBafotr(InwardRmt rmt) {
    Bafotr ret = null;
    ret = new Bafotr();
    
    ret.setBic(rmt.getReceiverCorr());
    ret.setCcy(rmt.getCcy());
    ret.setCrAmt(rmt.getInstAmt());
    
    return ret;
  }
  
}
