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

import java.util.List;

import masterdetail.android.service.LocalStorageService;
import masterdetail.android.service.ServiceAdapter;
import masterdetail.model.DetailEntry;
import masterdetail.service.FlatFileDetailService;
import masterdetail.service.DetailService;
import masterdetail.service.StorageService;
import masterdetail.viewmodel.DetailViewModel;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
//import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A list fragment representing a list of detail entries. This fragment also supports tablet devices
 * by allowing list items to be given an 'activated' state upon selection. This helps indicate which
 * item is currently being viewed in a {@link DetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks} interface.
 */
public class MasterFragment extends ListFragment implements LoaderCallbacks<List<DetailEntry>> {

    private static final int THE_LOADER = 0x01;

    /**
     * The serialization (saved instance state) Bundle key representing the activated item position.
     * Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        public void onItemSelected(int id);
        public void onLoaderCreated(Loader loader);
    }

    private static Callbacks dummyCallbacks = new Callbacks() {
        public void onItemSelected(int id) {}
        public void onLoaderCreated(Loader loader) {}
    };

    private Callbacks callbacks = dummyCallbacks;

    /** The current activated item position. Only used on tablets. */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
     * screen orientation changes).
     */
    public MasterFragment() {
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(getString(R.string.please_create_a_list));
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(THE_LOADER, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(THE_LOADER, null, this).forceLoad();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        callbacks = dummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the fragment is attached to one)
        // that an item has been selected.
        DetailEntry list = (DetailEntry) getListAdapter().getItem(position);
        callbacks.onItemSelected(list.getId());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be given the
     * 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically give items the 'activated'
        // state when touched.
        getListView().setChoiceMode(
                activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public Loader<List<DetailEntry>> onCreateLoader(int id, Bundle args) {
        StorageService storageService = new LocalStorageService(getActivity());
        DetailService detailService = new FlatFileDetailService(storageService);

        MasterDetailLoader loader = new MasterDetailLoader(getActivity(), detailService);

        callbacks.onLoaderCreated(loader);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<DetailEntry>> loader, List<DetailEntry> list) {
        StorageService storageService = new LocalStorageService(getActivity());
        final DetailService detailService = new FlatFileDetailService(storageService);

        ServiceAdapter listAdapter = new ServiceAdapter(detailService) {

            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View row;

                if (null == convertView) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    row = inflater.inflate(android.R.layout.two_line_list_item, parent, false);
                } else {
                    row = convertView;
                }

                DetailViewModel viewModel = new DetailViewModel(detailService, " ");
                viewModel.init(position);

                TextView text1 = (TextView) row.findViewById(android.R.id.text1);
                text1.setText(viewModel.getTitle());

                TextView text2 = (TextView) row.findViewById(android.R.id.text2);
                text2.setText(viewModel.getWords());

                return row;
            }
        };

        setListAdapter(listAdapter);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<DetailEntry>> arg0) {
        // TODO: May need to actually do something here when using a CursorAdapter in the future
        // Note that getListView() will throw an exception. Instead, the adapter may need to be
        // store in an instance variable so that it can be access here.
        // getListView().setAdapter(null);
    }
}
