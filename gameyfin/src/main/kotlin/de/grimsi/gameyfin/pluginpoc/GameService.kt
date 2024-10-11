package de.grimsi.gameyfin.pluginpoc

import de.grimsi.gameyfin.pluginapi.GameMetadataFetcher
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class GameService(
    private val metadataPlugins: List<GameMetadataFetcher>
) {
    private val log = KotlinLogging.logger {}

    fun demo() {
        createFromFile(Path.of("/path/to/my/game/library/Tetris"))
    }

    private fun createFromFile(path: Path) {
        if (metadataPlugins.isEmpty()) {
            log.error { "No plugins for extension point ${GameMetadataFetcher::class} found." }
            return
        }

        val extractedGameTitle = path.fileName.toString()
        val metadata = metadataPlugins.first().fetchMetadata(extractedGameTitle)
        log.info { "Metadata for $extractedGameTitle: $metadata" }
    }
}