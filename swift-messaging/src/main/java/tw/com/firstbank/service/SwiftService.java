package tw.com.firstbank.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface SwiftService {
  public Integer uploadSwiftFiles(List<MultipartFile> files) throws IOException;
  
  public Integer send(Integer records);
}
