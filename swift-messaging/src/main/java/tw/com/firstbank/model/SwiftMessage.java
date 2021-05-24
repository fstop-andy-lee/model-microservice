package tw.com.firstbank.model;

import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description = "Swift Message")
@Validated
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "valueDate"})
public class SwiftMessage {
  
  //@Schema(example = "d290f1ee-6c54-4b01-90e6-d701748f0851", required = true, description = "唯一值")
  @ApiModelProperty(example = "d290f1ee-6c54-4b01-90e6-d701748f0851", notes = "唯一值")
  @NotNull
  @Valid
  @JsonProperty("id")
  private UUID id = null;
  
  //@Schema(example = "2016/08/29", required = true, description = "交易日")
  @ApiModelProperty(example = "2016/08/29", notes = "交易日")
  @NotNull
  @Valid
  @JsonProperty("valueDate")
  @JsonFormat(pattern="yyyy/MM/dd")
  private Date valueDate;
  
}
