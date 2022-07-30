package tw.com.firstbank.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"flowId", "data"})
public class SagaGenericDto<T> implements Serializable {

  private static final long serialVersionUID = -5568069271965160368L;
  
  private String flowId;
  
  private T data;
  
}
