package com.aromano.superbassignment.domain.core

sealed interface ErrorKt {
    data class Unknown(val message: String?) : ErrorKt {
        constructor(ex: Throwable) : this(ex.message)
    }

    data object Unauthorized : ErrorKt
    data object Generic : ErrorKt
    data object NotFound : ErrorKt
    data object Network : ErrorKt

}