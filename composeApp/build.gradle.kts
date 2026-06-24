
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

    //kotlinxSerialization : kotlinversion
    kotlin("plugin.serialization") version "2.1.0"
    id("com.github.gmazzo.buildconfig") version "5.5.1"
}

// Read API key from local.properties
val localProperties = Properties() //import java.utils
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

//A ajouter à la racine
buildConfig {
    // Définit le nom de la classe générée
    className("BuildConfig")
    // Le package où la classe sera générée
    packageName("com.amonteiro.a03_kmp_mprolead_g1")

    // Récupération sécurisée de la clé
    val photographerApiKey = localProperties.getProperty("photographer.api.key") ?: ""

    println("photographerApiKey chargée : $photographerApiKey")

    // Crée le champ pour tous les targets (Android, iOS, Desktop)
    buildConfigField("String", "PHOTOGRAPHER_API_KEY", "\"$photographerApiKey\"")
}


kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)

            //Client de requêtes spécifique à Android
            implementation("io.ktor:ktor-client-okhttp:3.2.2")

            implementation("io.insert-koin:koin-android:4.1.+")


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
            implementation("io.ktor:ktor-client-core:3.2.2")
            //Intégration avec la bibliothèque de serialisation, gestion des headers
            implementation("io.ktor:ktor-client-content-negotiation:3.2.2")
            //Serialisation JSON
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.2.2")
            //Pour le logger
            implementation("io.ktor:ktor-client-logging:3.2.2")

            //Coil ImageLoader
            implementation("io.coil-kt.coil3:coil-network-ktor3:3.2.0")
            implementation("io.coil-kt.coil3:coil-compose:3.2.0")

            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")

            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.+")

            //Injection dépendance KOIN
            implementation("io.insert-koin:koin-compose:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel-navigation:4.1.+")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            //Client de requêtes spécifique au bureau sur JVM donc même qu'Android
            implementation("io.ktor:ktor-client-okhttp:3.2.2")
        }
        iosMain.dependencies {
            //Client de requêtes spécifique à iOS
            implementation("io.ktor:ktor-client-darwin:3.2.2")
        }

    }
}

android {
    namespace = "com.amonteiro.a03_kmp_mprolead_g1"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.amonteiro.a03_kmp_mprolead_g1"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.amonteiro.a03_kmp_mprolead_g1.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.amonteiro.a03_kmp_mprolead_g1"
            packageVersion = "1.0.0"
        }
    }
}
