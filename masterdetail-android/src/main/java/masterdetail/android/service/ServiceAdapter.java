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
package masterdetail.android.service;

import masterdetail.model.DetailEntry;
import masterdetail.service.DetailService;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Adapts the list service so that it can be used with list views.
 */
public class ServiceAdapter extends BaseAdapter {

    private DetailService service;

    public ServiceAdapter(DetailService service) {
        this.service = service;
    }

    @Override
    public int getCount() {
        return service.findAll().size();
    }

    @Override
    public DetailEntry getItem(int position) {
        return service.find(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
