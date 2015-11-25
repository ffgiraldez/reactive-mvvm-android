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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.BooleanSubscription;

public class PropertyChangeListenerOnSubscribe<T> implements Observable.OnSubscribe<T> {
    private final PropertyChangeSupport propertyChangeSupport;
    private final String property;

    public PropertyChangeListenerOnSubscribe(PropertyChangeSupport propertyChangeSupport, String property) {
        this.propertyChangeSupport = propertyChangeSupport;
        this.property = property;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void call(final Subscriber<? super T> subscriber) {
        final PropertyChangeListener changeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (!subscriber.isUnsubscribed() && property.equals(event.getPropertyName())) {
                    subscriber.onNext((T) event.getNewValue());
                }
            }
        };

        propertyChangeSupport.addPropertyChangeListener(changeListener);
        subscriber.add(BooleanSubscription.create(new Action0() {
            @Override
            public void call() {
                propertyChangeSupport.removePropertyChangeListener(changeListener);
            }
        }));
    }
}
