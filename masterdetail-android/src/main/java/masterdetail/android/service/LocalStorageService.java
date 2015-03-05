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

import java.io.File;

import masterdetail.service.StorageService;

import android.content.Context;

/**
 * An Android-specific implementation that returns the app's root storage path.
 */
public class LocalStorageService implements StorageService {

    private Context context;

    public LocalStorageService(Context context)
    {
        this.context = context;
    }

    @Override
    public File getStoragePath() {
        return context.getFilesDir();
    }
}
