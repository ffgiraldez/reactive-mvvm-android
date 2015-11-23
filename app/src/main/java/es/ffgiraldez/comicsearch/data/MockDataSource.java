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

package es.ffgiraldez.comicsearch.data;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import es.ffgiraldez.comicsearch.model.Volume;
import rx.Observable;

public class MockDataSource implements ComicDataSource {

    @Override
    public Observable<List<Volume>> fetchSuggestedVolumes(String query) {
        return mocks();
    }

    @Override
    public Observable<List<Volume>> fetchVolumes(String query) {
        return mocks();
    }

    @NonNull
    private Observable<List<Volume>> mocks() {
        return Observable.just(Arrays.asList(
                new Volume(
                        "Title",
                        "Author",
                        "https://s-media-cache-ak0.pinimg.com/originals/e6/3c/ff/e63cff79067fcfc29610266b10796eb8.jpg"),
                new Volume(
                        "Title 2",
                        "Author 2",
                        "https://s-media-cache-ak0.pinimg.com/originals/e6/3c/ff/e63cff79067fcfc29610266b10796eb8.jpg")
        ));
    }
}
