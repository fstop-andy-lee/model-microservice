package tw.com.firstbank.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

  public static String getYYYY() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
    return LocalDate.now().format(formatter);
  }
  
}
