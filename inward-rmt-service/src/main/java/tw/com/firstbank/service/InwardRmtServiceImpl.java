package tw.com.firstbank.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.opentracing.Span;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
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
  
  public InwardRmtDto processInwardRmt(InwardRmtDto dto) {
    Integer ret = 0;
    
    // 可置換成 orchestrator engine
    ret = processInwardRmtThenAmlThenAdvice(from(dto));
    
    // 現場 live demo 場景一
    //ret = processAmlThenInwardRmtThenAdvice(from(dto));
    
    dto.setReplyStatus(ret);
    
    return dto;
  }
  
  // 放行
  public Integer processVerifiedInwardRmt() {
    Integer ret = 0;
    List<InwardRmt> rmts =  repoHelper.findVerifiedInwardRmt();
    
    for(InwardRmt rmt : rmts) {
      // print rmt advice & notice
      printRmtAdvice(rmt);
      // waiting for payment
      repoHelper.markPayment(rmt);
      
      repoHelper.payment(rmt);
      
      repoHelper.billRpt(rmt);
      ret++;
    }
    
    return ret;    
  }
  
  // 審核
  @Override
  public Integer processVerifyPendingInwardRmt() {
    Integer ret = 0;
    List<InwardRmt> rmts =  repoHelper.findVerifyPendingInwardRmt();
    for(InwardRmt rmt : rmts) {
      repoHelper.markVerifyDone(rmt);
      ret++;
    }
    return ret;
  }
  
  private InwardRmt from(InwardRmtDto src) {
    InwardRmt target = new InwardRmt();
    BeanUtils.copyProperties(src, target);
    
    // status, verify status
    
    return target;
  }
  
  private Integer processInwardRmtThenAmlThenAdvice(InwardRmt rmt) {
    log.debug("processInwardRmtThenAmlThenAdvice {}", rmt.getId());
    Span sp = startTrace("processInwardRmtThenAmlThenAdvice " + rmt.getId());
    
    Integer ret = 0;
    try {
      
      // 取姓名
      Master master = repoHelper.findMasterByAcct(rmt.getBenefAcct());
      if (master != null) {
        rmt.setBenefName(master.getName());
      }
            
      repoHelper.parseComplete(rmt, addBafotr(rmt));      
      ret = 1;
      
      // check aml
      if (amlGateway.screenByApi(rmt.getBenefName()) > 0) {
        log.debug("AML HIT");
        traceDebug(sp, "AML HIT");
        repoHelper.markVerifyPending(rmt);
        
        return ret;
      } else {
        log.debug("AML OK");          
        traceDebug(sp, "AML OK");
        syncPayment(rmt);
        //asyncPayment(rmt);
        
        return ret;
      }
      
    } catch(Exception e) {
      log.error(e.getMessage(), e);
      traceError(sp, e.getMessage());
    } finally {
        endTrace(sp);
    }
    
    return ret;
  }
  
  @SuppressWarnings("unused")
  private Integer processAmlThenInwardRmtThenAdvice(InwardRmt rmt) {
    log.debug("processAmlThenInwardRmtThenAdvice {}", rmt.getId());
    Span sp = startTrace("processAmlThenInwardRmtThenAdvice " + rmt.getId() );
    Integer ret = 0;
    try {      
      
      
      // 取姓名
      Master master = repoHelper.findMasterByAcct(rmt.getBenefAcct());
      if (master != null) {
        rmt.setBenefName(master.getName());
      }
      
      // check aml
      Integer amlStatus = amlGateway.screenByApi(rmt.getBenefName());
      if (amlStatus > 0) {
        log.debug("AML HIT");
        traceDebug(sp, "AML HIT");
        repoHelper.markVerifyPending(rmt);
      } else {
        repoHelper.markVerifyDone(rmt);
      }
            
      repoHelper.parseComplete(rmt, addBafotr(rmt));
      ret = 1;

      if (amlStatus > 0) {
        return 1;
      }      
      
      if (rmt.isVerifyDone()) {
        // print rmt advice & notice
        printRmtAdvice(rmt);
        // waiting for payment
        repoHelper.markPayment(rmt);
         
        repoHelper.payment(rmt);
        
        repoHelper.billRpt(rmt);
        return ret;
      }
      
      // 異常
      
    } catch(Exception e) {
      log.error(e.getMessage(), e);
      traceError(sp, e.getMessage());
    } finally {
      endTrace(sp);
    }
    
    return ret;
  }
 
  @SuppressWarnings("unused")
  private void syncPayment(InwardRmt rmt) {
    repoHelper.markVerifyDone(rmt);
    // print rmt advice & notice
    printRmtAdvice(rmt);
    // waiting for payment
    repoHelper.markPayment(rmt);
    
    repoHelper.payment(rmt);
    
    repoHelper.billRpt(rmt);
  }
  
  @Async("threadPoolTaskExecutor")
  private void asyncPayment(InwardRmt rmt) {    
    repoHelper.markVerifyDone(rmt);
    // print rmt advice & notice
    printRmtAdvice(rmt);
    // wating for payment
    repoHelper.markPayment(rmt);
    
    repoHelper.payment(rmt);
    
    repoHelper.billRpt(rmt);
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

  private Span startTrace(String spanName) {
    Span sp = null;
    if (GlobalTracer.get() == null) {
      return sp;
    }
    if (GlobalTracer.get().activeSpan() != null) {
      sp = GlobalTracer.get().buildSpan(spanName).asChildOf(GlobalTracer.get().activeSpan()).start();
    } else {
      sp = GlobalTracer.get().buildSpan(spanName).start();
    }
    
    return sp;
  }

  private void traceError(Span sp, String msg) {
    if (sp == null) return;
    Tags.ERROR.set(sp, true);
    sp.log(msg);
  }
  
  private void traceDebug(Span sp, String msg) {
    if (sp == null) return;
    sp.log(msg);
  }
  
  private void endTrace(Span sp) {
    if (sp == null) return;
    sp.finish();
  }

  
}
