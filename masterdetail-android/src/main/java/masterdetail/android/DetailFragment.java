/*
 * Copyright 2014-2015 Kirk C. Vogen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package masterdetail.android;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import masterdetail.android.service.LocalStorageService;
import masterdetail.android.viewmodel.ViewModelListener;
import masterdetail.service.FlatFileDetailService;
import masterdetail.service.StorageService;
import masterdetail.viewmodel.DetailViewModel;

import masterdetail.android.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A fragment representing a single DetailEntry detail screen. This fragment is either contained in
 * a {@link MasterActivity} in two-pane mode (on tablets) or a {@link DetailActivity} on handsets.
 */
public class DetailFragment extends Fragment {

    /** The fragment argument representing the item ID that this fragment represents. */
    public static final String ARG_ITEM_ID = "item_id";

    private DetailViewModel viewModel;

    public interface Callbacks extends ViewModelListener<DetailViewModel> {
        void onListSaved();
    }

    private Callbacks callbacks = dummyCallbacks;

    private static Callbacks dummyCallbacks = new Callbacks() {
        public void onListSaved() {}
        public void onViewModelCreated(DetailViewModel viewModel) {}
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
     * screen orientation changes).
     */
    public DetailFragment() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        callbacks = (Callbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StorageService storageService = new LocalStorageService(getActivity());
        viewModel = new DetailViewModel(new FlatFileDetailService(storageService));
        callbacks.onViewModelCreated(viewModel);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        callbacks = dummyCallbacks;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container,
                false);

        final EditText title = (EditText) rootView.findViewById(
                R.id.masterdetail_detail_title);
        final EditText words = (EditText) rootView.findViewById(
                R.id.masterdetail_detail_words);

        viewModel.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent event) {
            if (DetailViewModel.TITLE.equals(event.getPropertyName())) {
                title.setText((String) event.getNewValue());
            }
            else if (DetailViewModel.WORDS.equals(event.getPropertyName())) {
                words.setText((String) event.getNewValue());
            }
            }
        });

        viewModel.init(getArguments().getInt(ARG_ITEM_ID));

        return rootView;
    }

    @Override
    public void onPause() {
        save();
        super.onPause();
    }

    private void save()
    {
        final EditText title = (EditText) getView().findViewById(R.id.masterdetail_detail_title);
        final EditText words = (EditText) getView().findViewById(R.id.masterdetail_detail_words);
        viewModel.save(title.getText().toString(), words.getText().toString());
        callbacks.onListSaved();
    }
}
