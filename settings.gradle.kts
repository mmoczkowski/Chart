enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "Chart"

include(":cache:api")
include(":cache:impl:lru")
include(":cache:impl:no-op")
include(":provider:api")
include(":provider:impl:debug")
include(":provider:impl:google")
include(":provider:impl:open-street-map")
include(":provider:impl:url")
include(":sample:basic")
include(":sample:mars")
include(":ui")
