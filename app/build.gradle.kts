import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use(::load)
    }
}

fun String.asBuildConfigString(): String =
    "\"${replace("\\", "\\\\").replace("\"", "\\\"")}\""

android {
    namespace = "com.rjw.bunpoun3"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rjw.bunpoun3"
        minSdk = 26
        targetSdk = 35
        versionCode = 4
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        buildConfigField(
            "String",
            "SUPABASE_URL",
            localProperties.getProperty("SUPABASE_URL", "").asBuildConfigString(),
        )
        buildConfigField(
            "String",
            "SUPABASE_ANON_KEY",
            localProperties.getProperty("SUPABASE_ANON_KEY", "").asBuildConfigString(),
        )
        buildConfigField(
            "String",
            "GOOGLE_WEB_CLIENT_ID",
            localProperties.getProperty("GOOGLE_WEB_CLIENT_ID", "").asBuildConfigString(),
        )
    }

    signingConfigs {
        create("release") {
            val storePath = localProperties.getProperty("RELEASE_STORE_FILE") ?: "keystore.jks"
            storeFile = file(storePath)
            storePassword = localProperties.getProperty("RELEASE_STORE_PASSWORD")
            keyAlias = localProperties.getProperty("RELEASE_KEY_ALIAS")
            keyPassword = localProperties.getProperty("RELEASE_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            val releaseConfig = signingConfigs.getByName("release")
            val isCi = System.getenv("CI") == "true"
            val isReleaseTask = gradle.startParameter.taskNames.any { task ->
                task.contains("Release", ignoreCase = true)
            }
            val hasReleaseKeystore = releaseConfig.storeFile?.exists() == true &&
                !releaseConfig.storePassword.isNullOrBlank() &&
                !releaseConfig.keyAlias.isNullOrBlank() &&
                !releaseConfig.keyPassword.isNullOrBlank()
            if (isCi && isReleaseTask && !hasReleaseKeystore) {
                throw GradleException("Release keystore missing or incomplete in CI.")
            }
            signingConfig = if (hasReleaseKeystore) releaseConfig else signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2026.02.00")

    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.credentials:credentials:1.6.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.6.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.2.0")

    implementation("androidx.room:room-runtime:2.8.1")
    implementation("androidx.room:room-ktx:2.8.1")
    ksp("androidx.room:room-compiler:2.8.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
