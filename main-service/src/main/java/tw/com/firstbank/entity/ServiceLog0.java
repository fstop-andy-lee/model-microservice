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
@Table(name = "service_log0")
public class ServiceLog0 extends ServiceLog implements Serializable {

  private static final long serialVersionUID = -1373397399424215693L;

}
