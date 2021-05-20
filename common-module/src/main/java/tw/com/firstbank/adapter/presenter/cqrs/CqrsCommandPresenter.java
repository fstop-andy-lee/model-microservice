package tw.com.firstbank.adapter.presenter.cqrs;

import tw.com.firstbank.adapter.presenter.Presenter;
import tw.com.firstbank.usecase.Output;
import tw.com.firstbank.usecase.Result;
import tw.com.firstbank.usecase.cqrs.CqrsCommandOutput;

public class CqrsCommandPresenter extends Result implements Presenter<CqrsCommandViewModel>, CqrsCommandOutput {

    private CqrsCommandViewModel viewModel;

    private CqrsCommandPresenter(){
        super();
        viewModel = new CqrsCommandViewModel();
    }

    public static CqrsCommandPresenter newInstance(){
        return new CqrsCommandPresenter();
    }


    @Override
    public CqrsCommandViewModel buildViewModel() {
        return viewModel;
    }

    @Override
    public String getId() {
        return viewModel.getId();
    }

    @Override
    public CqrsCommandOutput setId(String id) {
        viewModel.setId(id);
        return this;
    }

    @Override
    public Output setMessage(String message) {
        super.setMessage(message);
        viewModel.setMessage(message);
        return this;
    }
}
