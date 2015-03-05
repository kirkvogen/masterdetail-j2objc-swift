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

import masterdetail.android.viewmodel.ViewModelListener;
import masterdetail.viewmodel.DetailViewModel;
import masterdetail.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.widget.EditText;


/**
 * An activity representing a single DetailEntry detail screen. This activity is only used on
 * handset devices. On tablet-size devices, item details are presented side-by-side with a list of
 * items in a {@link MasterActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than a
 * {@link DetailFragment}.
 */
public class DetailActivity extends Activity implements DetailFragment.Callbacks {

    private DetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state saved from previous
        // configurations of this activity (e.g. when rotating the screen from portrait to
        // landscape). In this case, the fragment will automatically be re-added to its container so
        // we don't need to manually add it. For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(DetailFragment.ARG_ITEM_ID,
                    getIntent().getIntExtra(DetailFragment.ARG_ITEM_ID, 0));
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.masterdetail_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            final EditText title = (EditText) findViewById(R.id.masterdetail_detail_title);
            final EditText words = (EditText) findViewById(R.id.masterdetail_detail_words);

            viewModel.save(title.getText().toString(), words.getText().toString());

            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                // This activity is NOT part of this app's task, so create a new task when
                // navigating up, with a synthesized back stack.
                TaskStackBuilder.create(this)
                        // Add all of this activity's parents to the back stack
                        .addNextIntentWithParentStack(upIntent)
                        // Navigate up to the closest parent
                        .startActivities();
            } else {
                // This activity is part of this app's task, so simply navigate up to the logical
                // parent activity.
                NavUtils.navigateUpTo(this, upIntent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewModelCreated(DetailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onListSaved() {
        // Nothing to do
    }
}
