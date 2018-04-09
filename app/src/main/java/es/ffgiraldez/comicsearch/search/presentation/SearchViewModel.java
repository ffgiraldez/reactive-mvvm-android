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

package es.ffgiraldez.comicsearch.search.presentation;

import java.util.List;

import es.ffgiraldez.comicsearch.data.ComicStorage;
import es.ffgiraldez.comicsearch.model.SearchResult;
import es.ffgiraldez.comicsearch.rx.Replacements;
import es.ffgiraldez.comicsearch.search.domain.FetchSearchResultTransformer;
import rx.Observable;

public class SearchViewModel extends ObservableSearchViewModel {
    private static final String TAG = SearchViewModel.class.getSimpleName();
    private Observable<Void> updatedResultsObservable;
    private final ComicStorage comicStorage;

    public SearchViewModel(ComicStorage comicStorage) {
        this.comicStorage = comicStorage;
    }

    @Override
    public void initialize() {
        Observable<List<SearchResult>> fetchResultObservable = this.<String>observe(Property.QUERY)
                .compose(new FetchSearchResultTransformer(comicStorage));

        subscribe(Property.RESULTS, fetchResultObservable);
        Observable<List<String>> resultsObservable = observe(Property.RESULTS);
        updatedResultsObservable = resultsObservable.map(Replacements.returnVoid());
    }

    public Observable<Void> didUpdateResults() {
        return updatedResultsObservable;
    }
}
