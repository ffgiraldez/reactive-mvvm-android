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

package es.ffgiraldez.comicsearch.data.api;

import java.util.ArrayList;
import java.util.List;

import es.ffgiraldez.comicsearch.data.api.model.ApiVolume;
import es.ffgiraldez.comicsearch.model.Volume;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class FromVolumeResponseToVolumeList implements Observable.Transformer<VolumeResponse, List<Volume>> {

    private final boolean allowDistinct;

    public FromVolumeResponseToVolumeList(boolean allowDistinct) {
        this.allowDistinct = allowDistinct;
    }

    @Override
    public Observable<List<Volume>> call(Observable<VolumeResponse> apiObservable) {
        return apiObservable
                .flatMap(new Func1<VolumeResponse, Observable<List<ApiVolume>>>() {
                    @Override
                    public Observable<List<ApiVolume>> call(VolumeResponse comicVineResponse) {
                        return Observable.just(comicVineResponse.getResults());
                    }
                }).flatMap(new Func1<List<ApiVolume>, Observable<ApiVolume>>() {
                    @Override
                    public Observable<ApiVolume> call(List<ApiVolume> apiVolumes) {
                        return Observable.from(apiVolumes);
                    }
                }).map(new Func1<ApiVolume, Volume>() {
                    @Override
                    public Volume call(ApiVolume apiVolume) {
                        return new Volume(apiVolume.getName(), apiVolume.getPublisher(), apiVolume.getImage());
                    }
                }).reduce(new ArrayList<Volume>(), new Func2<List<Volume>, Volume, List<Volume>>() {
                    @Override
                    public List<Volume> call(List<Volume> volumes, Volume volume) {
                        if (allowDistinct || !volumes.contains(volume)) {
                            volumes.add(volume);
                        }
                        return volumes;
                    }
                });
    }
}
