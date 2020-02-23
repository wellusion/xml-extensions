package wellusion

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class DocumentExtTest : BaseTest() {

    @Test
    fun createDocumentFromString() {
        val document = DocumentExt.createDocument(testDocument)
        assertEquals(document.firstChild.localName, "testXmlDocument")
    }

    @Test
    fun createDocumentFromInputStream() {
        val document = getResourceAsStream(testDocumentPath).use { inputStream ->
            DocumentExt.createDocument(inputStream)
        }
        assertEquals(document.firstChild.localName, "testXmlDocument")
    }

    @Test
    fun getDocumentBuilder() {
        DocumentExt.getDocumentBuilder()
    }

    @Test
    fun writeDocumentToFile() {
        var file: File? = null
        try {
            val document = DocumentExt.createDocument(testDocumentNoNs)
            file = File.createTempFile("tmp12345", null)
            DocumentExt.writeDocumentToFile(file, document)

            val targetFile = File(file.path)
            val targetDocument = DocumentExt.getDocumentBuilder().parse(targetFile)
            val child4 = targetDocument.documentElement.findElementByName("child4")
            assertEquals("child4-value", child4.textContent)
        } finally {
            file?.delete()
        }
    }
}