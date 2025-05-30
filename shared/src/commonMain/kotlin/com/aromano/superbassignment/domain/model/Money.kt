package com.aromano.superbassignment.domain.model

import kotlin.jvm.JvmInline

// NOTE(aromano): Using cents rather than floats to avoid precision issues when dealing with money,
// this should be properly enhanced to support multiple currencies rather than just handling cents

@JvmInline
value class Cents(val value: Int)