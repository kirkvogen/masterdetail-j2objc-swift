package masterdetail.android.viewmodel;

import masterdetail.viewmodel.ViewModel;

/**
 * An interface intended for providing a way for the fragment to provide the view model instance to
 * the activity.
 *
 * @param <E> The view model type
 */
public interface ViewModelListener<E extends ViewModel> {

    void onViewModelCreated(E viewModel);
}
