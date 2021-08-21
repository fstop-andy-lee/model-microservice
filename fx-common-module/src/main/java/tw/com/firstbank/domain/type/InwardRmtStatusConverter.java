package tw.com.firstbank.domain.type;

import javax.persistence.AttributeConverter;

public class InwardRmtStatusConverter implements AttributeConverter<InwardRmtStatus, Integer> {
  @Override
  public Integer convertToDatabaseColumn(InwardRmtStatus status) {
     return (status == null ? null : status.getValue());
  }

  @Override
  public InwardRmtStatus convertToEntityAttribute(Integer dbValue) {
     return InwardRmtStatus.fromInteger(dbValue);
  }
}
