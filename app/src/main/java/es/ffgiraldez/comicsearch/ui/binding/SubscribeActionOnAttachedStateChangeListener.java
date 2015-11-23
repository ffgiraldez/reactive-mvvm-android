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

package es.ffgiraldez.comicsearch.ui.binding;

import android.view.View;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SubscribeActionOnAttachedStateChangeListener<T> implements View.OnAttachStateChangeListener {

    private final Observable<T> observable;
    private final Action1<T> action;
    private Subscription subscription;

    public SubscribeActionOnAttachedStateChangeListener(Observable<T> observable, Action1<T> action) {
        this.action = action;
        this.observable = observable;
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        subscription = observable.observeOn(AndroidSchedulers.mainThread()).subscribe(action);
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
