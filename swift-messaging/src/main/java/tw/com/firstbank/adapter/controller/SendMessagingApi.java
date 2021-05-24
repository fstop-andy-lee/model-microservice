package tw.com.firstbank.adapter.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import tw.com.firstbank.model.SwiftMessage;

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
      @Valid @RequestBody SwiftMessage body);
}
