package es.ffgiraldez.comicsearch.comics.domain

data class Volume(
        val title: String,
        val author: String,
        val cover: String
)

data class Query(
        val identifier: Long,
        val searchTerm: String
)