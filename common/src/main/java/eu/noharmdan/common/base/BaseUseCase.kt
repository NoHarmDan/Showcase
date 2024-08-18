package eu.noharmdan.common.base

import kotlinx.coroutines.flow.Flow

/**
 * A base class following the separate use cases approach.
 *
 * Meant to be implemented by classes providing a single piece of business logic,
 * (e.g. fetching a batch of data from a server) which may typically be reused.
 * The separate implementation shields the details of the business logic from
 * its places of use (e.g. repositories) while providing a common usage pattern.
 *
 * It is expected to share an instance through dependency injection and run
 * its logic by calling [execute]. Results of [execute] are returned in an observable
 * [Flow].
 *
 * Each subclass must also implement its own [Params] as a carrier of configuration
 * for each call of [execute].
 *
 */
abstract class BaseUseCase<in T : BaseUseCase.Params, out R> {

    abstract fun execute(params: T): Flow<R>

    interface Params

}