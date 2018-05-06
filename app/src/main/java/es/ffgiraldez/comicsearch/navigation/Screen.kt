package es.ffgiraldez.comicsearch.navigation

import es.ffgiraldez.comicsearch.comics.domain.Volume

sealed class Screen {
    data class Detail(val volume: Volume) : Screen()
}