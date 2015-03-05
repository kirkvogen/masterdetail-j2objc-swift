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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import masterdetail.model.DetailEntry;

/**
 * A service implementation that uses flat files for persisting items.
 */
public class FlatFileDetailService implements DetailService {

    private StorageService storageService;

    public FlatFileDetailService(StorageService storageService) {
        this.storageService = storageService;
    }

    private File getStorageDir() {
        File storageDir = new File(this.storageService.getStoragePath(), "list");
        storageDir.mkdirs();

        return storageDir;
    }

    private File getFile(int filenum) {
        return new File(getStorageDir(), String.valueOf(filenum));
    }

    @Override
    public DetailEntry create() {
        List<Integer> ids = findAllIds();

        int newId = 0;
        int lastId = newId;

        for (int id : ids) {
            newId = id + 1;

            if (lastId + 1 < newId) {
                newId = lastId;
                break;
            }

            lastId = newId;
        }

        createEmptyFile(newId);

        DetailEntry list = new DetailEntry();
        list.setId(newId);
        return list;
    }

    private void createEmptyFile(int newId) {
        File newFile = new File(getStorageDir(), String.valueOf(newId));

        try {
            newFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Unable to create item with ID: " + newId, e);
        }
    }

    @Override
    public void delete(int id) {
        getFile(id).delete();
    }

    @Override
    public void delete(DetailEntry detailEntry) {
        delete(detailEntry.getId());
    }

    @Override
    public void update(DetailEntry detailEntry) {
        FileWriter out = null;

        try {
            out = new FileWriter(getFile(detailEntry.getId()));
            writeContent(detailEntry.getTitle(), detailEntry.getWords(), out);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write updates to item with ID: "
                    + detailEntry.getId(), e);
        } finally {
            closeQuietly(out);
        }
    }

    private void writeContent(String title, List<String> words, Writer out) throws IOException {
        BufferedWriter buf = new BufferedWriter(out);

        buf.write(title);
        buf.newLine();

        for (String word : words) {
            buf.write(word);
            buf.newLine();
        }

        buf.flush();
    }

    @Override
    public DetailEntry find(int id) {
        FileReader in = null;
        DetailEntry list = null;

        File file = getFile(id);
        if (!file.exists())
        {
            return null;
        }

        try    {
            in = new FileReader(file);
            list = readContent(in);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read the item with ID: " + id, e);
        } finally {
            closeQuietly(in);
        }

        list.setId(id);
        return list;
    }

    private static void closeQuietly(Reader in)    {
        if (in != null)
        {
            try {
                in.close();
            } catch (IOException e) {
                // There is nothing more that can be done
            }
        }
    }

    private static void closeQuietly(Writer out)    {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                // There is nothing more that can be done
            }
        }
    }

    private static DetailEntry readContent(Reader in) throws IOException {
        ArrayList<String> words = new ArrayList<String> ();
        DetailEntry list = new DetailEntry();

        BufferedReader buf = new BufferedReader(in);
        list.setTitle(buf.readLine());

        String line;
        while ((line = buf.readLine()) != null)    {
            words.add(line);
        }

        list.setWords(words);
        return list;
    }

    @Override
    public Boolean exists(int id) {
        return getFile(id).exists();
    }

    @Override
    public List<DetailEntry> findAll() {
        ArrayList<DetailEntry> detailEntries = new ArrayList<DetailEntry> ();

        for (int id : findAllIds()) {
            detailEntries.add(find(id));
        }

        return detailEntries;
    }

    private List<Integer> findAllIds() {
        ArrayList<Integer> ids = new ArrayList<Integer> ();

        for (File file : getStorageDir().listFiles()) {
            ids.add(Integer.valueOf(file.getName()));
        }

        Collections.sort(ids);

        return ids;
    }
}
