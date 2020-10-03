buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(deps.android.gradle.plugin)
        classpath(deps.kotlin.gradle.plugin)
        classpath("com.android.tools.build:gradle:4.2.0-alpha10")
    }
}

subprojects {
    repositories {
        google()
        jcenter()
    }
}
