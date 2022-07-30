package tw.com.firstbank.service;

import tw.com.firstbank.model.MasterDto;
import tw.com.firstbank.model.SagaGenericDto;

public interface CamundaTaskService {
  public void startTask(SagaGenericDto<MasterDto> body);
}
