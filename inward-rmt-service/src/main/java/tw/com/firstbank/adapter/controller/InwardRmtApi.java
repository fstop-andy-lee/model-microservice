package tw.com.firstbank.adapter.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags="Inward RMT API", produces=MediaType.APPLICATION_JSON_VALUE)
@Validated
public interface InwardRmtApi {
  @ApiOperation(value = "",
      notes = "Process Verified Pending Records.\n"
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
      notes = "Verify Pending Records.\n"
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
  
}
