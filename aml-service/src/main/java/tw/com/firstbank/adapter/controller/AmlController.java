package tw.com.firstbank.adapter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.annotation.WebAdapter;
import tw.com.firstbank.service.AmlService;

@Slf4j
@WebAdapter
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(
    value = "/aml/v1"
  , produces = {MediaType.APPLICATION_JSON_VALUE}
  , headers = {"content-type=application/x-www-form-urlencoded"}
  , consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE}
  )
public class AmlController implements AmlApi {
  
  @Autowired
  private AmlService service;

  @Override
  public ResponseEntity<Integer> screen(String name) {
    Integer ret = 0;
    try {
     
      ret = service.screen(name);      
      
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }
    return new ResponseEntity<Integer>(ret, HttpStatus.OK);
  }

}
