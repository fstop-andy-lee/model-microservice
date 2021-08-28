package tw.com.firstbank.service;

import java.time.Instant;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.domain.type.InwardRmtStatus;
import tw.com.firstbank.domain.type.VerifyStatus;
import tw.com.firstbank.entity.AcMr;
import tw.com.firstbank.entity.Bafotr;
import tw.com.firstbank.entity.BillRpt;
import tw.com.firstbank.entity.InwardRmt;
import tw.com.firstbank.entity.Master;
import tw.com.firstbank.entity.Position;
import tw.com.firstbank.entity.RmtAdvice;
import tw.com.firstbank.entity.RmtCbQta;
import tw.com.firstbank.entity.RmtCbRpt3;
import tw.com.firstbank.repository.AcMrRepository;
import tw.com.firstbank.repository.BafotrRepository;
import tw.com.firstbank.repository.BillRptRepository;
import tw.com.firstbank.repository.InwardRmtRepository;
import tw.com.firstbank.repository.MasterRepository;
import tw.com.firstbank.repository.PositionRepository;
import tw.com.firstbank.repository.RmtAdviceRepository;
import tw.com.firstbank.repository.RmtCbQtaRepository;
import tw.com.firstbank.repository.RmtCbRpt3Repository;

/**
 * Any self-invocation calls will not start any transaction
 * Only public methods should be annotated with @Transactional
 * 
 */
@Slf4j
@Service
public class RepositoryHelperImpl implements RepositoryHelper {
  
  @Autowired
  private InwardRmtRepository inwardRmtRepo;
  
  @Autowired
  private BafotrRepository bafotrRepo;
  
  @Autowired
  private RmtAdviceRepository rmtAdviceRepo;
  
  @Autowired
  private MasterRepository masterRepo;
  
  @Autowired
  private AcMrRepository acmrRepo;
  
  @Autowired
  private RmtCbRpt3Repository rmtCbRpt3Repo;
  
  @Autowired
  private RmtCbQtaRepository rmtCbQtaRepo;
  
  @Autowired
  private PositionRepository positionRepo;  
  
  @Autowired
  private BillRptRepository billRptRepo;  
  
    
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
  
  public Master findMasterByAcct(String acct) {
    return masterRepo.findById(acct).orElse(null);
  }
  
  public void markVerifyPending(InwardRmt rmt) {
    rmt.setVerifyStatus(VerifyStatus.PENDING);
    saveInwardRmt(rmt);
  }

  public void markVerifyDone(InwardRmt rmt) {
    rmt.setVerifyStatus(VerifyStatus.DONE);
    rmt.setStatus(InwardRmtStatus.PAY);
    saveInwardRmt(rmt);
  }
  
  public void markPayment(InwardRmt rmt) {
    rmt.setStatus(InwardRmtStatus.PAY);
    saveInwardRmt(rmt);
  }
  
  public void markDone(InwardRmt rmt) {
    rmt.setStatus(InwardRmtStatus.DONE);
    saveInwardRmt(rmt);
  }
  
  public void saveRmtAdvice(RmtAdvice advice) {
    rmtAdviceRepo.save(advice);
  }
  
  public void saveInwardRmt(InwardRmt rmt) {
    inwardRmtRepo.save(rmt);
  }
  
  @Transactional
  public void parseComplete(InwardRmt rmt, Bafotr otr) {
    
    // 1 匯入資料檔
    saveInwardRmt(rmt);
    
    // 2 存同
    addBafotr(otr);
  }
  
  @Transactional
  public void payment(InwardRmt rmt) {
    
    // 1 入扣帳 I/O
    creditAccount(rmt);
    
    // 2 會計 I/O
    creditAcMr(rmt);
    
    // 3 央行媒體申報 I/O
    createRmtCbRpt3(rmt);
    
    // 4 央行額度通報 I/O
    createRmtCbQta(rmt);
    
    // 5 累計資金部位 I/O
    creditPosition(rmt);
    
    // 6 匯入資料檔 I/O
    markDone(rmt);
    
    if ("NZD".equalsIgnoreCase(rmt.getCcy())) {
      throw new IllegalStateException("Trigger tx rollback");      
    }
    
    //for(int i=0; i<5000000; i++) {
    //  Math.sqrt(12345678+i);
    //}
  }
  
