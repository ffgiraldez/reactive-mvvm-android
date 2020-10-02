package es.ffgiraldez.comicsearch.comics.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ComicVineApi {

    companion object {
        const val KEY = "d800216c205879548fdc491e0a260ff402633c00"
        const val BASE_URL = "https://www.comicvine.com"
    }

    @GET("/api/search?format=json&field_list=name&limit=20&page=1&resources=volume&api_key=$KEY")
    fun fetchSuggestedVolumes(@Query("query") query: String): Single<SuggestionResponse>

    @GET("/api/search?format=json&field_list=name,image,publisher&limit=20&page=1&resources=volume&api_key=$KEY")
    fun fetchVolumes(@Query("query") query: String): Single<VolumeResponse>
}

interface SuspendComicVineApi {

    companion object {
        const val KEY = "d800216c205879548fdc491e0a260ff402633c00"
        const val BASE_URL = "https://www.comicvine.com"
    }

    @GET("/api/search?format=json&field_list=name&limit=20&page=1&resources=volume&api_key=$KEY")
    suspend fun fetchSuggestedVolumes(@Query("query") query: String): SuggestionResponse

    @GET("/api/search?format=json&field_list=name,image,publisher&limit=20&page=1&resources=volume&api_key=$KEY")
    suspend fun fetchVolumes(@Query("query") query: String): VolumeResponse
}