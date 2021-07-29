package tw.com.firstbank.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "service_log2")
public class ServiceLog2 extends ServiceLog implements Serializable {
  
  private static final long serialVersionUID = 5635029564188847957L;

}
