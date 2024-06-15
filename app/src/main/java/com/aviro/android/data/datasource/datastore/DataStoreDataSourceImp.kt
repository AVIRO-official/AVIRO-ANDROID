package com.aviro.android.data.datasource.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aviro.android.domain.entity.key.DATASTORE_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(DATASTORE_KEY)

class DataStoreDataSourceImp @Inject constructor (
    @ApplicationContext private val context: Context
) : DataStoreDataSource {


    override suspend fun readDataStore(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[dataStoreKey]

    }

    override suspend fun writeDataStore(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)

        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }
    override suspend fun removeDataStore(key: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it.remove(dataStoreKey)
        }
    }
    override suspend fun resetDataStore() {
        context.dataStore.edit {
            it.clear()
        }
    }
}