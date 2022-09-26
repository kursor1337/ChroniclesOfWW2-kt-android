val ktor_version: String by project
val koin_version: String by project
val dtos_version: String by project
val model_version: String by project
val room_version: String by project

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.kursor.chroniclesofww2"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {


    //my
    implementation("com.github.kursor1337:chronicles-of-ww2-kt-dtos:$dtos_version")
    implementation("com.github.kursor1337:chronicles-of-ww2-kt-model:$model_version")

    implementation(project(":connection"))
    implementation(project(":data"))
    implementation(project(":domain"))

    //ktor client
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    //Koin di
    // Koin Core features
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("androidx.room:room-ktx:$room_version")
    // Koin Test features
    testImplementation("io.insert-koin:koin-test:$koin_version")
    // Koin main features for Android
    implementation("io.insert-koin:koin-android:$koin_version")
    // Java Compatibility
    implementation("io.insert-koin:koin-android-compat:$koin_version")
    // Jetpack WorkManager
    implementation("io.insert-koin:koin-androidx-workmanager:$koin_version")
    // Navigation Graph
    implementation("io.insert-koin:koin-androidx-navigation:$koin_version")
    // Koin for JUnit 5
    testImplementation("io.insert-koin:koin-test-junit5:$koin_version")

    implementation("androidx.lifecycle:lifecycle-extensions:")
    implementation("androidx.appcompat:appcompat:1.6.0-rc01")

    implementation("com.phelat:navigationresult:1.0.1")

    implementation("androidx.navigation:navigation-fragment-ktx:2.5.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.2")

    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.5.2")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:2.5.2")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.moshi:moshi-kotlin:1.13.0")
//    testImplementation 'junit:junit:5'
//    testImplementation 'org.junit.jupiter:junit-jupiter'
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}