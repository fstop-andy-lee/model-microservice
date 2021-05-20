package tw.com.firstbank.usecase.messaging.check;

import tw.com.firstbank.usecase.cqrs.CqrsCommandOutput;

public class CheckMessageUseCaseImpl implements CheckMessageUseCase{
    @Override
    public void execute(CheckMessageInput input, CqrsCommandOutput output) {

    }

    @Override
    public CheckMessageInput newInput() {
        return null;
    }
}
