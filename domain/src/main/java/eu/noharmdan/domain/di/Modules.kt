package eu.noharmdan.domain.di

import eu.noharmdan.domain.rest.ClientFactory
import eu.noharmdan.domain.usecase.GetRandomQuestionsUseCase
import org.koin.dsl.module

/**
 * Koin module for dependency injection of all "domain" related classes.
 *
 * (In a larger project, multiple e.g. function-separated modules would be declared here as well.)
 */
val domainModule = module {

    single {
        ClientFactory.createClient()
    }

    single {
        GetRandomQuestionsUseCase(get())
    }
}