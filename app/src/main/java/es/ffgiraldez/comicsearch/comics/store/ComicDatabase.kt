package es.ffgiraldez.comicsearch.comics.store

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

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

    abstract fun volumeDao(): VolumeDao
}