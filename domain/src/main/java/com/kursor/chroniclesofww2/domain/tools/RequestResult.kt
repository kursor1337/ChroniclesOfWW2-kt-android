package com.kursor.chroniclesofww2.domain.tools

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ConnectException

@JvmInline
value class RequestResult<T> internal constructor(
    inline val value: Any?
) {

    val isConnectionFailure: Boolean get() = value is ConnectionFailure

    val isUnauthorized: Boolean get() = value is Unauthorized

    internal class Unauthorized {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return 0
        }
    }

    internal class ConnectionFailure {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return 1
        }
    }

    inline fun onSuccess(action: (T) -> Unit): RequestResult<T> {
        if (!isUnauthorized && !isConnectionFailure) action(value as T)
        return this
    }

    inline fun onUnauthorized(action: () -> Unit): RequestResult<T> {
        if (isUnauthorized) action()
        return this
    }

    inline fun onConnectionFailure(action: () -> Unit): RequestResult<T> {
        if (isConnectionFailure) action()
        return this
    }


}

suspend fun <T> tryRequest(block: suspend () -> T): RequestResult<T> = withContext(Dispatchers.IO) {
    var requestResult: RequestResult<T>? = null
    runCatching {
        block()
    }.onSuccess {
        requestResult = RequestResult(it)
    }.onFailure {
        requestResult = when (it) {
            is ConnectException -> RequestResult(RequestResult.ConnectionFailure())
            is UnauthorizedException -> RequestResult(RequestResult.Unauthorized())
            else -> throw it
        }
    }
    requestResult!!
}