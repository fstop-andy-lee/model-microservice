package tw.com.firstbank.usecase.cqrs;

import tw.com.firstbank.usecase.Input;
import tw.com.firstbank.usecase.Output;
import tw.com.firstbank.usecase.UseCase;

public interface Query<I extends Input, O extends Output> extends UseCase<I, O> {
	I newInput();
}
