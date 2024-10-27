import org.gradle.internal.extensions.core.extra

group = "de.grimsi.gameyfin"
version = "0.0.1-SNAPSHOT"
val pf4jVersion: String by rootProject.extra
val pluginsDir: File by rootProject.extra
val appMainClass = "de.grimsi.gameyfin.PluginPocApplication"

plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.spring") version "2.0.20"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    application
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":plugin-api"))
    api("org.pf4j:pf4j:${pf4jVersion}")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.oshai:kotlin-logging-jvm:6.0.3")

    //developmentOnly("org.springframework.boot:spring-boot-devtools")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}



application {
    mainClass.set(appMainClass)
}

tasks.named<JavaExec>("run") {
    systemProperty("pf4j.pluginsDir", pluginsDir.absolutePath)
}

tasks.register<Jar>("uberJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    dependsOn(tasks.named("compileKotlin"))
    archiveClassifier.set("uber")

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    manifest {
        attributes["Main-Class"] = appMainClass
    }

    archiveBaseName.set("${project.name}-plugin-demo")
}