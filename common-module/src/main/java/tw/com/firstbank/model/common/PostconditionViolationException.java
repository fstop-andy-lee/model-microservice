package tw.com.firstbank.model.common;

public class PostconditionViolationException extends Error {

  private static final long serialVersionUID = 3436569878063059953L;

    public PostconditionViolationException(){
        super();
    }

    public PostconditionViolationException(String message){
        super(message);
    }

    public PostconditionViolationException(String message, Throwable cause) {
        super(message, cause);
    }

}
