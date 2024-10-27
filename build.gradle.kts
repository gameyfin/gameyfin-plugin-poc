import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

plugins {
    kotlin("jvm") version "2.0.20"
}

subprojects {
    apply(plugin = "java")

    version = "2.0.0-SNAPSHOT"

    java.sourceCompatibility = JavaVersion.VERSION_21
    java.targetCompatibility = JavaVersion.VERSION_21

    tasks.withType<KotlinJvmCompile> {
        compilerOptions {
            languageVersion = KotlinVersion.KOTLIN_2_0
            apiVersion = KotlinVersion.KOTLIN_2_0
            jvmTarget = JvmTarget.JVM_21
            freeCompilerArgs.add("-Xjsr305=strict")
        }
    }
}

tasks.named("build") {
    dependsOn(":gameyfin:uberJar")
}

extra.set("pluginsDir", rootProject.layout.buildDirectory.get().asFile.resolve("../packaged_plugins"))