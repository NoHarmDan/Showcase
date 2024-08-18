package eu.noharmdan.data.di

import eu.noharmdan.data.datastore.QuizDataStore
import org.koin.dsl.module

/**
 * Koin module for dependency injection of all "data" related classes.
 *
 * (In a larger project, multiple e.g. function-separated modules would be declared here as well.)
 */
val dataModule = module {
    single {
        QuizDataStore(get())
    }
}