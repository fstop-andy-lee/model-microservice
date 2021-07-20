package tw.com.firstbank.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "journal")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "seq", "balance", "status"})
@IdClass(JournalKey.class)
public class Journal {
  
  @Id
  @Column(name = "id", nullable=false)
  private String id;
  
  @Id
  @Column(name = "seq", nullable=false)
  private Integer seq;
  
  @Column(name = "balance", nullable=true)
  private BigDecimal balance;
  
  @Column(name = "status", nullable=true)
  private Integer status;
  
  public void setBalance(BigDecimal amt) {
    this.balance = amt;
  }
  
  public void setBalance(Integer amt) {
    this.balance = BigDecimal.valueOf(amt);
  }

  public void setBalance(Float amt) {
    this.balance = BigDecimal.valueOf(amt);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((seq == null) ? 0 : seq.hashCode());
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
    Journal other = (Journal) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (seq == null) {
      if (other.seq != null)
        return false;
    } else if (!seq.equals(other.seq))
      return false;
    return true;
  }
  
}
