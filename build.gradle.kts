import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform") version "2.4.10"
    kotlin("plugin.compose") version "2.4.10"
    kotlin("plugin.serialization") version "2.4.10"
    kotlin("plugin.power-assert") version "2.4.10"
    id("com.google.devtools.ksp") version "2.3.12"
    id("io.kotest") version "6.2.6"
    id("org.jetbrains.compose") version "1.12.2"
}

group = "de.nielsfalk.shopping.list"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvmToolchain(17)

    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        jsMain.dependencies {
            implementation("org.jetbrains.compose.runtime:runtime:1.12.2")
            implementation("org.jetbrains.compose.html:html-core:1.12.2")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.12.0")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.8.0")
        }
        jsTest.dependencies {
            implementation(kotlin("test"))
            implementation("io.kotest:kotest-framework-engine:6.2.6")
        }
    }
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
powerAssert {
    functions = listOf("kotlin.test.assertTrue")
}

val buildVersion = providers.environmentVariable("GITHUB_SHA").orElse("dev")

tasks.named<ProcessResources>("jsProcessResources") {
    val version = buildVersion.get()
    inputs.property("buildVersion", version)
    filesMatching("sw.js") {
        filter { line -> line.replace("__BUILD_VERSION__", version) }
    }
}
