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

package es.ffgiraldez.comicsearch.data.api.model;

import com.google.gson.annotations.SerializedName;

public class ApiVolume {

    @SerializedName("name")
    String name;

    @SerializedName("publisher")
    ApiPublisher publisher;

    @SerializedName("image")
    ApiImage image;

    public String getName() {
        return name;
    }

    public String getPublisher() {
        return publisher != null ? publisher.getName() : "";
    }

    public String getImage() {
        return image != null ? image.getUrl() : "";
    }
}
