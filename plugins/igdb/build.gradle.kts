plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "2.0.20-1.0.24"
}

group = "de.grimsi.gameyfin"
version = "0.0.1-SNAPSHOT"
val pf4jVersion: String by rootProject.extra

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":plugin-api"))
    compileOnly("org.pf4j:pf4j:$pf4jVersion") {
        exclude(group = "org.slf4j")
    }
    ksp("care.better.pf4j:pf4j-kotlin-symbol-processing:2.0.20-1.0.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}