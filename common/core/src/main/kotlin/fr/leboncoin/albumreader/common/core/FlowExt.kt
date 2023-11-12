package fr.leboncoin.albumreader.common.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

/**
 * Wrapper for [Flow.stateIn] configured with a default `SharingStarted.WhileSubscribed(5000)` adapted for android
 * lifecycle which avoids cancelling the flow on quick configuration change (device rotation, etc.).
 *
 * See this [related blog post](https://medium.com/androiddevelopers/migrating-from-livedata-to-kotlins-flow-379292f419fb).
 */
fun <T> Flow<T>.stateInForAndroidLifecycle(scope: CoroutineScope, initialValue: T): StateFlow<T> =
    stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = 5000 // Avoid cancelling the flow on configuration change
        ),
        initialValue = initialValue
    )

/**
 * Wrapper for [Flow.shareIn] configured with a default `SharingStarted.WhileSubscribed(5000)` adapted for android
 * lifecycle which avoids cancelling the flow on quick configuration change (device rotation, etc.).
 *
 * See this [related blog post](https://medium.com/androiddevelopers/migrating-from-livedata-to-kotlins-flow-379292f419fb).
 */
fun <T> Flow<T>.shareInForAndroidLifecycle(scope: CoroutineScope, replay: Int = 0): SharedFlow<T> =
    shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = 5000 // Avoid cancelling the flow on configuration change
        ),
        replay = replay
    )
