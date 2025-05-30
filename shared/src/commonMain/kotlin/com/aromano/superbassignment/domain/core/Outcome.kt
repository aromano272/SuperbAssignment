package com.aromano.superbassignment.domain.core

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

typealias ErrorType = ErrorKt

sealed interface Outcome<out R> {
    data class Success<out R>(val data: R) : Outcome<R>
    data class Failure(val error: ErrorType) : Outcome<Nothing>
}


// Extensions
val <T> Outcome<T>.asData: T?
    get() = when (this) {
        is Outcome.Success -> data
        is Outcome.Failure -> null
    }

val Outcome<*>.asError: ErrorType?
    get() = when (this) {
        is Outcome.Success -> null
        is Outcome.Failure -> error
    }

fun <T> Outcome<T>.doOnSuccess(block: (T) -> Unit): Outcome<T> = when (this) {
    is Outcome.Success -> this.also { block(data) }
    is Outcome.Failure -> this
}

fun <T> Outcome<T>.doOnFailure(block: (ErrorType) -> Unit): Outcome<T> = when (this) {
    is Outcome.Success -> this
    is Outcome.Failure -> this.also { block(error) }
}

suspend fun <T, R> Outcome<T>.mapSuccess(mapper: suspend (T) -> R): Outcome<R> = when (this) {
    is Outcome.Success -> Outcome.Success(mapper(data))
    is Outcome.Failure -> this
}

suspend fun <T> Outcome<T>.mapFailure(mapper: suspend (ErrorType) -> ErrorType): Outcome<T> =
    when (this) {
        is Outcome.Success -> this
        is Outcome.Failure -> Outcome.Failure(mapper(error))
    }

suspend fun <T, R> Outcome<T>.flatMap(
    onSuccess: suspend (T) -> Outcome<R>,
    onFailure: suspend (ErrorType) -> Outcome<R>,
): Outcome<R> = when (this) {
    is Outcome.Success -> onSuccess(data)
    is Outcome.Failure -> onFailure(error)
}

suspend fun <T, R> Outcome<T>.flatMapSuccess(mapper: suspend (T) -> Outcome<R>): Outcome<R> =
    when (this) {
        is Outcome.Success -> mapper(data)
        is Outcome.Failure -> this
    }

suspend fun <T> Outcome<T>.flatMapFailure(mapper: suspend (ErrorType) -> Outcome<T>): Outcome<T> =
    when (this) {
        is Outcome.Success -> this
        is Outcome.Failure -> mapper(error)
    }

suspend fun Outcome<*>.ignoreData(): Outcome<Unit> = mapSuccess { }

suspend fun <T1, T2> zipOutcomesTogether(
    t1func: suspend () -> Outcome<T1>,
    t2func: suspend () -> Outcome<T2>,
): Outcome<Pair<T1, T2>> = zipOutcomes(t1func, t2func) { t1, t2 ->
    t1 to t2
}

suspend fun <T1, T2, R> zipOutcomes(
    t1func: suspend () -> Outcome<T1>,
    t2func: suspend () -> Outcome<T2>,
    mapper: (T1, T2) -> R,
): Outcome<R> = coroutineScope {
    val t1 = async { t1func() }
    val t2 = async { t2func() }

    val t1Result = t1.await()
    val t2Result = t2.await()

    if (t1Result is Outcome.Success && t2Result is Outcome.Success) {
        Outcome.Success(mapper(t1Result.data, t2Result.data))
    } else {
        val firstError = (t1Result as? Outcome.Failure) ?: (t2Result as? Outcome.Failure)!!
        firstError
    }
}

suspend fun <T1, T2, T3> zipOutcomesTogether(
    t1func: suspend () -> Outcome<T1>,
    t2func: suspend () -> Outcome<T2>,
    t3func: suspend () -> Outcome<T3>,
): Outcome<Triple<T1, T2, T3>> = zipOutcomes(t1func, t2func, t3func) { t1, t2, t3 ->
    Triple(t1, t2, t3)
}

suspend fun <T1, T2, T3, R> zipOutcomes(
    t1func: suspend () -> Outcome<T1>,
    t2func: suspend () -> Outcome<T2>,
    t3func: suspend () -> Outcome<T3>,
    mapper: (T1, T2, T3) -> R,
): Outcome<R> = coroutineScope {
    val t1 = async { t1func() }
    val t2 = async { t2func() }
    val t3 = async { t3func() }

    val t1Result = t1.await()
    val t2Result = t2.await()
    val t3Result = t3.await()

    if (t1Result is Outcome.Success && t2Result is Outcome.Success && t3Result is Outcome.Success) {
        Outcome.Success(mapper(t1Result.data, t2Result.data, t3Result.data))
    } else {
        val firstError = (t1Result as? Outcome.Failure) ?: (t2Result as? Outcome.Failure)
        ?: (t3Result as? Outcome.Failure)!!
        firstError
    }
}
