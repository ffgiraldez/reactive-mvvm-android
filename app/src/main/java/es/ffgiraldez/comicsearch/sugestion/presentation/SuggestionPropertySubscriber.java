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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ffgiraldez.comicsearch.rx.PropertySubscriber;
import es.ffgiraldez.comicsearch.rx.PropertyActionBuilder;
import rx.functions.Action1;

public class SuggestionPropertySubscriber extends PropertySubscriber {
    public SuggestionPropertySubscriber(ObservableSuggestionViewModel viewModel) {
        super(createActionBuilder(viewModel));
    }

    private static PropertyActionBuilder createActionBuilder(final ObservableSuggestionViewModel viewModel) {
        Map<String, Action1<?>> actionMap = new HashMap<>();
        actionMap.put(
                ObservableSuggestionViewModel.Property.QUERY.name(),
                new Action1<String>() {
                    @Override
                    public void call(String value) {
                        viewModel.setQuery(value);
                    }
                }
        );

        actionMap.put(
                ObservableSuggestionViewModel.Property.SUGGESTIONS.name(),
                new Action1<List<String>>() {
                    @Override
                    public void call(List<String> value) {
                        viewModel.setSuggestions(value);
                    }
                }
        );

        return new PropertyActionBuilder(actionMap);
    }
}
