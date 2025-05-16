package com.a401.spicoandroid

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.kakao.sdk.common.KakaoSdk

@HiltAndroidApp
class SpicoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}