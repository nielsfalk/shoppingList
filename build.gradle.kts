plugins {
    kotlin("multiplatform") version "2.4.10"
    kotlin("plugin.compose") version "2.4.10"
    kotlin("plugin.serialization") version "2.4.10"
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

    js(IR) {
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
        }
    }
}
