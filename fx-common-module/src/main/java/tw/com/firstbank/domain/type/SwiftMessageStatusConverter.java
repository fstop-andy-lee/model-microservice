package tw.com.firstbank.domain.type;

import javax.persistence.AttributeConverter;

public class SwiftMessageStatusConverter implements AttributeConverter<SwiftMessageStatus, Integer> {
  @Override
  public Integer convertToDatabaseColumn(SwiftMessageStatus status) {
     return (status == null ? null : status.getValue());
  }

  @Override
  public SwiftMessageStatus convertToEntityAttribute(Integer dbValue) {
     return SwiftMessageStatus.fromInteger(dbValue);
  }
}
