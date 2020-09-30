package es.ffgiraldez.comicsearch.platform

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.left
import arrow.core.right
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

typealias Option<A> = Either<Unit, A>

fun <A, B> left(a: A): Either<A, B> = a.left()
fun <A, B> right(b: B): Either<A, B> = b.right()
fun <T> T?.toOption(): Option<T> = this?.let { Right(it) } ?: Left(Unit)

operator fun CompositeDisposable.plus(disposable: Disposable): CompositeDisposable = apply {
    add(disposable)
}