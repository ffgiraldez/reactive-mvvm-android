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

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.List;

import es.ffgiraldez.comicsearch.data.ComicDataSource;
import es.ffgiraldez.comicsearch.model.Volume;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;

public class RestDataSource implements ComicDataSource {
    private final ComicVineApi endpoint;

    public RestDataSource() {
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(httpLoggingInterceptor);
        endpoint = new Retrofit.Builder()
                .baseUrl(ComicVineApi.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(ComicVineApi.class);
    }

    @Override
    public Observable<List<Volume>> fetchSuggestedVolumes(String query) {
        return endpoint.fetchSuggestedVolumes(query)
                .compose(new FromVolumeResponseToVolumeList(false));
    }

    @Override
    public Observable<List<Volume>> fetchVolumes(String query) {
        return endpoint.fetchVolumes(query)
                .compose(new FromVolumeResponseToVolumeList(true));
    }
}
