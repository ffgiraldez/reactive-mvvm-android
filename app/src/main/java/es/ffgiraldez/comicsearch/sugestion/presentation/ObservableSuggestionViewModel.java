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

import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.ffgiraldez.comicsearch.rx.ObservableViewModel;
import es.ffgiraldez.comicsearch.rx.PropertyObservableProvider;
import es.ffgiraldez.comicsearch.rx.PropertySubscriber;
import rx.Observable;
import rx.functions.Action1;

public abstract class ObservableSuggestionViewModel extends AbstractSuggestionViewModel implements ObservableViewModel {

    public enum Property {QUERY, SUGGESTIONS}

    private static final Set<String> PROPERTIES = new HashSet<>(Arrays.asList(
            Property.QUERY.name(), Property.SUGGESTIONS.name()
    ));

    private final PropertyObservableProvider propertyObservableProvider;
    private final PropertySubscriber propertySubscriber;

    public ObservableSuggestionViewModel() {
        propertyObservableProvider = new PropertyObservableProvider(PROPERTIES);
        propertySubscriber = new PropertySubscriber(createActionMapping());
    }

    @Override
    public void dispose() {
        propertySubscriber.dispose();
    }

    @RxLogObservable
    public <T> Observable<T> observe(Property property) {
        return propertyObservableProvider.provide(property.name());
    }

    public <T> void subscribe(Property property, Observable<T> observable) {
        propertySubscriber.subscribe(property.name(), observable);
    }

    public void setQuery(String query) {
        String oldQuery = this.query;
        this.query = query;
        propertyObservableProvider.fireChange(Property.QUERY.name(), oldQuery, query);
    }

    public void setSuggestions(List<String> suggestions) {
        List<String> oldSuggestions = this.suggestions;
        this.suggestions = suggestions;
        propertyObservableProvider.fireChange(Property.SUGGESTIONS.name(), oldSuggestions, suggestions);
    }

    private Map<String, Action1<?>> createActionMapping() {
        Map<String, Action1<?>> actionMap = new HashMap<>();
        actionMap.put(
                Property.QUERY.name(),
                new Action1<String>() {
                    @Override
                    public void call(String value) {
                        setQuery(value);
                    }
                }
        );

        actionMap.put(
                Property.SUGGESTIONS.name(),
                new Action1<List<String>>() {
                    @Override
                    public void call(List<String> value) {
                        setSuggestions(value);
                    }
                }
        );
        return actionMap;
    }
}
