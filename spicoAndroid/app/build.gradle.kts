import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    id("kotlin-parcelize")
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.a401.spicoandroid"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.a401.spicoandroid"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "KAKAO_NATIVE_APP_KEY",
            "\"${properties["kakao.native.app.key"]}\""
        )
        manifestPlaceholders["KAKAO_APP_KEY"] = properties.getProperty("kakao.native.app.key")

        buildConfigField("String", "AZURE_KEY",
            "\"${properties["azure.key"]}\""
        )
        manifestPlaceholders["AZURE_KEY"] = properties.getProperty("azure.key")

        buildConfigField("String", "AZURE_REGION",
            "\"${properties["azure.region"]}\""
        )
        manifestPlaceholders["AZURE_REGION"] = properties.getProperty("azure.region")

        buildConfigField("String", "OPENAI_KEY",
            "\"${properties["openai.key"]}\"")
        manifestPlaceholders["OPENAI_KEY"] = properties.getProperty("openai.key")
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"${project.findProperty("BASE_URL")}\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    buildFeatures {
        compose = true
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    // Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Hilt (DI)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.client.sdk)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.view)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.adapter.rxjava2)

    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)

    implementation(libs.gson)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.datastore.core.android)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom)) // version integral management
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material.icons.extended) // Material Icons

    // 로그인 화면 캐러셀용
    implementation(libs.androidx.foundation.pager)

    // UI
    implementation(libs.lottie.compose) // Lottie animation
    implementation(libs.dotlottie) // .lottie file
    implementation(libs.accompanist.system.ui) // System UI Control (ex. StatusBar)
    implementation(libs.palette) // colorPicker
    implementation(libs.compose.reorderable)
    implementation(libs.mpandroidchart) // android chart

    // video player
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.exoplayer.dash)
    implementation(libs.media3.ui)
    implementation(libs.media3.ui.compose)

    // DataStore
    implementation(libs.datastore.preferences)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // 전면 카메라
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // kakao
    implementation(libs.v2.user)

    // Azure
    implementation("com.microsoft.cognitiveservices.speech:client-sdk:1.34.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // 비동기 처리
}
