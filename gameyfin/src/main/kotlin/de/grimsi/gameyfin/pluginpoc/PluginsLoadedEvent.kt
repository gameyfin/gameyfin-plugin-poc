package de.grimsi.gameyfin.pluginpoc

import org.springframework.context.ApplicationEvent

class PluginsLoadedEvent(source: Any) : ApplicationEvent(source)