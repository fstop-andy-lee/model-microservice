package tw.com.firstbank.adapter.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import tw.com.firstbank.model.MasterDto;


@Api(tags="Master API", produces=MediaType.APPLICATION_JSON_VALUE)
@Validated
public interface MasterApi {
  
  @ApiOperation(value="master api", notes="master api")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message  = "message sent"),
      @ApiResponse(code = 400, message = "invalid input, object invalid"),
      @ApiResponse(code = 409, message = "already exists")})
  @RequestMapping(value = "/api/master", consumes = {"application/json"}, method = RequestMethod.POST)
  ResponseEntity<String> saveMaster(
      @ApiParam(value = "MasterDto", required = true)
      @Valid @RequestBody MasterDto body);
    
  @ApiOperation(value="do saga api", notes="do saga api")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message  = "message sent"),
      @ApiResponse(code = 400, message = "invalid input, object invalid"),
      @ApiResponse(code = 409, message = "already exists")})
  @RequestMapping(value = "/api/saga", method = RequestMethod.GET)
  ResponseEntity<String> doSaga(
      @ApiParam(value = "testCase", required = false)
      @RequestParam(name = "testCase", required = false, defaultValue="0") Integer testCase);
  
  @ApiOperation(value="send test mq message", notes="send test mq message")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message  = "message sent"),
      @ApiResponse(code = 400, message = "invalid input, object invalid"),
      @ApiResponse(code = 409, message = "already exists")})
  @RequestMapping(value = "/api/mq", method = RequestMethod.GET)
  ResponseEntity<String> testMq(
      @ApiParam(value = "msg", required = false)
      @RequestParam(name = "msg", required = false, defaultValue="this is a test") String msg);
}
