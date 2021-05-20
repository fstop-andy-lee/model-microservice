package tw.com.firstbank.usecase;

public interface Output {

    String getMessage();

    Output setMessage(String message);

    ExitCode getExitCode();

    Output setExitCode(ExitCode exitCode);

}
