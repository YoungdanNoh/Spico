package com.a401.spicoandroid.infrastructure.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.a401.spicoandroid.data.auth.dto.AuthResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun setUserInfo(token: String, nickname: String, expiresIn: Long) {
        println("🐛 서버에서 받은 expiresIn = $expiresIn")
        println("🐛 현재 시간(ms) = ${System.currentTimeMillis()}")
        println("🐛 계산된 expiresAt(ms) = ${System.currentTimeMillis() + (expiresIn * 1000L)}")
        val expiresAt = System.currentTimeMillis() + (expiresIn * 1000L)
        context.dataStore.edit {
            it[UserPreferences.ACCESS_TOKEN] = token
            it[UserPreferences.NICKNAME] = nickname
            it[UserPreferences.EXPIRES_AT] = expiresAt
        }
        println("✅ token 저장됨 → $token")
    }

    val accessToken: Flow<String?> = context.dataStore.data.map {
        val expiresAt = it[UserPreferences.EXPIRES_AT] ?: 0L
        if (System.currentTimeMillis() > expiresAt) {
            null // 만료되었으면 null 반환
        } else {
            it[UserPreferences.ACCESS_TOKEN]
        }
    }

    val nickname: Flow<String?> = context.dataStore.data.map {
        it[UserPreferences.NICKNAME]
    }

    val expiresAt: Flow<Long?> = context.dataStore.data.map {
        it[UserPreferences.EXPIRES_AT]
    }

    suspend fun isTokenExpired(): Boolean {
        val expiresAt = context.dataStore.data.map {
            it[UserPreferences.EXPIRES_AT] ?: 0L
        }.first()
        return System.currentTimeMillis() > expiresAt
    }

    fun observeAccessToken(): Flow<String?> {
        return accessToken
    }

    fun observeUserInfo(): Flow<AuthResponse> {
        return combine(accessToken, nickname, expiresAt
        ) { accessToken, nickname, expiresAt ->
            if (accessToken != null && nickname != null && expiresAt != null) {
                AuthResponse(
                    accessToken = accessToken,
                    nickname = nickname,
                    expiresIn = expiresAt
                )
            } else {
                throw IllegalStateException("User info is incomplete.")
            }
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun autoClearIfExpired() {
        val expired = isTokenExpired()
        if (expired) {
            clear()
            println("🧹 Token expired → UserDataStore cleared")
        }
    }

}
