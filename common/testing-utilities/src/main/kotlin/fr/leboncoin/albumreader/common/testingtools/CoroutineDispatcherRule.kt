package fr.leboncoin.albumreader.common.testingtools

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * ## Description
 *
 * JUnit rule replacing the main Coroutine Dispatcher during tests execution.
 *
 * From [official documentation](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#inject-dispatchers):
 * > _The `viewModelScope` property of `ViewModel` classes is hardcoded to [Dispatchers.Main]. Replace it in
 * tests by calling [Dispatchers.setMain] and passing in a test dispatcher._
 *
 * @param testDispatcher Test dispatcher to use during tests execution, default is [StandardTestDispatcher].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}