package tw.com.firstbank.usecase.messaging.check;

import tw.com.firstbank.usecase.cqrs.Command;
import tw.com.firstbank.usecase.cqrs.CqrsCommandOutput;

public interface CheckMessageUseCase extends Command<CheckMessageInput, CqrsCommandOutput> {
}
