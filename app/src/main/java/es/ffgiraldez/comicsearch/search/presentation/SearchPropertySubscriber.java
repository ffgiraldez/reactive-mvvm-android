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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ffgiraldez.comicsearch.model.SearchResult;
import es.ffgiraldez.comicsearch.rx.PropertyActionBuilder;
import es.ffgiraldez.comicsearch.rx.PropertySubscriber;
import rx.functions.Action1;

public class SearchPropertySubscriber extends PropertySubscriber {
    public SearchPropertySubscriber(ObservableSearchViewModel viewModel) {
        super(createBuilder(viewModel));
    }

    private static PropertyActionBuilder createBuilder(final ObservableSearchViewModel viewModel) {
        Map<String, Action1<?>> actionMap = new HashMap<>();
        actionMap.put(
                ObservableSearchViewModel.Property.QUERY.name(),
                new Action1<String>() {
                    @Override
                    public void call(String query) {
                        viewModel.setQuery(query);
                    }
                }
        );

        actionMap.put(
                ObservableSearchViewModel.Property.RESULTS.name(),
                new Action1<List<SearchResult>>() {
                    @Override
                    public void call(List<SearchResult> results) {
                        viewModel.setResults(results);
                    }
                }
        );
        return new PropertyActionBuilder(actionMap);
    }
}
