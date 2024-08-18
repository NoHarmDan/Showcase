package eu.noharmdan.showcase.di

import eu.noharmdan.data.datastore.QuizDataStore
import eu.noharmdan.showcase.scene.quiz.QuizViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        QuizDataStore(get())
    }

    viewModel {
        QuizViewModel(get(), get())
    }
}