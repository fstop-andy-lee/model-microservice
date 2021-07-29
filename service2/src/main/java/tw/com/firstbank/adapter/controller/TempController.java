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
import tw.com.firstbank.service.TempService;

@WebAdapter
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class TempController implements TempApi {

  @Autowired
  TempService tempService;
  
  @Override
  public ResponseEntity<String> saveTemp(@Valid MasterDto body) {
    log.debug("temp");
    String rep = "OK";
    try {
      tempService.sagaUpdate(body);      
    } catch(Exception e) {
      rep = e.getMessage();
    }
    return new ResponseEntity<>(
        rep, 
        HttpStatus.OK);
  }

}
