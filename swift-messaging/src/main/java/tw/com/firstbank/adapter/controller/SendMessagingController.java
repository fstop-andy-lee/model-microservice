package tw.com.firstbank.adapter.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import io.swagger.annotations.ApiOperation;
//import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.annotation.WebAdapter;
import tw.com.firstbank.model.SwiftMessageDto;
import tw.com.firstbank.service.SwiftService;
import tw.com.firstbank.util.DateTimeUtil;

@Slf4j
@WebAdapter
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(
    value = "/swift/v1"
  , produces = {MediaType.APPLICATION_JSON_VALUE}
  , headers = {"content-type=application/x-www-form-urlencoded"}
  , consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE}
  )
public class SendMessagingController implements SendMessagingApi {

  private final HttpServletRequest request;
  
  @Autowired
  private RestTemplate restTemplate;
  
  @Autowired
  private SwiftService service;
  
  @Value("${server.port}")
  private int port;
  
  //@Autowired
  //private Tracer tracer;
  
  //@Hidden
  @ApiOperation(value = "This method is hidden.", hidden = true)
  @GetMapping(value="/")
  ResponseEntity<String> index() {
    log.debug("index");
    return new ResponseEntity<>(
        "OK", 
        HttpStatus.OK);
  }
  
  @ApiOperation(value = "This method is hidden.", hidden = true)
  @GetMapping(value="/test")
  ResponseEntity<String> test() {
    log.debug("test");
    return new ResponseEntity<>(
        "test", 
        HttpStatus.OK);
  }
  
  // spring mvc 方式，一定報錯
  @ApiOperation(value = "This method is hidden.", hidden = true)
  @RequestMapping("/e-tracing")
  public String eTracing() throws InterruptedException {
      Thread.sleep(100);
      return "tracing";
  }

  //spring mvc 方式，一定報錯
  @ApiOperation(value = "This method is hidden.", hidden = true)
  @RequestMapping("/e-open")
  public String eOpen() throws InterruptedException {
      ResponseEntity<String> response = 
        restTemplate.getForEntity("http://localhost:" + port + "/e-tracing", 
                                  String.class);
      Thread.sleep(200);
      return "open " + response.getBody();
  }
  
  @ApiOperation(value = "This method is hidden.", hidden = true)
  @GetMapping("/open")
  public ResponseEntity<String> open() throws InterruptedException {
      ResponseEntity<String> response = 
        restTemplate.getForEntity("http://localhost:" + port + "/tracing", 
                                  String.class);
      Thread.sleep(200);
      return new ResponseEntity<>(
          "open " + response.getBody(), 
          HttpStatus.OK);
  }
  
  @ApiOperation(value = "This method is hidden.", hidden = true)
  @GetMapping("/tracing")
  public ResponseEntity<String> tracing() throws InterruptedException {
      Thread.sleep(100);
      return new ResponseEntity<>(
          "tracing", 
          HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> sendMessage(@Valid SwiftMessageDto body) {   
    String accept = request.getHeader("Accept");
    log.debug(accept);
    return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
  }

  @Override
  public ResponseEntity<Integer> uploadMessage(List<MultipartFile> files,
      HttpServletRequest httpServletReq) {
    Integer ret = 0;
    try {
      ret = service.uploadSwiftFiles(files);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }
    return new ResponseEntity<Integer>(ret, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Integer> send(@RequestParam Integer records) {
    Integer ret = 0;
    try {
      log.debug(DateTimeUtil.getYYYY());
      
      ret = service.send(records);
      
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }
    return new ResponseEntity<Integer>(ret, HttpStatus.OK);
  }
  
}
