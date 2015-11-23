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

package es.ffgiraldez.comicsearch.search.domain;

import java.util.List;

import es.ffgiraldez.comicsearch.data.ComicStorage;
import es.ffgiraldez.comicsearch.model.SearchResult;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FetchSearchResultTransformer implements Observable.Transformer<String, List<SearchResult>> {

    private final ComicStorage comicStorage;

    public FetchSearchResultTransformer(ComicStorage comicStorage) {
        this.comicStorage = comicStorage;
    }

    @Override
    public Observable<List<SearchResult>> call(Observable<String> stringObservable) {
        return stringObservable.filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String query) {
                return query != null && !query.isEmpty();
            }
        }).flatMap(new Func1<String, Observable<List<SearchResult>>>() {
            @Override
            public Observable<List<SearchResult>> call(String query) {
                return comicStorage
                        .fetchSearchResults(query)
                        .subscribeOn(Schedulers.io());
            }
        });
    }
}
