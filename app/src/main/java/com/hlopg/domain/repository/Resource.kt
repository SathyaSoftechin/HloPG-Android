package com.hlopg.domain.repository

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val token: String? = null
) {

    class Success<T>(
        data: T,
        token: String? = null
    ) : Resource<T>(
        data = data,
        token = token
    )

    class Error<T>(
        message: String?,
        data: T? = null
    ) : Resource<T>(
        data = data,
        message = message
    )

    class Loading<T>(
        data: T? = null
    ) : Resource<T>(
        data = data
    )
}
