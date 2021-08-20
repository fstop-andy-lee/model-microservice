package tw.com.firstbank.domain.type;

import javax.persistence.AttributeConverter;

public class UserStatusConverter implements AttributeConverter<UserStatus, Integer> {
  @Override
  public Integer convertToDatabaseColumn(UserStatus status) {
     return (status == null ? null : status.getValue());
  }

  @Override
  public UserStatus convertToEntityAttribute(Integer dbValue) {
     return UserStatus.fromInteger(dbValue);
  }
}
