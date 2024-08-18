package eu.noharmdan.domain.di

import eu.noharmdan.domain.rest.ClientFactory
import eu.noharmdan.domain.usecase.GetRandomQuestionsUseCase
import org.koin.dsl.module

val domainModule = module {

    single {
        ClientFactory.createClient()
    }

    single {
        GetRandomQuestionsUseCase(get())
    }
}