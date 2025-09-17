import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    /** ksp */
    id("com.google.devtools.ksp")
    /** Hilt */
    id("com.google.dagger.hilt.android")
}

// 秘匿情報を読み込む
val secretPropertiesFile = rootProject.file("secret.properties")
val secretProperties = Properties()
secretProperties.load(secretPropertiesFile.inputStream())

android {
    namespace = "com.amefure.capsuletoyapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.amefure.capsuletoyapp"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            manifestPlaceholders["ADMOB_APP_ID"] = secretProperties["ADMOB_APP_ID_TEST"] ?: ""
            manifestPlaceholders["GOOGLE_MAP_API_KEY"] = secretProperties["GOOGLE_MAP_API_KEY"] ?: ""
        }
        release {
            // ProGuard(コードの縮小、最適化、難読化)を有効にするフラグ
            isMinifyEnabled = true
            // リソースの縮小を有効にするフラグ
            isShrinkResources = true
            manifestPlaceholders["ADMOB_APP_ID"] = secretProperties["ADMOB_APP_ID_PROD"] ?: ""
            manifestPlaceholders["GOOGLE_MAP_API_KEY"] = secretProperties["GOOGLE_MAP_API_KEY"] ?: ""

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /** Compose Navigation */
    implementation(libs.androidx.navigation.compose)

    /** Room(ローカルデータベース) */
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    /** Hilt(DI；依存性注入) */
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    /** Hilt for Navigation Compose */
    implementation(libs.androidx.hilt.navigation.compose)

    /** CameraX(カメラ機能) */
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    // CameraX のライフサイクル連動ライブラリ
    implementation(libs.androidx.camera.lifecycle)
    // CameraX 用の UI コンポーネント（CameraView）を提供
    implementation(libs.androidx.camera.view)

    /** マテリアルアイコン全読み込み */
    implementation(libs.androidx.material.icons.extended)

    /** 位置情報取得 */
    implementation(libs.play.services.location)

    /** Google Maps Compose */
    implementation(libs.maps.compose)

    /** Kotlin Coroutines */
    implementation(libs.kotlinx.coroutines.play.services)
}
