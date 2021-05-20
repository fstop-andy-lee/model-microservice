package tw.com.firstbank.usecase.messaging.send;

import tw.com.firstbank.usecase.cqrs.Command;
import tw.com.firstbank.usecase.cqrs.CqrsCommandOutput;

public interface SendMessageUseCase extends Command<SendMessageInput, CqrsCommandOutput> {
}
