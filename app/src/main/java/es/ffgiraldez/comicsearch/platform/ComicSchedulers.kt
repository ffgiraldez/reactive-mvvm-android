package es.ffgiraldez.comicsearch.platform

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

object ComicSchedulers {
    val database: Scheduler = Schedulers.single()
    val network: Scheduler = Schedulers.io()
}