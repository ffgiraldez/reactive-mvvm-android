package es.ffgiraldez.comicsearch.platform

import arrow.core.Either
import arrow.core.left
import arrow.core.right

fun <A, B, C> safe(first: A?, second: B?, block: (A, B) -> C): C? {
    return if (first != null && second != null) {
        block(first, second)
    } else {
        null
    }
}

fun <A, B> left(a: A): Either<A, B> = a.left()
fun <A, B> right(b: B): Either<A, B> = b.right()