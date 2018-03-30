package es.ffgiraldez.comicsearch.sugestion.data

import com.google.gson.annotations.SerializedName


data class VolumeResponse(
        @SerializedName("status_code") var statusCode: Int = 0,
        @SerializedName("error") var error: String?,
        @SerializedName("results") var results: List<ApiVolume>
)

data class SuggestionResponse(
        @SerializedName("status_code") var statusCode: Int = 0,
        @SerializedName("error") var error: String?,
        @SerializedName("results") var results: List<SuggestionVolume>
)

data class SuggestionVolume(
        @SerializedName("name") var name: String
)

data class ApiVolume(
        @SerializedName("name") var name: String?,
        @SerializedName("publisher") var apiPublisher: ApiPublisher,
        @SerializedName("image") var apiImage: ApiImage
)

data class ApiImage(
        @SerializedName("thumb_url") val url: String?
)

data class ApiPublisher(
        @SerializedName("name") val name: String?
)
