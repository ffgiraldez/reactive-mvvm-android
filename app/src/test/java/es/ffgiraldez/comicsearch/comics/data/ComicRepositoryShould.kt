package es.ffgiraldez.comicsearch.comics.data

import arrow.core.left
import arrow.core.right
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import es.ffgiraldez.comicsearch.comics.data.GivenComicRepository.Companion.expectedTerm
import es.ffgiraldez.comicsearch.comics.data.GivenComicRepository.Companion.givenComicRepositoryMethodSource
import es.ffgiraldez.comicsearch.comics.domain.ComicError
import es.ffgiraldez.comicsearch.comics.domain.ComicError.EmptyResultsError
import es.ffgiraldez.comicsearch.comics.domain.ComicError.NetworkError
import es.ffgiraldez.comicsearch.platform.left
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class ComicRepositoryShould
    : GivenComicRepository by givenComicRepository {

    @ParameterizedTest
    @MethodSource(givenComicRepositoryMethodSource)
    fun <T> `return EmptyResultsError when local query does not have results`(
            localDataSource: ComicLocalDataSource<T>,
            remoteDataSource: ComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: ComicRepository<T>
    ) {
        localDataSource.withValues(term = expectedTerm, results = emptyList())

        val observer = repository.findByTerm(expectedTerm).test()

        observer.assertValue(EmptyResultsError.left())
    }

    @ParameterizedTest
    @MethodSource(givenComicRepositoryMethodSource)
    fun <T> `return results list when local query does have results`(
            localDataSource: ComicLocalDataSource<T>,
            remoteDataSource: ComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: ComicRepository<T>
    ) {
        val expectedList = generator.next()
        localDataSource.withValues(term = expectedTerm, results = expectedList)

        val observer = repository.findByTerm(expectedTerm).test()

        observer.assertValue(expectedList.right())
    }

    @ParameterizedTest
    @MethodSource(givenComicRepositoryMethodSource)
    fun <T> `ask for remote result when does not have local query`(
            localDataSource: ComicLocalDataSource<T>,
            remoteDataSource: ComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: ComicRepository<T>
    ) {
        localDataSource.withoutValues()
        remoteDataSource.withoutValues()

        repository.findByTerm(expectedTerm).test()

        verify(remoteDataSource).findByTerm(any())

    }

    @ParameterizedTest
    @MethodSource(givenComicRepositoryMethodSource)
    fun <T> `safe remote result on local when does not have local query`(
            localDataSource: ComicLocalDataSource<T>,
            remoteDataSource: ComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: ComicRepository<T>
    ) {
        val expected = generator.next()
        localDataSource.withoutValues()
        remoteDataSource.withValues(results = expected)

        repository.findByTerm(expectedTerm).test()

        verify(localDataSource).insert(expectedTerm, expected)
    }

    @ParameterizedTest
    @MethodSource(givenComicRepositoryMethodSource)
    fun <T> `return EmptyResultError when local and remote does not have results`(
            localDataSource: ComicLocalDataSource<T>,
            remoteDataSource: ComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: ComicRepository<T>
    ) {
        localDataSource
                .withoutValues()
                .withSave()
        remoteDataSource.withoutValues()

        val observer = repository.findByTerm(expectedTerm).test()

        observer.assertValue(EmptyResultsError.left())
    }

    @ParameterizedTest
    @MethodSource(givenComicRepositoryMethodSource)
    fun <T> `return NetworkError when local does not have results and remote fails`(
            localDataSource: ComicLocalDataSource<T>,
            remoteDataSource: ComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: ComicRepository<T>
    ) {
        localDataSource.withoutValues()
        remoteDataSource.withError()

        val observer = repository.findByTerm(expectedTerm).test()

        observer.assertValue(NetworkError.left())
    }

    @ParameterizedTest
    @MethodSource(givenComicRepositoryMethodSource)
    fun <T> `return results list when local does not have results but remote have results`(
            localDataSource: ComicLocalDataSource<T>,
            remoteDataSource: ComicRemoteDataSource<T>,
            generator: Arb<List<T>>,
            repository: ComicRepository<T>
    ) {
        val expected = generator.next()
        localDataSource.withoutValues()
                .withSave()
        remoteDataSource.withValues(results = expected)

        val observer = repository.findByTerm(expectedTerm).test()

        observer.assertValue(expected.right())
    }
}


