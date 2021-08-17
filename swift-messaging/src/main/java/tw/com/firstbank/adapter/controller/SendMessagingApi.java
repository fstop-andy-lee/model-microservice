package tw.com.firstbank.adapter.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import tw.com.firstbank.model.SwiftMessageDto;

@Api(tags="Send Swift Message API", produces=MediaType.APPLICATION_JSON_VALUE)
@Validated
public interface SendMessagingApi {
  
  //@Operation(summary = "send swift message", description = "send swift message", tags = {"swift"})
  @ApiOperation(value="send swift message", notes="send swift message")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message  = "message sent"),
      @ApiResponse(code = 400, message = "invalid input, object invalid"),
      @ApiResponse(code = 409, message = "an existing message already exists")})
  @RequestMapping(value = "/send-message", consumes = {"application/json"}, method = RequestMethod.POST)
  ResponseEntity<Void> sendMessage(
      //@Parameter(in = ParameterIn.DEFAULT, description = "Swift Message to send", schema=@Schema())
      @ApiParam(value = "SwiftMessage", required = true)
      @Valid @RequestBody SwiftMessageDto body);
  
  @ApiOperation(value = "",
      notes = "Upload Swift Files\n"
              + "Don't test this api from swagger ui form, it won't work! \n")
      @ApiResponses(value = {
             @ApiResponse(code = 200, message = "SUCCESS")
           , @ApiResponse(code = 400, message = "INVALID_PARAM")
           , @ApiResponse(code = 1999, message = "EXCEPTION")
           , @ApiResponse(code = 1002, message = "PARAM_VALUE_FAIL")
          })
  @RequestMapping(method = {RequestMethod.POST}    
      , value = "/upload"
      , produces = {MediaType.APPLICATION_JSON_VALUE}
      , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE
          , MediaType.APPLICATION_JSON_VALUE
          , MediaType.MULTIPART_MIXED_VALUE}
          )
  @ResponseBody
  ResponseEntity<Integer> uploadMessage(
      @ApiParam(value = "Swift files", required = true)  
      @RequestPart(name="files", required=false) List<MultipartFile> files
      , @ApiParam(hidden = true) HttpServletRequest httpServletReq);
  
}
