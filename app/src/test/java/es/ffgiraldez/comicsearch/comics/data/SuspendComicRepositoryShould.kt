package es.ffgiraldez.comicsearch.comics.data

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import es.ffgiraldez.comicsearch.comics.data.SuspendGivenComicRepository.Companion.expectedTerm
import es.ffgiraldez.comicsearch.comics.data.SuspendGivenComicRepository.Companion.givenSuspendComicRepositoryMethodSource
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.comics.domain.ComicError.EmptyResultsError
import es.ffgiraldez.comicsearch.comics.domain.ComicError.NetworkError
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class SuspendComicRepositoryShould
    : SuspendGivenComicRepository by givenSuspendComicRepository {

    @ParameterizedTest
    @MethodSource(givenSuspendComicRepositoryMethodSource)
    fun <T> `return EmptyResultsError when local query does not have results`(
            localDataSource: SuspendComicLocalDataSource<T>,
            remoteDataSource: SuspendComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: SuspendComicRepository<T>
    ) = runBlocking {
        localDataSource.withValues(term = expectedTerm, results = emptyList())

        repository.findByTerm(expectedTerm).test {
            assertEquals(expectItem(), ComicError.emptyResult().left())
        }
    }

    @ParameterizedTest
    @MethodSource(givenSuspendComicRepositoryMethodSource)
    fun <T> `return results list when local query does have results`(
            localDataSource: SuspendComicLocalDataSource<T>,
            remoteDataSource: SuspendComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: SuspendComicRepository<T>
    ) = runBlocking {
        val expectedList = generator.next()
        localDataSource.withValues(term = expectedTerm, results = expectedList)

        repository.findByTerm(expectedTerm).test {
            assertEquals(expectItem(), expectedList.right())
        }
    }

    @ParameterizedTest
    @MethodSource(givenSuspendComicRepositoryMethodSource)
    fun <T> `ask for remote result when does not have local query`(
            localDataSource: SuspendComicLocalDataSource<T>,
            remoteDataSource: SuspendComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: SuspendComicRepository<T>
    ) = runBlocking {
        localDataSource.withoutValues()
        remoteDataSource.withoutValues()

        repository.findByTerm(expectedTerm).test { expectItem() }

        verify(remoteDataSource).findByTerm(any())
    }

    @ParameterizedTest
    @MethodSource(givenSuspendComicRepositoryMethodSource)
    fun <T> `safe remote result on local when does not have local query`(
            localDataSource: SuspendComicLocalDataSource<T>,
            remoteDataSource: SuspendComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: SuspendComicRepository<T>
    ) = runBlocking {
        val expected = generator.next()
        localDataSource.withoutValues()
        remoteDataSource.withValues(results = expected)

        repository.findByTerm(expectedTerm).test { }

        verify(localDataSource).insert(expectedTerm, expected)
    }

    @ParameterizedTest
    @MethodSource(givenSuspendComicRepositoryMethodSource)
    fun <T> `return EmptyResultError when local and remote does not have results`(
            localDataSource: SuspendComicLocalDataSource<T>,
            remoteDataSource: SuspendComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: SuspendComicRepository<T>
    ) = runBlocking {
        localDataSource
                .withoutValues()
                .withSave()
        remoteDataSource.withoutValues()

        repository.findByTerm(expectedTerm).test {
            assertEquals(expectItem(), EmptyResultsError.left())
        }
    }

    @ParameterizedTest
    @MethodSource(givenSuspendComicRepositoryMethodSource)
    fun <T> `return NetworkError when local does not have results and remote fails`(
            localDataSource: SuspendComicLocalDataSource<T>,
            remoteDataSource: SuspendComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: SuspendComicRepository<T>
    ) = runBlocking {
        localDataSource.withoutValues()
        remoteDataSource.withError()

        repository.findByTerm(expectedTerm).test {
            assertEquals(expectItem(), NetworkError.left())
        }
    }

    @ParameterizedTest
    @MethodSource(givenSuspendComicRepositoryMethodSource)
    fun <T> `return results list when local does not have results but remote have results`(
            localDataSource: SuspendComicLocalDataSource<T>,
            remoteDataSource: SuspendComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: SuspendComicRepository<T>
    ) = runBlocking {
        val expected = generator.next()
        localDataSource.withoutValues()
                .withSave()
        remoteDataSource.withValues(results = expected)

        repository.findByTerm(expectedTerm).test {
            assertEquals(expectItem(), expected.right())
        }
    }
}