package tw.com.firstbank.model.common;

public class PreconditionViolationException extends Error {

  private static final long serialVersionUID = -1820551103951043496L;

    public PreconditionViolationException(){
        super();
    }

    public PreconditionViolationException(String message){
        super(message);
    }

    public PreconditionViolationException(String message, Throwable cause) {
        super(message, cause);
    }

}
