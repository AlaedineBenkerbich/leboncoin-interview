package fr.leboncoin.albumreader.common.core

import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

/**
 * @see Dispatchers.Default
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

/**
 * @see Dispatchers.IO
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

/**
 * @see Dispatchers.Main
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher
