import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version "2.1.21"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            implementation("io.ktor:ktor-client-core:2.3.2")
            implementation("io.ktor:ktor-client-cio:2.3.2")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.2")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.2")
        }
        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:2.3.7")
        }
        val ktorVersion = "2.3.7"

        androidMain {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.danielchew.zwiftviewer.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

tasks.register("assembleXCFramework") {
    notCompatibleWithConfigurationCache("Uses project.exec and lazy project references")

    dependsOn(
        tasks.named("linkReleaseFrameworkIosArm64"),
        tasks.named("linkReleaseFrameworkIosSimulatorArm64")
    )

    doLast {
        val frameworkName = "Shared"
        val outputDir = buildDir.resolve("XCFrameworks/release")
        outputDir.mkdirs()

        exec {
            commandLine(
                "xcodebuild", "-create-xcframework",
                "-framework", buildDir.resolve("bin/iosArm64/releaseFramework/${frameworkName}.framework"),
                "-framework", buildDir.resolve("bin/iosSimulatorArm64/releaseFramework/${frameworkName}.framework"),
                "-output", outputDir.resolve("${frameworkName}.xcframework")
            )
        }
    }
}
