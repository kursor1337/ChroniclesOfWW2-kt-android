// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.7.10")
        classpath("com.google.gms:google-services:4.3.13")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.1")


        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}



plugins {
    id("org.jetbrains.kotlin.android") version("1.7.10") apply(false)
    id("org.jetbrains.kotlin.jvm") version("1.6.21") apply(false)
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}