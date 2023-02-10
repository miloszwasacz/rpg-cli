import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import java.io.FileNotFoundException

class PdfHandler(outPath: String) {

    private val pdf = PdfDocument(PdfWriter(outPath))

    fun use(error: (message: String) -> Unit, block: (document: Document) -> Unit) {
        val document = Document(pdf)
        document.setMargins(0f, 0f, 0f, 0f)

        try {
            block(document)
            document.close()
        } catch (e: FileNotFoundException) {
            error(e.message ?: "File not found")
        }
    }

    companion object {
        fun Document.addImagePage(image: Image) {
            pdfDocument.addNewPage(PageSize(image.imageWidth, image.imageHeight))
            add(image)
        }
    }
}

