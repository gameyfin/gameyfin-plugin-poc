package de.grimsi.gameyfin.pluginpoc

import io.github.oshai.kotlinlogging.KotlinLogging
import org.pf4j.DefaultPluginManager
import org.pf4j.PluginManager
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import java.nio.file.Path
import kotlin.collections.map
import kotlin.io.path.absolute

@Configuration
class PluginManagerConfig(
    private val gameService: GameService
) {
    private val log = KotlinLogging.logger {}
    val pluginsDir = System.getProperty("pf4j.pluginsDir", "./plugins")

    @Bean
    fun pluginManager(): PluginManager {
        log.info { "Loading plugins from ${Path.of(pluginsDir).absolute()}" }
        return DefaultPluginManager(Path.of(pluginsDir))
    }

    @EventListener(ApplicationReadyEvent::class)
    fun loadPlugins() {
        pluginManager().loadPlugins()
        pluginManager().startPlugins()
        log.info { "Loaded plugins: ${pluginManager().plugins.map { it.pluginId }}" }
        val extensionPoints =
            pluginManager().plugins.map { pluginManager().getExtensions(it.pluginId) }.flatten().toSet()
        log.info { "Supported extension points by loaded plugins: $extensionPoints" }

        gameService.demo()
    }
}