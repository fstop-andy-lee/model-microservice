package tw.com.firstbank.adapter.gateway;

import tw.com.firstbank.model.InwardRmtDto;

public interface InwardRmtGateway {
  
  public InwardRmtDto processInwardRmtByEvent(InwardRmtDto dto);
  
  public void processInwardRmtByAsyncEvent(InwardRmtDto dto);
  
  public InwardRmtDto processInwardRmtByApi(InwardRmtDto dto);

}
