package eu.noharmdan.data.di

import eu.noharmdan.data.datastore.QuizDataStore
import org.koin.dsl.module

val dataModule = module {
    single {
        QuizDataStore(get())
    }
}