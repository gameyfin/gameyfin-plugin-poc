val pluginsDir: File by rootProject.extra


plugins {
    kotlin("jvm") // need to apply kotlin plugin here as it provides 'build' task
}

// here we define the tasks which will build the plugins in the subprojects
subprojects {
    // if the variable definitions are put here they are resolved for each subproject
    val pluginId: String by project
    val pluginClass: String by project
    val pluginProvider: String by project

    val project = this
    // we have to apply the gradle jvm plugin, because it provides the jar and build tasks
    apply(plugin = "org.jetbrains.kotlin.jvm")

    // for the jar task we have to set the plugin properties, so they can be written to the manifest
    tasks.jar {
        manifest {
            attributes["Plugin-Class"] = pluginClass
            attributes["Plugin-Id"] = pluginId
            attributes["Plugin-Version"] = archiveVersion
            attributes["Plugin-Provider"] = pluginProvider
        }

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(configurations.runtimeClasspath.get().map { project.zipTree(it) }) {
            exclude("META-INF/*.SF")
            exclude("META-INF/*.DSA")
            exclude("META-INF/*.RSA")
        }
        from(sourceSets["main"].output.classesDirs)
        from(sourceSets["main"].resources)
        isZip64 = true
    }
    
    // the assemblePlugin will copy the jar file into the common plugins directory
    tasks.register<Copy>("assemblePlugin") {
        from(project.tasks.jar.map { it.archiveFile })
        into(pluginsDir)
    }
}

tasks.register<Copy>("assemblePlugins") {
    dependsOn(subprojects.map { it.tasks.named("assemblePlugin") })
}

tasks {
    "build" {
        dependsOn(named("assemblePlugins"))
    }
}