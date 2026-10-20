import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.4.0"

}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    jvm()

    android {
        namespace = "fr.amonteiro.a10_legrand_kmp.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation("io.ktor:ktor-client-okhttp:3.4.2")

        }
        iosMain.dependencies {
            //Client de requêtes spécifique à iOS
            implementation("io.ktor:ktor-client-darwin:3.4.2")
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // (les interfaces en gros)
            implementation("io.ktor:ktor-client-core:3.4.2")
            //Intégration avec la bibliothèque de serialisation, gestion des headers
            implementation("io.ktor:ktor-client-content-negotiation:3.4.2")
            //Serialisation JSON
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.2")
            //Pour le logger
            implementation("io.ktor:ktor-client-logging:3.4.2")

            //pour les icônes avec org.jetbrains spécifique à KMP
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
            //Pour le ViewModel
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.11.+")

            //ImageLoader (identique)
            implementation("io.coil-kt.coil3:coil-network-ktor3:3.2.0")
            implementation("io.coil-kt.coil3:coil-compose:3.2.0")

            //Navigation 3 avec org.jetbrains spécifique à KMP
            implementation("org.jetbrains.androidx.navigation3:navigation3-ui:1.1.2")

            //Injection dépendance KOIN
            implementation("io.insert-koin:koin-compose:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel-navigation:4.1.+")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            //Client de requêtes spécifique au bureau sur JVM donc même qu'Android
            implementation("io.ktor:ktor-client-okhttp:3.4.2")
        }

    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}