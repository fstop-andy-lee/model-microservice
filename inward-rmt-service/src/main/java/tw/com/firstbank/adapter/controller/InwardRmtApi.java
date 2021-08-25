package tw.com.firstbank.adapter.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import tw.com.firstbank.model.InwardRmtDto;

@Api(tags="Inward RMT API", produces=MediaType.APPLICATION_JSON_VALUE)
@Validated
public interface InwardRmtApi {
  @ApiOperation(value = "",
      notes = "Process Verified Pending Records. 放行\n"
              + " \n")
      @ApiResponses(value = {
             @ApiResponse(code = 200, message = "SUCCESS")
           , @ApiResponse(code = 400, message = "INVALID_PARAM")
           , @ApiResponse(code = 1999, message = "EXCEPTION")
           , @ApiResponse(code = 1002, message = "PARAM_VALUE_FAIL")
          })
  @RequestMapping(method = {RequestMethod.GET}    
      , value = "/verified"
      , produces = {MediaType.APPLICATION_JSON_VALUE}
      , consumes = {MediaType.ALL_VALUE}
          )
  @ResponseBody
  ResponseEntity<Integer> verified();
  
  @ApiOperation(value = "",
      notes = "Verify Pending Records. 審核\n"
              + " \n")
      @ApiResponses(value = {
             @ApiResponse(code = 200, message = "SUCCESS")
           , @ApiResponse(code = 400, message = "INVALID_PARAM")
           , @ApiResponse(code = 1999, message = "EXCEPTION")
           , @ApiResponse(code = 1002, message = "PARAM_VALUE_FAIL")
          })
  @RequestMapping(method = {RequestMethod.GET}    
      , value = "/verify"
      , produces = {MediaType.APPLICATION_JSON_VALUE}
      , consumes = {MediaType.ALL_VALUE}
          )
  @ResponseBody
  ResponseEntity<Integer> verify();
  
  @ApiOperation(value="Input Inward RMT", notes="Input Inward RMT")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message  = "OK"),
      @ApiResponse(code = 400, message = "invalid input, object invalid"),
      @ApiResponse(code = 409, message = "already exists")})
  @RequestMapping(value = "/send", consumes = {"application/json"}, method = RequestMethod.POST)
  ResponseEntity<InwardRmtDto> send(
      @ApiParam(value = "InwardRmtDto", required = true)
      @Valid @RequestBody InwardRmtDto body);

  
}
