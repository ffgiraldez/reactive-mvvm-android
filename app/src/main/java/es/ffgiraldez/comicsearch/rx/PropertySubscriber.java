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

package es.ffgiraldez.comicsearch.rx;

import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class PropertySubscriber {

    private final CompositeSubscription disposeSubscription;
    private final Map<String, Action1<?>> actionMap;

    public PropertySubscriber(Map<String, Action1<?>> actionMap) {
        this.actionMap = actionMap;
        this.disposeSubscription = new CompositeSubscription();
    }

    @SuppressWarnings("unchecked")
    public <T> void subscribe(String property, Observable<T> observable) {
        Action1<T> action = (Action1<T>) actionMap.get(property);
        if (action != null) {
            subscribe(observable, action);
        }
    }

    public void dispose() {
        disposeSubscription.unsubscribe();
    }

    private <T> void subscribe(final Observable<T> observable, final Action1<T> action) {
        final CompositeSubscription compositeSubscription = new CompositeSubscription();
        final Observer<T> observer = new Observer<T>() {
            @Override
            public void onCompleted() {
                compositeSubscription.unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                compositeSubscription.unsubscribe();
            }

            @Override
            public void onNext(T value) {
                action.call(value);
            }
        };
        Subscription subscription = observable.subscribe(observer);
        compositeSubscription.add(subscription);
        disposeSubscription.add(compositeSubscription);
    }
}
