package tw.com.firstbank.adapter.controller;

import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.annotation.WebAdapter;

@WebAdapter
@RequiredArgsConstructor
@Slf4j
public class SendMessagingController {

  @GetMapping(value="/test")
  void test() {
    log.debug("test");
  }
  
}
