val dtos_version: String by project
val model_version: String by project

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.github.kursor1337:chronicles-of-ww2-kt-dtos:$dtos_version")
    implementation("com.github.kursor1337:chronicles-of-ww2-kt-model:$model_version")
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}
