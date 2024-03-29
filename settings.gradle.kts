import org.gradle.api.initialization.resolve.RepositoriesMode

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { setUrl("https://www.jitpack.io") }
         // Warning: this repository is going to shut down soon
    }
}
rootProject.name = "Chronicles Of WW2"
include("app", "data", "connection", "domain")
