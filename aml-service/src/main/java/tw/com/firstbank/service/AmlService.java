package tw.com.firstbank.service;

public interface AmlService {
  /**
   * 名單檢核
   * @param name
   * @return 0 = ok, other = hit
   */
  public Integer screen(String name);
}
