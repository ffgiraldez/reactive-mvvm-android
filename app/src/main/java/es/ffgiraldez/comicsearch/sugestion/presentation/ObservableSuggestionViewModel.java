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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import es.ffgiraldez.comicsearch.ui.binding.ObservableViewModel;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public abstract class ObservableSuggestionViewModel extends AbstractSuggestionViewModel implements ObservableViewModel {

    public enum Property {QUERY, SUGGESTIONS}

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private final CompositeSubscription disposeSubscription = new CompositeSubscription();

    @Override
    public void dispose() {
        disposeSubscription.unsubscribe();
    }

    @RxLogObservable
    @SuppressWarnings("unchecked")
    public <T> Observable<T> observe(Property property) {
        if (Property.QUERY.equals(property)) {
            return (Observable<T>) Observable.create(new ObservableSuggestionViewModelQueryOnSubscribe(this));
        }
        if (Property.SUGGESTIONS.equals(property)) {
            return (Observable<T>) Observable.create(new ObservableSuggestionViewModelSuggestionsOnSubscribe(this));
        }
        return Observable.empty();
    }

    @SuppressWarnings("unchecked")
    public void subscribe(Property property, Observable<? extends Object> observable) {
        if (Property.SUGGESTIONS.equals(property)) {
            subscribe(observable, new Action1<Object>() {
                @Override
                public void call(Object o) {
                    setSuggestions((List<String>) o);
                }
            });
        }

        if (Property.QUERY.equals(property)) {
            subscribe(observable, new Action1<Object>() {
                @Override
                public void call(Object o) {
                    setQuery((String) o);
                }
            });
        }
    }

    public void setQuery(String query) {
        String oldQuery = this.query;
        this.query = query;
        pcs.firePropertyChange(Property.QUERY.name(), oldQuery, query);
    }

    public void setSuggestions(List<String> suggestions) {
        List<String> oldSuggestions = this.suggestions;
        this.suggestions = suggestions;
        pcs.firePropertyChange(Property.SUGGESTIONS.name(), oldSuggestions, suggestions);
    }

    void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    private void subscribe(Observable<?> observable, final Action1<Object> action) {
        final CompositeSubscription compositeSubscription = new CompositeSubscription();
        Observer<Object> observer = new Observer<Object>() {
            @Override
            public void onCompleted() {
                compositeSubscription.unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                compositeSubscription.unsubscribe();
            }

            @Override
            public void onNext(Object o) {
                action.call(o);
            }
        };
        Subscription subscription = observable.subscribe(observer);
        compositeSubscription.add(subscription);
        disposeSubscription.add(compositeSubscription);
    }
}
