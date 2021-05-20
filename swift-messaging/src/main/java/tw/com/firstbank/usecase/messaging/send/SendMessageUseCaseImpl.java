package tw.com.firstbank.usecase.messaging.send;

import tw.com.firstbank.usecase.cqrs.CqrsCommandOutput;

public class SendMessageUseCaseImpl implements SendMessageUseCase{
    @Override
    public void execute(SendMessageInput input, CqrsCommandOutput output) {

    }

    @Override
    public SendMessageInput newInput() {
        return null;
    }
}
