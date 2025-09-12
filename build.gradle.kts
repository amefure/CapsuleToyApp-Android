// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    /** ksp (Kotlinのバージョンに合わせて導入する) */
    id("com.google.devtools.ksp") version "2.1.10-1.0.31" apply false
    /** Hilt */
    id("com.google.dagger.hilt.android") version "2.57.1" apply false
    /** Spotless */
    id("com.diffplug.spotless") version "7.2.1"
}

// Spotless + ktlint の設定
// 解析： ./gradlew spotlessCheck
// 反映： ./gradlew spotlessApply
spotless {
    kotlin {
        // ----- Kotlin ファイルに対する整形ルール -----

        // フォーマット対象のファイルを指定
        target("**/*.kt")

        // ktlint のバージョンを指定
        ktlint("0.50.0")
            // ktlint の設定を上書き
            .editorConfigOverride(
                mapOf(
                    // インデントのスペース数を指定（4スペース）
                    "indent_size" to "4",
                    // ファイル末尾に改行を強制
                    "insert_final_newline" to "true"
                )
            )
    }

    kotlinGradle {
        // ----- build.gradle.kts ファイルに対する整形ルール -----
        // フォーマット対象の Gradle Kotlin DSL ファイルを指定
        target("**/*.gradle.kts")
        ktlint("0.50.0")
    }
}
