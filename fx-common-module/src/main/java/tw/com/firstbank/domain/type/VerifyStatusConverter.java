package tw.com.firstbank.domain.type;

import javax.persistence.AttributeConverter;

public class VerifyStatusConverter implements AttributeConverter<VerifyStatus, Integer> {
  @Override
  public Integer convertToDatabaseColumn(VerifyStatus status) {
     return (status == null ? null : status.getValue());
  }

  @Override
  public VerifyStatus convertToEntityAttribute(Integer dbValue) {
     return VerifyStatus.fromInteger(dbValue);
  }
}
