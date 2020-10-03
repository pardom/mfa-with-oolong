plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "oolong.counter.android"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerVersion = deps.kotlin.version
        kotlinCompilerExtensionVersion = deps.androidx.compose.version
    }
    packagingOptions {
        exclude("META-INF/*kotlin*")
    }
}

dependencies {
    implementation(deps.androidx.appcompat.core)
    implementation(deps.androidx.compose.foundation.core)
    implementation(deps.androidx.compose.foundation.layout)
    implementation(deps.androidx.compose.material.core)
    implementation(deps.androidx.compose.runtime.core)
    implementation(deps.androidx.ui.tooling)
    implementation(deps.oolong)
    implementation(project(":src:counter:app"))
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf("-Xallow-jvm-ir-dependencies")
    }
}