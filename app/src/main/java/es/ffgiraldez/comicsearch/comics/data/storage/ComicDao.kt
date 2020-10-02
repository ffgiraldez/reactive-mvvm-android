package es.ffgiraldez.comicsearch.comics.data.storage


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import es.ffgiraldez.comicsearch.comics.domain.Volume
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SuspendSuggestionDao {

    @Query("SELECT * FROM queries WHERE search_term like :searchTerm")
    abstract fun findQueryByTerm(searchTerm: String): Flow<QueryEntity?>

    @Query("SELECT * FROM suggestions WHERE query_id = :queryId")
    abstract fun findSuggestionByQuery(queryId: Long): Flow<List<SuggestionEntity>>

    @Insert
    abstract suspend fun insert(query: QueryEntity): Long

    @Insert
    abstract suspend fun insert(vararg suggestions: SuggestionEntity)

    @Transaction
    @Insert
    suspend fun insert(query: String, volumeTitles: List<String>) {
        val id = insert(QueryEntity(0, query))
        val suggestions = volumeTitles.map { SuggestionEntity(0, id, it) }
        insert(*suggestions.toTypedArray())

    }
}

@Dao
abstract class SuspendingVolumeDao {

    @Query("SELECT * FROM search WHERE search_term like :searchTerm")
    abstract fun findQueryByTerm(searchTerm: String): Flow<SearchEntity?>

    @Query("SELECT * FROM volumes WHERE search_id = :queryId")
    abstract fun findVolumeByQuery(queryId: Long): Flow<List<VolumeEntity>>

    @Insert
    abstract suspend fun insert(query: SearchEntity): Long

    @Insert
    abstract suspend fun insert(vararg suggestions: VolumeEntity)

    @Transaction
    @Insert
    suspend fun insert(query: String, volumeTitles: List<Volume>) {
        val id = insert(SearchEntity(0, query))
        val suggestions = volumeTitles.map { VolumeEntity(0, id, it.title, it.author, it.cover) }
        insert(*suggestions.toTypedArray())
    }

}