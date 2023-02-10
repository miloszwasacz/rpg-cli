import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

class Downloader {
    companion object {
        fun download(url: String): ByteArray? = try {
            URL(url).openStream().readAllBytes()
        } catch (_: IOException) {
            null
        }

        fun download(url: String, outFile: String): Boolean = try {
            URL(url).openStream().use { Files.copy(it, Paths.get(outFile)) }
            true
        } catch (_: IOException) {
            false
        }

        fun <T> iteratePages(block: (page: Int) -> T?) {
            var page = 1
            do {
                val result = block(page)
                if (result != null)
                    print("\rDownloaded: ${page - 1} pages...")
                page++
            } while (result != null)
            println("\nDone.")
        }
    }
}