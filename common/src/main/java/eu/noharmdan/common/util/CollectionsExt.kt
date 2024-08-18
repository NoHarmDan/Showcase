package eu.noharmdan.common.util

/**
 * A simple convenience function which replaces an [old] element for a [new] one in an [Iterable],
 * assuming that an instance of [old] is present in the [Iterable].
 *
 * - If no instance is present, the [Iterable] is not changed.
 * - If multiple instances are present, **all** are replaced.
 */
fun <E> Iterable<E>.replace(old: E, new: E) = map {
    if (it == old) new else it
}