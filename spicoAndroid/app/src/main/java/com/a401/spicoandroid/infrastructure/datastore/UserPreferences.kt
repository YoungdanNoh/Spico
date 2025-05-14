package com.a401.spicoandroid.infrastructure.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(@ApplicationContext context: Context) {
    private val Context.dataStore by preferencesDataStore("user_prefs")
    private val dataStore = context.dataStore

    companion object {
        val LOGIN_STATE = booleanPreferencesKey("login_state")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    suspend fun setLoginState(isLoggedIn: Boolean) {
        dataStore.edit { prefs ->
            prefs[LOGIN_STATE] = isLoggedIn
        }
    }

    suspend fun setUserInfo(name: String, email: String) {
        dataStore.edit { prefs ->
            prefs[USER_NAME] = name
            prefs[USER_EMAIL] = email
        }
    }

    val userInfoFlow: Flow<UserInfo> = dataStore.data.map { prefs ->
        UserInfo(
            name = prefs[USER_NAME] ?: "김도영",
            email = prefs[USER_EMAIL] ?: "doyeong@gmail.com"
        )
    }
}

data class UserInfo(val name: String, val email: String)