  @Transactional
  public void billRpt(InwardRmt rmt) {    
    Master master = findMasterByAcct(rmt.getBenefAcct());
    if (master == null) {
      log.error("Invalid acct {}", rmt.getBenefAcct());
      throw new IllegalStateException();
    }
    
    // 印水單、申報書        
    try {
      
      BillRpt rpt = new BillRpt();
      
      rpt.setId(rmt.getId());
      rpt.setBenefAcct(rmt.getBenefAcct());
      rpt.setUnino(master.getUnino());
      rpt.setAddr(master.getAddr());
      rpt.setPhone(master.getPhone());
      rpt.setBenefName(master.getName());
      rpt.setBenefCust(rmt.getBenefCust());
      rpt.setCcy(rmt.getCcy());
      rpt.setInstAmt(rmt.getInstAmt());      
      rpt.setFee(rmt.getReceiverCharge());      
      rpt.setOrderCust(rmt.getOrderCust());
      
      log.debug("BILL {}", rpt.toString());
      billRptRepo.save(rpt);
      
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    
  }
  
  public void creditAccount(InwardRmt rmt) {
    Master master = findMasterByAcct(rmt.getBenefAcct());
    if (master == null) {
      log.error("Invalid acct {}", rmt.getBenefAcct());
      throw new IllegalStateException();
    }
    
    master.setBalance(master.getBalance().add(rmt.getInstAmt()));
    masterRepo.save(master);
  }
  
  public void creditAcMr(InwardRmt rmt) {
    String acno = null;
    AcMr acmr = null;
    
    // （借）存放銀行同業－國外代理行（外幣） / （貸）應解匯款（外幣）傳票    
    acno = "存放銀行同業－國外代理行（外幣）";
    acmr = findAcMr(acno);
    acmr.setDbAmt(acmr.getDbAmt().add(rmt.getInstAmt()));
    acmr.setDbCnt(acmr.getDbCnt() + 1);
    acmrRepo.save(acmr);
    
    acno = "應解匯款（外幣）";
    acmr = findAcMr(acno);
    acmr.setCrAmt(acmr.getCrAmt().add(rmt.getInstAmt()));
    acmr.setCrCnt(acmr.getCrCnt() + 1);
    acmrRepo.save(acmr);
    
    // （借）應解匯款（外幣） / （貸）外匯活期存款或外匯定期存款（外幣）
    acno = "應解匯款（外幣）";
    acmr = findAcMr(acno);
    acmr.setDbAmt(acmr.getDbAmt().add(rmt.getInstAmt()));
    acmr.setDbCnt(acmr.getDbCnt() + 1);
    acmrRepo.save(acmr);
    
    acno = "外匯活期存款或外匯定期存款（外幣）";
    acmr = findAcMr(acno);
    acmr.setCrAmt(acmr.getCrAmt().add(rmt.getInstAmt()));
    acmr.setCrCnt(acmr.getCrCnt() + 1);
    acmrRepo.save(acmr);
    
  }
  
  public void createRmtCbRpt3(InwardRmt rmt) {
    RmtCbRpt3 rpt = new RmtCbRpt3();
    
    rpt.setCcy(rmt.getCcy());
    rpt.setDataAmtSign("+");
    rpt.setDataAmt(rmt.getInstAmt());    
    rpt.setValueDate(rmt.getValueDate().toString());
    
    rpt.setSwiftBank(rmt.getSenderCorr());
    
    Master master = findMasterByAcct(rmt.getBenefAcct());
    rpt.setIdno(master.getUnino());
    
    rpt.setDataSource("M");
    
    rmtCbRpt3Repo.save(rpt);
  }
  
  public void createRmtCbQta(InwardRmt rmt) {
    RmtCbQta qta = new RmtCbQta();
    
    qta.setId(rmt.getId());
    qta.setAmt(rmt.getInstAmt());
    
    Master master = findMasterByAcct(rmt.getBenefAcct());
    qta.setUnino(master.getUnino());
    
    qta.setCreateTime(Instant.now());
    
    rmtCbQtaRepo.save(qta);
  }
  
  public void creditPosition(InwardRmt rmt) {
    Position pos = positionRepo.findById(rmt.getCcy()).orElse(null);
    
    if (pos == null) {
      pos = new Position();
      pos.setId(rmt.getCcy());
    } 
    
    pos.setCrAmt(pos.getCrAmt().add(rmt.getInstAmt()));
    
    pos.setNetAmt(pos.getCrAmt().subtract(pos.getDbAmt()));
    
    positionRepo.save(pos);
  }
  
  public List<InwardRmt> findVerifyPendingInwardRmt() {
    return inwardRmtRepo.findVerifyPending();
  }
  
  public List<InwardRmt> findVerifiedInwardRmt() {
    return inwardRmtRepo.findVerified();
  }
  
  private AcMr findAcMr(String acno) {
    AcMr ret = acmrRepo.findById(acno).orElse(null);;
    if (ret == null) {
      ret = new AcMr();
      ret.setId(acno);
    }    
    return ret;
  }
  
  
  
}
