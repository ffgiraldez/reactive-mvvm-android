package es.ffgiraldez.comicsearch.comics.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
        entities = [
            QueryEntity::class,
            SuggestionEntity::class,
            SearchEntity::class,
            VolumeEntity::class
        ],
        version = 1
)
abstract class ComicDatabase : RoomDatabase() {
    abstract fun suggestionDao(): SuggestionDao

    abstract fun suspendingSuggestionDao(): SuspendSuggestionDao

    abstract fun volumeDao(): VolumeDao

    abstract fun suspendingVolumeDao(): SuspendingVolumeDao
}