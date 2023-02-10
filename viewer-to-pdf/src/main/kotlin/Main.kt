import PdfHandler.Companion.addImagePage
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.element.Image
import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    SSLManager.ignoreSSL()


    val url = readln().trim()
//    val url = "https://online.anyflip.com/sqtpd/uree/files/mobile/" //FIXME Temp path

    val out = File(Paths.get("out").toAbsolutePath().toString())
    println(out.absolutePath)
    if (out.exists())
        out.deleteRecursively()
    out.mkdir()

    val pdf = PdfHandler("out/podr.pdf")
    pdf.use({ System.err.println("File error: $it") }) { document ->
        Downloader.iteratePages { page ->
            Downloader.download("$url$page.jpg")?.let {
                val img = Image(ImageDataFactory.createJpeg(it))
                document.addImagePage(img)
            }
        }
    }

//    try {
//        val pdf = PdfDocument(PdfWriter("out/podr.pdf"))
//        val document = Document(pdf)
//        document.setMargins(0f, 0f, 0f, 0f)
//
//        var page = 1
//        do {
//            val result = Downloader.download("$url$page.jpg")?.let {
//                val img = Image(ImageDataFactory.createJpeg(it))
//                pdf.addNewPage(PageSize(img.imageWidth, img.imageHeight))
//                document.add(img)
//            }
//            if (result != null)
//                print("\rDownloaded: ${page - 1} pages...")
//            page++
//        } while (result != null)
//        println("\nDone")
//
//        document.close()
//    } catch (e: FileNotFoundException) {
//        System.err.println("File error: ${e.message}")
//    }
}