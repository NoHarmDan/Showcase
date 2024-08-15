package eu.noharmdan.showcase.base

import kotlinx.coroutines.flow.Flow

abstract class UseCase<in T : UseCase.Params, out R> {

    abstract fun execute(params: T): Flow<R>

    interface Params

}