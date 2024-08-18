package eu.noharmdan.common.base

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import eu.noharmdan.common.util.baseCoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * A wrapper class for [DataStore] allowing simple instantiation with migrations
 * and introducing convenience functions for stored data access.
 *
 * @see lazyFlow
 * @see get
 * @see set
 */
abstract class BaseDataStore(context: Context, name: String, migrations: List<DataMigration<Preferences>> = listOf()) {

    /**
     * The coroutine scope to be used for all storage and access operations. Uses the [baseCoroutineExceptionHandler].
     */
    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + baseCoroutineExceptionHandler)

    /**
     * The inner instance of [dataStore] used for all storage and access operations of this wrapper.
     */
    private val dataStore: DataStore<Preferences>

    init {
        val produceFile = { context.preferencesDataStoreFile(name) }
        dataStore = PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = migrations,
            scope = ioScope,
            produceFile = produceFile
        )
    }

    /**
     * A convenience function which allows simplified access to data stored under [key] through a lazy [Flow]
     * with [defaultValue] used to return a value if no data is stored.
     */
    protected fun <T> lazyFlow(key: Preferences.Key<T>, defaultValue: () -> T): Lazy<Flow<T>> = lazy {
        dataStore.data.map {
            it[key] ?: defaultValue()
        }
    }

    /**
     * A convenience function which allows simplified access to the latest instance of data stored under [key]
     * with [defaultValue] used to return a value if no data is stored.
     */
    protected suspend fun <T> get(key: Preferences.Key<T>, defaultValue: () -> T): T {
        return dataStore.data.map {
            it[key] ?: defaultValue()
        }.first()
    }

    /**
     * A convenience function which allows simplified storage of data [value] under [key].
     */
    protected suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

}