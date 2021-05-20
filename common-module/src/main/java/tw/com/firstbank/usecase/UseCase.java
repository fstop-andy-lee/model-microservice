package tw.com.firstbank.usecase;


public interface UseCase<I extends Input, O extends Output> {
	void execute(I input, O output);
}
