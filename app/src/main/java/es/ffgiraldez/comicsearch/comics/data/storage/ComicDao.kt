package es.ffgiraldez.comicsearch.comics.data.storage

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import es.ffgiraldez.comicsearch.comics.domain.Volume
import io.reactivex.Flowable

@Dao
abstract class SuggestionDao {

    @Query("SELECT * FROM queries WHERE search_term like :searchTerm")
    abstract fun findQueryByTerm(searchTerm: String): Flowable<List<QueryEntity>>

    @Query("SELECT * FROM suggestions WHERE query_id = :queryId")
    abstract fun findSuggestionByQuery(queryId: Long): Flowable<List<SuggestionEntity>>

    @Insert
    abstract fun insert(query: QueryEntity): Long

    @Insert
    abstract fun insert(vararg suggestions: SuggestionEntity)

    @Transaction
    @Insert
    fun insert(query: String, volumeTitles: List<String>) {
        val id = insert(QueryEntity(0, query))
        val suggestions = volumeTitles.map { SuggestionEntity(0, id, it) }
        insert(*suggestions.toTypedArray())

    }
}

@Dao
abstract class VolumeDao {

    @Query("SELECT * FROM search WHERE search_term like :searchTerm")
    abstract fun findQueryByTerm(searchTerm: String): Flowable<List<SearchEntity>>

    @Query("SELECT * FROM volumes WHERE search_id = :queryId")
    abstract fun findVolumeByQuery(queryId: Long): Flowable<List<VolumeEntity>>

    @Insert
    abstract fun insert(query: SearchEntity): Long

    @Insert
    abstract fun insert(vararg suggestions: VolumeEntity)

    @Transaction
    @Insert
    fun insert(query: String, volumeTitles: List<Volume>) {
        val id = insert(SearchEntity(0, query))
        val suggestions = volumeTitles.map { VolumeEntity(0, id, it.title, it.author, it.cover) }
        insert(*suggestions.toTypedArray())
    }

}