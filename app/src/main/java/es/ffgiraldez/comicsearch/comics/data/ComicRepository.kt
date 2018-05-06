package es.ffgiraldez.comicsearch.comics.data

import arrow.core.None
import arrow.core.Some
import io.reactivex.Flowable

abstract class ComicRepository<T>(
        private val local: ComicLocalDataSource<T>,
        private val remote: ComicRemoteDataSource<T>
) {
    fun findByTerm(term: String): Flowable<List<T>> =
            local.findQueryByTerm(term)
                    .flatMap {
                        when (it) {
                            is None -> remote.findByTerm(term)
                                    .flatMapPublisher { local.insert(term, it).toFlowable<List<T>>() }
                            is Some -> local.findByQuery(it.t)
                        }
                    }
}