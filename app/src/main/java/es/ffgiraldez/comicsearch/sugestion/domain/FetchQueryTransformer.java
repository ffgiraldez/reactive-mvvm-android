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

package es.ffgiraldez.comicsearch.sugestion.domain;

import java.util.Collections;
import java.util.List;

import es.ffgiraldez.comicsearch.data.ComicStorage;
import rx.Observable;
import rx.functions.Func1;

public class FetchQueryTransformer implements Observable.Transformer<String, List<String>> {

    private final ComicStorage comicStorage;

    public FetchQueryTransformer(ComicStorage comicStorage) {
        this.comicStorage = comicStorage;
    }

    @Override
    public Observable<List<String>> call(Observable<String> input) {
        return input.onErrorReturn(new Func1<Throwable, String>() {
            @Override
            public String call(Throwable throwable) {
                return throwable.getMessage();
            }
        }).switchMap(new Func1<String, Observable<List<String>>>() {
            @Override
            public Observable<List<String>> call(String query) {
                return (query != null && query.length() >= 3)
                        ? fetchSuggestions(query) : Observable.just(Collections.<String>emptyList());
            }
        });
    }

    private Observable<List<String>> fetchSuggestions(String query) {
        return comicStorage.fetchSuggestions(query);
    }
}
