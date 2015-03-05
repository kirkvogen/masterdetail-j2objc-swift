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
package masterdetail.viewmodel;

import java.util.Arrays;
import java.util.List;

import masterdetail.model.DetailEntry;
import masterdetail.service.DetailService;

/**
 * A view model for presenting detail items.
 */
public class DetailViewModel extends ViewModel {

    public static final String TITLE = "title";
    public static final String WORDS = "words";

    private String title;
    private String words;
    private DetailService detailService;
    private DetailEntry detailEntry = new DetailEntry();
    private String listDelimiter = System.getProperty("line.separator");

    public DetailViewModel(DetailService detailService) {
        this.detailService = detailService;
    }

    /**
     * Creates a new instance using the provided delimiter. The delimiter will be used to display
     * the content in the view. For example, using newline as a delimiter will present each word
     * on one line. Another view might wish to present the words as one line, so a space character
     * could be used.
     *
     * @param detailService The service for detail item persistence
     * @param listDelimiter The delimiter.
     */
    public DetailViewModel(DetailService detailService, String listDelimiter) {
        this(detailService);
        this.listDelimiter = listDelimiter;
    }

    public void init(int id) {
        detailEntry = detailService.find(id);
        setTitle(detailEntry.getTitle());
        setWords(detailEntry.getWords());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        firePropertyChange(TITLE, this.title, title);
        this.title = title;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        firePropertyChange(WORDS, this.words, words);
        this.words = words;
    }

    public void setWords(List<String> words) {
        if (words == null)
            return;

        StringBuilder content = new StringBuilder();
        for (String word : words) {
            content.append(word);
            content.append(listDelimiter);
        }

        setWords(content.toString());
    }

    public void save(String newTitle, String newWords) {
        title = newTitle;
        words = newWords;

        detailEntry.setTitle(title);

        detailEntry.setWords(Arrays.asList(words.split(System.getProperty("line.separator"))));

        detailService.update(detailEntry);
    }
}
