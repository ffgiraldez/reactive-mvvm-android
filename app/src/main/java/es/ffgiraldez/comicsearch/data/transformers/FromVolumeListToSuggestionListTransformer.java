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

package es.ffgiraldez.comicsearch.data.transformers;

import java.util.List;

import es.ffgiraldez.comicsearch.model.Volume;
import rx.Observable;
import rx.functions.Func1;

public class FromVolumeListToSuggestionListTransformer implements Observable.Transformer<List<Volume>, List<String>> {
    @Override
    public Observable<List<String>> call(Observable<List<Volume>> listObservable) {
        return listObservable
                .flatMap(new Func1<List<Volume>, Observable<Volume>>() {
                    @Override
                    public Observable<Volume> call(List<Volume> volumes) {
                        return Observable.from(volumes);
                    }
                })
                .distinctUntilChanged()
                .map(new Func1<Volume, String>() {
                    @Override
                    public String call(Volume volume) {
                        return volume.getTitle();
                    }
                })
                .toList();
    }
}
