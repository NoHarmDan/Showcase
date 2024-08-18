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

abstract class BaseDataStore(context: Context, name: String, migrations: List<DataMigration<Preferences>> = listOf()) {

    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + baseCoroutineExceptionHandler)
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

    protected fun <T> lazyFlow(key: Preferences.Key<T>, defaultValue: () -> T): Lazy<Flow<T>> = lazy {
        dataStore.data.map {
            it[key] ?: defaultValue()
        }
    }

    protected suspend fun <T> get(key: Preferences.Key<T>, defaultValue: () -> T): T {
        return dataStore.data.map {
            it[key] ?: defaultValue()
        }.first()
    }

    protected suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

}