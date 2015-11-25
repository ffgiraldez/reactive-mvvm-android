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

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;

public class PropertySubject {

    private final PropertyObservableProvider propertyObservableProvider;
    private final PropertySubscriber propertySubscriber;

    public PropertySubject(Collection<String> propertiesHandled, Map<String, Action1<?>> propertiesActionMapping) {
        propertySubscriber = new PropertySubscriber(propertiesActionMapping);
        propertyObservableProvider = new PropertyObservableProvider(new HashSet<>(propertiesHandled));
    }

    public void dispose() {
        propertySubscriber.dispose();
    }

    public <T> void subscribe(String property, Observable<T> observable) {
        propertySubscriber.subscribe(property, observable);
    }

    public <T> Observable<T> observe(String property) {
        return propertyObservableProvider.provide(property);
    }

    public <T> void fireChange(String property, T oldValue, T newValue) {
        propertyObservableProvider.fireChange(property, oldValue, newValue);
    }
}
