/*
 * Copyright (C) 2015 Fernando Franco Giráldez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.ffgiraldez.comicsearch.sugestion.presentation;

import java.util.List;
import java.util.concurrent.TimeUnit;

import es.ffgiraldez.comicsearch.ReplaceToVoid;
import es.ffgiraldez.comicsearch.data.ComicStorage;
import es.ffgiraldez.comicsearch.sugestion.domain.FetchQueryTransformer;
import rx.Observable;

public class SuggestionViewModel extends ObservableSuggestionViewModel {

    Observable<Void> updatedSuggestionsObservable;
    private final ComicStorage comicStorage;

    public SuggestionViewModel(ComicStorage comicStorage) {
        this.comicStorage = comicStorage;
    }
    @Override
    public void initialize() {
        Observable<String> input = this.<String>observe(Property.QUERY)
                .debounce(400, TimeUnit.MILLISECONDS);
        Observable<List<String>> suggestionObservable = input
                .compose(new FetchQueryTransformer(comicStorage));

        subscribe(Property.SUGGESTIONS, suggestionObservable);

        Observable<List<String>> suggestionPropObservable = observe(Property.SUGGESTIONS);
        updatedSuggestionsObservable = suggestionPropObservable.map(new ReplaceToVoid<List<String>>());
    }

    public Observable<Void> didUpdateSuggestion() {
        return updatedSuggestionsObservable;
    }

    public int suggestionSize() {
        return suggestions.size();
    }

    public String suggestionAt(int position) {
        return suggestions.get(position);
    }

    private Observable<List<String>> fetchSuggestions(String query) {
        return comicStorage.fetchSuggestions(query);
    }
}
