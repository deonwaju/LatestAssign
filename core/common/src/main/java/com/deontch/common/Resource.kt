package com.deontch.common

/**
 * A generic sealed class that contains data and status about loading this data.
 * It is used to communicate the state of a network request or data fetch
 * from the data layer (repository) to the UI layer (ViewModel/View).
 *
 * @param T The type of the data being held.
 * @property data The actual data. Can be null in loading or error states.
 * @property message An error message. Only present in the Error state.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Represents a successful state with the loaded data.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Represents an error state, optionally with cached data and an error message.
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /**
     * Represents a loading state, optionally with initial or cached data.
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
