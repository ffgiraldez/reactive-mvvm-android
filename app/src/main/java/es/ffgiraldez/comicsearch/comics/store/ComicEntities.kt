package es.ffgiraldez.comicsearch.comics.store

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity(
        tableName = "queries",
        indices = [
            Index(
                    value = ["query_identifier"],
                    name = "idx_query_identifier"
            ),
            Index(
                    value = ["search_term"],
                    name = "idx_query_term"
            )
        ]
)
data class QueryEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "query_identifier")
        var queryId: Long,
        @ColumnInfo(name = "search_term")
        var searchTerm: String
)

@Entity(
        tableName = "suggestions",
        indices = [
            Index(
                    value = ["query_id"],
                    name = "idx_suggestion_id"
            )
        ],
        foreignKeys = [
            ForeignKey(
                    entity = QueryEntity::class,
                    parentColumns = ["query_identifier"],
                    childColumns = ["query_id"],
                    onDelete = CASCADE
            )
        ]
)
data class SuggestionEntity(
        @PrimaryKey(autoGenerate = true)
        var suggestionId: Long,
        @ColumnInfo(name = "query_id")
        var queryId: Long,
        @ColumnInfo(name = "title")
        var title: String
)

@Entity(
        tableName = "search",
        indices = [
            Index(
                    value = ["search_identifier"],
                    name = "idx_search_identifier"
            ),
            Index(
                    value = ["search_term"],
                    name = "idx_search_term"
            )
        ]
)
data class SearchEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "search_identifier")
        var queryId: Long,
        @ColumnInfo(name = "search_term")
        var searchTerm: String
)
@Entity(
        tableName = "volumes",
        indices = [
            Index(
                    value = ["search_id"],
                    name = "idx_search_id"
            )
        ],
        foreignKeys = [
            ForeignKey(
                    entity = SearchEntity::class,
                    parentColumns = ["search_identifier"],
                    childColumns = ["search_id"],
                    onDelete = CASCADE
            )
        ]
)
data class VolumeEntity(
        @PrimaryKey(autoGenerate = true)
        var suggestionId: Long,
        @ColumnInfo(name = "search_id")
        var queryId: Long,
        @ColumnInfo(name = "title")
        var title: String,
        @ColumnInfo(name = "author")
        var author: String,
        @ColumnInfo(name = "url")
        var url: String
)