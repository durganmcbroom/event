plugins {
    kotlin("jvm") version "1.7.10"
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.4.32"
}

group = "com.durganmcbroom"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.dokka")

    repositories {
        mavenCentral()
    }

    tasks.compileKotlin {
        destinationDirectory.set(tasks.compileJava.get().destinationDirectory.asFile.get())
        kotlinOptions.jvmTarget = "17"
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    tasks.compileJava {
        targetCompatibility = "17"
        sourceCompatibility = "17"
    }

    dependencies {
        implementation(kotlin("stdlib"))
        testImplementation(kotlin("test"))
    }

    kotlin {
        explicitApi()
    }

    tasks.test {
        useJUnitPlatform()
    }
}