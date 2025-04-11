pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "HelloStachka"
include(":simple-jni")
include(":icons")
include(":theme")
include(":jnigenerator:annotations")
project(":jnigenerator:annotations").projectDir = file("jnigenerator/java/annotations")
// One cannot simply put plugin into buildSrc
// https://discuss.gradle.org/t/compileonly-dependency-in-buildsrc/26879/6
includeBuild("external-build-plugin")
include(":jnihellostachka")
include(":logging")
include(":libloader")
