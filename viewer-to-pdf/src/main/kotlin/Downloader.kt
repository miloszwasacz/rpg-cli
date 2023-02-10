import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class Downloader {
    companion object {
        fun download(url: String): ByteArray? = try {
            URL(url).openStream().readAllBytes()
        } catch (_: IOException) {
            null
        }

        fun download(url: String, out: File): Boolean? = try {
            URL(url).openStream().use { Files.copy(it, out.toPath(), StandardCopyOption.REPLACE_EXISTING) }
            true
        } catch (e: IOException) {
            null
        }

        fun <T> iteratePages(block: (page: Int) -> T?) {
            var page = 1
            print("\rDownloaded: 0 pages...")
            do {
                val result = block(page)
                if (result != null)
                    print("\rDownloaded: $page pages...")
                page++
            } while (result != null)
            println(" Done.")
        }

        fun useFiles(url: String, block: (files: List<File>) -> Unit) {
            //FIXME Different temp folder
            val temp = Paths.get("out/temp").toAbsolutePath().toFile()
            if (temp.exists())
                temp.deleteRecursively()
            temp.mkdirs()

            val files = mutableListOf<File>()
            iteratePages { page ->
                val file = Files.createTempFile(temp.toPath(), "page${page}_", ".jpg").toFile()
                val result = download("$url$page.jpg", file)
                if (result == true)
                    files.add(file)
                result
            }
            block(files)

            temp.deleteRecursively()
        }
    }
}