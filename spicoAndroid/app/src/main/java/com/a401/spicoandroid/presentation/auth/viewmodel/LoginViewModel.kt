package com.a401.spicoandroid.presentation.auth.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a401.spicoandroid.domain.auth.AuthRepository
import com.a401.spicoandroid.infrastructure.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val kakaoAuthUrl = ""
    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()

    private fun buildOAuthUrl(
        clientId: String,
        redirectUri: String
    ): String {
        return Uri.parse(kakaoAuthUrl)
            .buildUpon()
            .appendQueryParameter("client_id", clientId)
            .appendQueryParameter("redirect_uri", redirectUri)
            .appendQueryParameter("response_type", "code")
            .build()
            .toString()
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val clientId = ""
                val redirectUri = ""
                val url = buildOAuthUrl(clientId, redirectUri)

                _authState.update {
                    it.copy(
                        isLoading = false,
                        openOAuthUrl = url
                    )
                }
            } catch (e: Exception) {
                _authState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun onOAuthCallbackReceived(
        accessToken: String
    ) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true) }

            try {
                authRepository.saveAccessToken(accessToken)

                _authState.update {
                    userPreferences.setLoginState(true)
                    it.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        openOAuthUrl = null
                    )
                }
            } catch (e: Exception) {
                _authState.update {
                    it.copy(
                        isLoading = false,
                        isLoggedIn = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }
}