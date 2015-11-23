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

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface ComicVineApi {

    String KEY = "75d580a0593b7320727309feb6309f62def786cd";
    String BASE_URL = "http://www.comicvine.com";

    @GET("/api/search?format=json&field_list=name&limit=10&page=1&resources=volume&api_key=" + KEY)
    Observable<VolumeResponse> fetchSuggestedVolumes(@Query("query") String query);

    @GET("/api/search?format=json&field_list=name,image,publisher&limit=10&page=1&resources=volume&api_key=" + KEY)
    Observable<VolumeResponse> fetchVolumes(@Query("query") String query);
}