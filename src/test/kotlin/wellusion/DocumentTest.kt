package wellusion

import org.junit.Test
import java.io.File
class DocumentTest : BaseTest() {

    @Test
    fun writeDocumentToFile() {
        var file: File? = null
        try {
            val document = DocumentExt.createDocument(testDocumentNoNs)
            file = File.createTempFile("tmp12345", null)
            document.ext.toFile(file)
        } finally {
            file?.delete()
        }
    }

}