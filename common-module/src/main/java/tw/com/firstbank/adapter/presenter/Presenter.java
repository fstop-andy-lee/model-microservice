package tw.com.firstbank.adapter.presenter;

public interface Presenter<M extends ViewModel> {
    public M buildViewModel();
}
