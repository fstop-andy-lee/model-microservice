package tw.com.firstbank.adapter.gateway;

import tw.com.firstbank.model.InwardRmtDto;

public interface InwardRmtGateway {
  
  public Integer processInwardRmtByEvent(InwardRmtDto dto);
  
  public Integer processInwardRmtByApi(InwardRmtDto dto);

}
