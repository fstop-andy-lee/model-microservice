package tw.com.firstbank.usecase.cqrs;

import tw.com.firstbank.usecase.Output;

public interface CqrsCommandOutput extends Output {

	String getId();

	CqrsCommandOutput setId(String id);

}
