package tw.com.firstbank.model;

import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description = "Swift Message")
@JsonIgnoreProperties(ignoreUnknown = true)
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SwiftMessage other = (SwiftMessage) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
  
  
}
