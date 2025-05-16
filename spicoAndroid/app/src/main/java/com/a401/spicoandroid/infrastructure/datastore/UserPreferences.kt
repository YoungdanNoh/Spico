package com.a401.spicoandroid.infrastructure.datastore

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferences {

    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val NICKNAME = stringPreferencesKey("nickname")
    val EXPIRES_AT = longPreferencesKey("expires_at")
}