package eu.noharmdan.common.util

import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * A shared implementation of [CoroutineExceptionHandler] meant to simplify exception handling
 * across the app and make sure that no exceptions that happen in a coroutine are ignored.
 */
val baseCoroutineExceptionHandler = CoroutineExceptionHandler { _, ex ->
    // todo decide of exception should be thrown or only reported to e.g. Firebase Crashlytics etc.
    throw ex
}

