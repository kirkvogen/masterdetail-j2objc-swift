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

package masterdetail.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import masterdetail.model.DetailEntry;
import masterdetail.service.FlatFileDetailService;
import masterdetail.service.DetailService;
import masterdetail.service.StorageService;

import org.junit.After;
import org.junit.Test;

/**
 * Integration tests the {@link FlatFileDetailService}
 */
public class FlatFileDetailServiceIT {

    TempFolderStorageService storageService = new TempFolderStorageService();

    DetailService service = new FlatFileDetailService(storageService);

    @After
    public void deleteFiles() {
        storageService.reset();
    }

    @Test
    public void create() {
        assertThat(service.create(), is(notNullValue()));
    }

    @Test
    public void update() {
        DetailEntry list = service.create();
        list.setTitle("Some title");
        list.setWords(Collections.singletonList("Word1"));

        service.update(list);

        DetailEntry queried = service.find(list.getId());

        assertEquals(list.getTitle(), queried.getTitle());
        assertEquals(list.getWords(), queried.getWords());
    }

    @Test
    public void delete() {
        DetailEntry list = service.create();
        service.delete(list);
        assertThat(service.find(list.getId()), is(nullValue()));
    }

    @Test
    public void findAll() {
        service.create();
        service.create();

        List<DetailEntry> lists = service.findAll();
        assertEquals(2, lists.size());
        assertThat(lists.get(0).getId(), not(equalTo(lists.get(1).getId())));
    }


    class TempFolderStorageService implements StorageService {
        File storageDir;

        @Override
        public File getStoragePath() {
            if (storageDir == null) {
                try {
                    storageDir = File.createTempFile(FlatFileDetailServiceIT.class.getSimpleName(), "tmp");
                } catch (IOException e) {
                    fail("Unable to create temporary folder");
                }
                storageDir.delete();
                storageDir.mkdirs();
            }

            return storageDir;
        }

        public void reset() {
            if (storageDir != null) {
                storageDir.delete();
            }
        }
    }
}
