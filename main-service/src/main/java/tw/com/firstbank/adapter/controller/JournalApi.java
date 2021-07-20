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
import tw.com.firstbank.model.MasterDto;

@Api(tags="Journal API", produces=MediaType.APPLICATION_JSON_VALUE)
@Validated
public interface JournalApi {
  @ApiOperation(value="journal api", notes="journal api")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message  = "message sent"),
      @ApiResponse(code = 400, message = "invalid input, object invalid"),
      @ApiResponse(code = 409, message = "already exists")})
  @RequestMapping(value = "/api/journal", consumes = {"application/json"}, method = RequestMethod.POST)
  ResponseEntity<String> saveJournal(
      @ApiParam(value = "MasterDto", required = true)
      @Valid @RequestBody MasterDto body);
}
