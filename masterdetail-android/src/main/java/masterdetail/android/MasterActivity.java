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

import masterdetail.android.service.LocalStorageService;
import masterdetail.model.DetailEntry;
import masterdetail.service.FlatFileDetailService;
import masterdetail.service.DetailService;
import masterdetail.service.StorageService;
import masterdetail.viewmodel.DetailViewModel;

import android.app.Activity;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


/**
 * An activity representing a list of detail entries. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link DetailActivity} representing item details. On tablets, the activity
 * presents the list of items and item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a {@link MasterFragment} and the
 * item details (if present) is a {@link DetailFragment}.
 * <p>
 * This activity also implements the required {@link MasterFragment.Callbacks} interface to listen
 * for item selections.
 */
public class MasterActivity extends Activity
        implements MasterFragment.Callbacks, DetailFragment.Callbacks {

    private Loader loader;

    /** Whether or not the activity is in two-pane mode, i.e. running on a tablet device. */
    private boolean twoPane;

    private DetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        if (findViewById(R.id.masterdetail_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/values-large and res/values-sw600dp). If this view is present, then the activity
            // should be in two-pane mode.
            twoPane = true;

            // In two-pane mode, list items should be given the 'activated' state when touched.
            ((MasterFragment) getFragmentManager()
                    .findFragmentById(R.id.masterdetail_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link MasterFragment.Callbacks} indicating that the item with the given
     * ID was selected.
     */
    @Override
    public void onItemSelected(int id) {
        if (twoPane) {
            // In two-pane mode, show the detail view in this activity by adding or replacing the
            // detail fragment using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(DetailFragment.ARG_ITEM_ID, id);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.masterdetail_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity for the selected item ID.
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra(DetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onLoaderCreated(Loader loader) {
        this.loader = loader;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_master_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void newAction(MenuItem menuItem)
    {
        StorageService storageService = new LocalStorageService(this);
        DetailService detailService = new FlatFileDetailService(storageService);
        DetailEntry list = detailService.create();
        int id = list.getId();
        if (twoPane) {
            // In two-pane mode, show the detail view in this activity by adding or replacing
            // the detail fragment using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(DetailFragment.ARG_ITEM_ID, id);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.masterdetail_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity for the selected item ID.
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra(DetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onViewModelCreated(DetailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onListSaved() {
        loader.onContentChanged();
    }
}
