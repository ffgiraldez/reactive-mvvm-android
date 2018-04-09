package es.ffgiraldez.comicsearch.comics.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ComicVineApi {

    companion object {
        const val KEY = "75d580a0593b7320727309feb6309f62def786cd"
        const val BASE_URL = "http://www.comicvine.com"
    }

    @GET("/api/search?format=json&field_list=name&limit=20&page=1&resources=volume&api_key=$KEY")
    fun fetchSuggestedVolumes(@Query("query") query: String): Single<SuggestionResponse>

    @GET("/api/search?format=json&field_list=name,image,publisher&limit=20&page=1&resources=volume&api_key=$KEY")
    fun fetchVolumes(@Query("query") query: String): Single<VolumeResponse>
}