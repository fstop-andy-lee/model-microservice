package tw.com.firstbank.adapter.controller;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
    date = "2021-05-22T12:54:58.959Z[GMT]")
public class NotFoundException extends ApiException {

  private static final long serialVersionUID = -5351970871178293691L;
  @SuppressWarnings("unused")
  private int code;

  public NotFoundException(int code, String msg) {
    super(code, msg);
    this.code = code;
  }
}
