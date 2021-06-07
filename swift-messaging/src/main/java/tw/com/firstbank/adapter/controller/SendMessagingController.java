package tw.com.firstbank.adapter.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.annotation.WebAdapter;
import tw.com.firstbank.model.SwiftMessage;

@WebAdapter
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class SendMessagingController implements SendMessagingApi {

  private final HttpServletRequest request;
  
  @GetMapping(value="/")
  String index() {
    log.debug("index");
    return "ok";
  }
  
  @GetMapping(value="/test")
  void test() {
    log.debug("test");
  }

  @Override
  public ResponseEntity<Void> sendMessage(@Valid SwiftMessage body) {   
    String accept = request.getHeader("Accept");
    log.debug(accept);
    return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
  }
  
}
