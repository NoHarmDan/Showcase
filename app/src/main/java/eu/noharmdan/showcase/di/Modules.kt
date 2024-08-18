package eu.noharmdan.showcase.di

import eu.noharmdan.data.datastore.QuizDataStore
import eu.noharmdan.showcase.scene.quiz.QuizViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for dependency injection of all top-level application related classes.
 *
 * (In a larger project, multiple e.g. function-separated modules would be declared here as well.)
 */
val appModule = module {
    single {
        QuizDataStore(get())
    }

    viewModel {
        QuizViewModel(get(), get())
    }
}