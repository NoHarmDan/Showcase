package eu.noharmdan.common.base

import androidx.compose.runtime.Immutable
import eu.noharmdan.common.util.collectState

/**
 * Interface used for classes representing the "current" state of the UI as decided by [BaseViewModel].
 *
 * All of its subclasses should make sure that all their fields use [Immutable] classes as well as
 * immutable implementations of [List]s or [Set]s etc. in order to prevent unnecessary recompositions.
 *
 * @see collectState
 */
@Immutable
interface ViewState