import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.pdfocr.OcrPdfCreator
import com.itextpdf.pdfocr.tesseract4.Tesseract4LibOcrEngine
import com.itextpdf.pdfocr.tesseract4.Tesseract4OcrEngineProperties
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Paths

class PdfHandler(outPath: String) {

    companion object {
        val ocrEngineProps = Tesseract4OcrEngineProperties()

        //FIXME Different OCR data folder
        fun getTesseractDataFolder(): File = Paths.get(
            Paths.get("").toAbsolutePath().toString(),
            "/src/main/resources/ocr"
        ).toFile()
    }

    private val pdf = PdfDocument(PdfWriter(outPath))
    private val ocrReader = Tesseract4LibOcrEngine(ocrEngineProps)

    fun use(printError: Boolean = true, block: (document: Document) -> Unit) {
        val document = Document(pdf)
        document.setMargins(0f, 0f, 0f, 0f)

        try {
            block(document)
            document.close()
        } catch (e: FileNotFoundException) {
            if (printError) {
                val message = e.message ?: "File not found"
                System.err.println("File error: $message")
            }
        }
    }

    fun addImagePage(document: Document, image: Image) {
        pdf.addNewPage(PageSize(image.imageWidth, image.imageHeight))
        document.add(image)
    }

    fun combineWithOCR(list: List<File>, printError: Boolean = true) {
        ocrEngineProps.pathToTessData = getTesseractDataFolder()

        val pdfCreator = OcrPdfCreator(ocrReader)
        try {
            pdfCreator.createPdf(list, pdf.writer).close()
        } catch (e: Exception) {
            if (printError) {
                val message = "OCR error" +
                        if (e.message != null) ": ${e.message}"
                        else ""
                System.err.println(message)
            }
        }
    }
}

