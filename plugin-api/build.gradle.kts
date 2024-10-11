val pf4jVersion: String by project

plugins {
    kotlin("jvm") version "2.0.20"
}

group = "de.grimsi.gameyfin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("org.pf4j:pf4j:${pf4jVersion}")
}

kotlin {
    jvmToolchain(21)
}