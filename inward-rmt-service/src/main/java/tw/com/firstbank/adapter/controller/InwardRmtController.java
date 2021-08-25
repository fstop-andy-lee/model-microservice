package tw.com.firstbank.adapter.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.annotation.WebAdapter;
import tw.com.firstbank.model.InwardRmtDto;
import tw.com.firstbank.service.InwardRmtService;

@Slf4j
@RequiredArgsConstructor
@WebAdapter
@CrossOrigin
@RequestMapping(
    value = "/inward-rmt/v1"
  , produces = {MediaType.APPLICATION_JSON_VALUE}
  , headers = {"content-type=application/x-www-form-urlencoded"}
  , consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE}
  )
public class InwardRmtController implements InwardRmtApi {

  @Autowired
  private InwardRmtService inwardRmtService;
  
  @Override
  public ResponseEntity<Integer> verified() {
    Integer ret = 0;
    try {
      
      ret = inwardRmtService.processVerifiedInwardRmt();
      
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResponseEntity<Integer>(ret, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Integer> verify() {
    Integer ret = 0;
    try {
      
      ret = inwardRmtService.processVerifyPendingInwardRmt();
      
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResponseEntity<Integer>(ret, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<InwardRmtDto> send(@Valid InwardRmtDto body) {
    InwardRmtDto ret = null;
    try {
      body.setInstAmt(body.getApiInstAmt());
      log.debug("AMT={}", body.getInstAmt());
      ret = inwardRmtService.processInwardRmt(body);      
      return new ResponseEntity<>(ret, HttpStatus.OK);
    } catch(Exception e) {
      log.error(e.getMessage());
    }
    return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
  }

}
