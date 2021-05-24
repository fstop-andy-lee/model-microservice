package tw.com.firstbank.adapter.controller;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-22T12:54:58.959Z[GMT]")
public class ApiException extends Exception {
 
  private static final long serialVersionUID = -6577539212756237838L;
    @SuppressWarnings("unused")
    private int code;
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }
}
