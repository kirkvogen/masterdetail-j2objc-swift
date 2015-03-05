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

import masterdetail.model.DetailEntry;
import masterdetail.service.DetailService;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class MasterDetailLoader extends AsyncTaskLoader<List<DetailEntry>> {

    private DetailService detailService;

    private List<DetailEntry> data;

    public MasterDetailLoader(Context ctx, DetailService detailService) {
        super(ctx);
        this.detailService = detailService;
    }

    @Override
    public List<DetailEntry> loadInBackground() {
        List<DetailEntry> lists = detailService.findAll();

        return lists;
    }

    @Override
    public void deliverResult(List<DetailEntry> newData) {
        if (isReset()) {
            releaseResources(newData);
            return;
        }

        List<DetailEntry> oldData = data;
        data = newData;

        if (isStarted())
        {
            super.deliverResult(newData);
        }

        if (oldData != null && oldData != newData) {
            releaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        }

        if (takeContentChanged() || data == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();

        if (data != null) {
            releaseResources(data);
            data = null;
        }
    }

    private void releaseResources(List<DetailEntry> releasedData) {
        // All the resources associated with the loader should be released here. For example, a
        // cursor should be closed.
        //
        // For the DetailService, there is nothing to release.
    }

}
