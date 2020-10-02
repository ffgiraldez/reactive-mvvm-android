package es.ffgiraldez.comicsearch.comics.data

import com.nhaarman.mockitokotlin2.*
import es.ffgiraldez.comicsearch.comics.domain.Query
import es.ffgiraldez.comicsearch.comics.gen.suggestionList
import es.ffgiraldez.comicsearch.comics.gen.volumeList
import es.ffgiraldez.comicsearch.query.search.data.SuspendSearchRepository
import es.ffgiraldez.comicsearch.query.sugestion.data.SuspendSuggestionRepository
import io.kotest.property.Arb
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

val givenSuspendComicRepository = object : SuspendGivenComicRepository {
    override var localQuery: BroadcastChannel<Query?> = ConflatedBroadcastChannel()
    private val localResults = ConflatedBroadcastChannel<Any>()

    override fun <T> localResults(): BroadcastChannel<List<T>> = localResults as BroadcastChannel<List<T>>

}

interface SuspendGivenComicRepository {
    fun <T> SuspendComicLocalDataSource<T>.withoutValues(): SuspendComicLocalDataSource<T> = apply {
        withValues()
    }

    fun <T> SuspendComicLocalDataSource<T>.withSave(): SuspendComicLocalDataSource<T> = apply {
        given { runBlocking { insert(any(), any()) } } willAnswer {
            val query = it.arguments[0] as String
            val results = it.arguments[1] as List<T>
            runBlocking {
                localResults<T>().send(results)
                localQuery.send(Query(4, query))
            }
        }
    }

    fun <T> SuspendComicLocalDataSource<T>.withValues(term: String? = null, results: List<T> = emptyList()): SuspendComicLocalDataSource<T> = apply {
        configure()
        val q = term?.let { Query(3, term) }
        runBlocking {
            localQuery.send(q)
            q?.let { localResults<T>().send(results) }
        }
    }

    fun <T> SuspendComicRemoteDataSource<T>.withoutValues(): SuspendComicRemoteDataSource<T> = apply {
        withValues()
    }

    fun <T> SuspendComicRemoteDataSource<T>.withError(): SuspendComicRemoteDataSource<T> = apply {
        given { runBlocking { findByTerm(any()) } } willThrow { RuntimeException("BOOM!") }

    }

    fun <T> SuspendComicRemoteDataSource<T>.withValues(results: List<T> = emptyList()): SuspendComicRemoteDataSource<T> = apply {
        given { runBlocking { findByTerm(any()) } } willReturn { results }
    }

    var localQuery: BroadcastChannel<Query?>
    fun <T> localResults(): BroadcastChannel<List<T>>

    private fun <T> SuspendComicLocalDataSource<T>.configure() {
        localQuery = ConflatedBroadcastChannel()
        given { findQueryByTerm(any()) } willAnswer { localQuery.asFlow() }
        given { findByQuery(any()) } willReturn { localResults<T>().asFlow() }
    }

    companion object {
        const val givenSuspendComicRepositoryMethodSource: String = "es.ffgiraldez.comicsearch.comics.data.SuspendGivenComicRepository#arguments"
        const val expectedTerm = "Batman"

        @JvmStatic
        @Suppress("unused")
        fun arguments(): Stream<Arguments> = Stream.of(
                build(mock(), mock(), Arb.suggestionList(), ::SuspendSuggestionRepository),
                build(mock(), mock(), Arb.volumeList(), ::SuspendSearchRepository)
        )

        private fun <A, B, C> build(local: A, remote: B, result: Arb<List<C>>, repo: (A, B) -> SuspendComicRepository<C>): Arguments =
                Arguments.of(local, remote, result, repo(local, remote))
    }
}

