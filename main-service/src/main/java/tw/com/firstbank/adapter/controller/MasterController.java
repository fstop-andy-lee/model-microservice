package tw.com.firstbank.adapter.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.annotation.WebAdapter;
import tw.com.firstbank.model.MasterDto;
import tw.com.firstbank.service.MasterService;

@WebAdapter
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class MasterController implements MasterApi {

  @Autowired
  MasterService masterService;
  
  @Override
  public ResponseEntity<String> saveMaster(@Valid MasterDto body) {
    log.debug("master input = {}", body.toString());
    String rep = "OK";
    try {
      masterService.save(body);      
    } catch(Exception e) {
      rep = e.getMessage();
    }
    return new ResponseEntity<>(
        rep, 
        HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> doSaga(Integer testCase) {
    String rep = "OK";
    log.debug("saga");
    try {
      String id = "10-110-1234";
      Integer amt = 100;
      Integer status = 1;
      
      MasterDto dto = new MasterDto();
      dto.setId(id);
      dto.setBalance(amt.toString());
      dto.setStatus(status);
      dto.setTestCase(testCase);
      
      log.debug("saga input = {}", dto.toString());
      masterService.sagaUpdate(dto);
          
    } catch(Exception e) {
      rep = e.getMessage();
    }    
    return new ResponseEntity<>(
        rep, 
        HttpStatus.OK);
  }

}
