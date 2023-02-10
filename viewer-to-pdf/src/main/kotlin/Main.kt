import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import java.io.File
import java.nio.file.Paths

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            setupLogger()
            SSLManager.ignoreSSL()
            //TODO Add flags and args (e.g. input URL, output file, silent, etc.)

            println("AnyFlip URL:")
            val url = readln().trim()

            val out = Paths.get("out").toAbsolutePath().toFile()
            if (out.exists()) out.deleteRecursively()
            out.mkdir()

            //TODO Customizable output path
            val outFile = "${out.absolutePath}${File.separator}pdr.pdf"

            val pdf = PdfHandler(outFile)
            when {
                args.contains("--no-ocr") -> {
                    pdf.use { document ->
                        Downloader.iteratePages { page ->
                            Downloader.download("$url$page.jpg")?.let {
                                val img = Image(ImageDataFactory.createJpeg(it))
                                pdf.addImagePage(document, img)
                            }
                        }
                    }
                }

                else -> {
                    Downloader.useFiles(url) { files ->
                        pdf.combineWithOCR(files)
                    }
                }
            }

            println("Saved: $outFile")
        }
    }
}
