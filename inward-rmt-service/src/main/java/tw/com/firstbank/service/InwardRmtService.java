package tw.com.firstbank.service;

import tw.com.firstbank.model.InwardRmtDto;

public interface InwardRmtService {
  public InwardRmtDto processInwardRmt(InwardRmtDto dto);
  public Integer processVerifiedInwardRmt();
  public Integer processVerifyPendingInwardRmt();
}
