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

package es.ffgiraldez.comicsearch.data;

import java.util.List;

import es.ffgiraldez.comicsearch.data.api.RestDataSource;
import es.ffgiraldez.comicsearch.data.transformers.FromVolumeListToSearchResultListTransformer;
import es.ffgiraldez.comicsearch.data.transformers.FromVolumeListToSuggestionListTransformer;
import es.ffgiraldez.comicsearch.model.SearchResult;
import rx.Observable;

public class ComicStorage {

    private final ComicDataSource remoteDataSource;

    public ComicStorage() {
        this(new RestDataSource());
    }

    public ComicStorage(RestDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public Observable<List<String>> fetchSuggestions(String query) {
        return remoteDataSource.fetchSuggestedVolumes(query)
                .compose(new FromVolumeListToSuggestionListTransformer());
    }

    public Observable<List<SearchResult>> fetchSearchResults(String query) {
        return remoteDataSource.fetchVolumes(query)
                .compose(new FromVolumeListToSearchResultListTransformer());
    }
}
