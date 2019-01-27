package es.ffgiraldez.comicsearch.platform

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun <A, B> left(a: A): Either<A, B> = a.left()
fun <A, B> right(b: B): Either<A, B> = b.right()

operator fun CompositeDisposable.plus(disposable: Disposable): CompositeDisposable = apply {
    add(disposable)
}