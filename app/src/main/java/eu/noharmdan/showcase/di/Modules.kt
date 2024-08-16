package eu.noharmdan.showcase.di

import eu.noharmdan.showcase.model.datastore.AppDataStore
import eu.noharmdan.showcase.rest.ClientFactory
import eu.noharmdan.showcase.scene.quiz.QuizViewModel
import eu.noharmdan.showcase.usecase.GetRandomQuestionsUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        ClientFactory.createClient()
    }

    single {
        GetRandomQuestionsUseCase(get())
    }

    single {
        AppDataStore(get())
    }

    viewModel {
        QuizViewModel(get(), get())
    }
}