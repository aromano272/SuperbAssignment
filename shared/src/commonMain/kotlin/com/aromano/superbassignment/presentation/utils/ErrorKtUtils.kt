package com.aromano.superbassignment.presentation.utils

import com.aromano.superbassignment.domain.core.ErrorKt

val ErrorKt.message: String
    get(): String = when (this) {
        is ErrorKt.Unknown -> message ?: Strings.error_unknown
        ErrorKt.Generic -> Strings.error_unknown
        ErrorKt.Network -> Strings.error_network
        ErrorKt.NotFound -> Strings.error_not_found
        ErrorKt.Unauthorized -> Strings.error_unauthorized
    }