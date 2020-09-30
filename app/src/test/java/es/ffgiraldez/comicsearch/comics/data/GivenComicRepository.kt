package es.ffgiraldez.comicsearch.comics.data

import arrow.core.Option
import arrow.core.toOption
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import es.ffgiraldez.comicsearch.comic.gen.suggestionList
import es.ffgiraldez.comicsearch.comic.gen.volumeList
import es.ffgiraldez.comicsearch.comics.domain.Query
import es.ffgiraldez.comicsearch.query.search.data.SearchRepository
import es.ffgiraldez.comicsearch.query.sugestion.data.SuggestionRepository
import io.kotest.property.Arb
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream


val givenComicRepository = object : GivenComicRepository {
    override var localQuery: FlowableProcessor<Option<Query>> = BehaviorProcessor.create()
    private val localResults = BehaviorProcessor.create<Any>()


    override fun <T> localResults(): FlowableProcessor<List<T>> = localResults as FlowableProcessor<List<T>>

}

interface GivenComicRepository {
    fun <T> ComicLocalDataSource<T>.withoutValues(): ComicLocalDataSource<T> = apply {
        withValues()
    }

    fun <T> ComicLocalDataSource<T>.withSave(): ComicLocalDataSource<T> = apply {
        whenever(insert(any(), any())).thenAnswer {
            val query = it.arguments[0] as String
            val results = it.arguments[1] as List<T>
            Completable.complete().apply {
                localResults<T>().onNext(results)
                localQuery.onNext(Query(4, query).toOption())
            }
        }
    }

    fun <T> ComicLocalDataSource<T>.withValues(term: String? = null, results: List<T> = emptyList()): ComicLocalDataSource<T> = apply {
        configure()
        val q = term?.let { Query(3, term) }
        localQuery.onNext(q.toOption())
        q?.let {
            localResults<T>().onNext(results)
        }
    }

    fun <T> ComicRemoteDataSource<T>.withoutValues(): ComicRemoteDataSource<T> = apply {
        withValues()
    }

    fun <T> ComicRemoteDataSource<T>.withError(): ComicRemoteDataSource<T> = apply {
        whenever(findByTerm(any())).thenReturn(Single.error { RuntimeException("BOOM!") })
    }

    fun <T> ComicRemoteDataSource<T>.withValues(results: List<T> = emptyList()): ComicRemoteDataSource<T> = apply {
        whenever(findByTerm(any())).thenReturn(Single.just(results))
    }

    var localQuery: FlowableProcessor<Option<Query>>
    fun <T> localResults(): FlowableProcessor<List<T>>

    private fun <T> ComicLocalDataSource<T>.configure() {
        localQuery = BehaviorProcessor.create()
        whenever(findQueryByTerm(any())).thenReturn(localQuery)
        whenever(findByQuery(any())).thenReturn(localResults())
    }
    
    companion object {
        const val givenComicRepositoryMethodSource: String = "es.ffgiraldez.comicsearch.comics.data.GivenComicRepository#arguments"
        const val expectedTerm = "Batman"

        @JvmStatic
        @Suppress("unused")
        fun arguments(): Stream<Arguments> = Stream.of(
                build(mock(), mock(), Arb.suggestionList(), ::SuggestionRepository),
                build(mock(), mock(), Arb.volumeList(), ::SearchRepository)
        )

        private fun <A, B, C> build(local: A, remote: B, result: Arb<List<C>>, repo: (A, B) -> ComicRepository<C>): Arguments =
                Arguments.of(local, remote, result, repo(local, remote))
    }
}

