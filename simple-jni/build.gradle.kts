import com.example.gradle.ExternalBuildExtension
import com.example.gradle.JniBuild
import com.example.gradle.LocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.example.external-build-plugin")
}

configure<ExternalBuildExtension> {
    target = "//cpp:hellostachka_lib"
    dir = file("$rootDir/hellostachka-lib")
    libName = "libhellostachka_lib.so"
}

LocalProperties.rootDir = rootDir

android {
    namespace = "com.example.simplejni"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.simplejni"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            // Specifies the ABI configurations of your native
            // libraries Gradle should build and package with your app.
            abiFilters.add("arm64-v8a")
        }

        @Suppress("UnstableApiUsage")
        externalNativeBuild {
            when (LocalProperties.jniBuild) {
                JniBuild.NdkBuild -> Unit
                JniBuild.ExternalBuild,
                JniBuild.Cmake -> cmake {
                    arguments += listOf(
                        "-DANDROID_STL=c++_static",
                        "-DANDROID_TOOLCHAIN=clang",
                        "-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON"
                    )
                }
            }
        }

        val nativeLibName = when (LocalProperties.jniBuild) {
            JniBuild.ExternalBuild -> "hellostachka_lib"
            else -> "hellostachka"
        }

        buildConfigField("String", "LIB_NAME", "\"${nativeLibName}\"")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    externalNativeBuild {
        when (LocalProperties.jniBuild) {
            JniBuild.NdkBuild -> ndkBuild {
                path = file("src/main/cpp/Android.mk")
            }

            JniBuild.Cmake, JniBuild.ExternalBuild -> cmake {
                path = file("src/main/cpp/CMakeLists.txt")
            }
        }
    }
}

/**
 * See <a href="https://youtrack.jetbrains.com/issue/KT-35127">yt/KT-35127</a>
 */
tasks.register<JavaCompile>("generateJNIHeaders") {
    val destination = layout.buildDirectory.dir("generated/jni/includes").get().asFile.path
    source = fileTree("srcCopy/main/java")
    destinationDirectory.set(file(destination))

    classpath = files(android.sourceSets["main"].java.srcDirs)

    options.compilerArgs = listOf("-h", destination)

    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(project(":jnihellostachka"))
    implementation(project(":logging"))
    implementation(project(":libloader"))

    // region Compose
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.activity)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    androidTestImplementation(composeBom)
    androidTestImplementation(libs.compose.ui.test.junit)
    // endregion

    implementation(project(":icons"))
    implementation(project(":theme"))

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
