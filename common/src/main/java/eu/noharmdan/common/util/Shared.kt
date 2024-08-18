package eu.noharmdan.common.util

import kotlinx.coroutines.CoroutineExceptionHandler

val baseCoroutineExceptionHandler = CoroutineExceptionHandler { _, ex ->
    throw ex
    // todo decide of exception should be thrown or only reported to e.g. Firebase Crashlytics etc.
}

