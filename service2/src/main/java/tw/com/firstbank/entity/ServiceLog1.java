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
@Table(name = "service_log1")
public class ServiceLog1 extends ServiceLog implements Serializable {
  
  private static final long serialVersionUID = 1256540802415876187L;

}
