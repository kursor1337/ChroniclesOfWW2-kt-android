package com.kursor.chroniclesofww2.domain.tools

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@JvmInline
public value class RequestResult<T> internal constructor(
    inline val value: Any?
) {

    public val isConnectionFailure: Boolean get() = value is ConnectionFailure

    public val isUnauthorized: Boolean get() = value is Unauthorized

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

    public inline fun onSuccess(action: (T) -> Unit): RequestResult<T> {
        if (!isUnauthorized && !isConnectionFailure) action(value as T)
        return this
    }

    public inline fun onUnauthorized(action: () -> Unit): RequestResult<T> {
        if (isUnauthorized) action()
        return this
    }

    public inline fun onConnectionFailure(action: () -> Unit): RequestResult<T> {
        if (isConnectionFailure) action()
        return this
    }


}

suspend fun <T> tryRequest(block: suspend () -> T): RequestResult<T> {
    var requestResult: RequestResult<T>? = null
    withContext(Dispatchers.IO) {
        runCatching(block)
            .onSuccess {
                requestResult = RequestResult<T>(it)
            }.onFailure {
                requestResult = when (it) {
                    is ConnectionException -> RequestResult<T>(RequestResult.ConnectionFailure())
                    is UnauthorizedException -> RequestResult<T>(RequestResult.Unauthorized())
                    else -> throw it
                }
            }
    }
    return requestResult!!
}