package wellusion

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

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
    fun createDocumentFromByteArray() {
        val document = DocumentExt.createDocument(testDocument)
        val bDocument = ByteArrayOutputStream().use { byteArrayOutputStream ->
            val result = StreamResult(byteArrayOutputStream)
            val source = DOMSource(document)
            TransformerExt.createXmlTransformer().transform(source, result)
            byteArrayOutputStream.toByteArray()
        }
        val createdDocument = DocumentExt.createDocument(bDocument)
        assertEquals(createdDocument.documentElement.localName, document.documentElement.localName)
    }

    @Test
    fun createDocumentFromFile() {
        var file: File? = null
        try {
            val document = DocumentExt.createDocument(testDocumentNoNs)
            file = File.createTempFile("tmp12345", null)
            document.ext.toFile(file)

            val targetFile = File(file.path)
            val targetDocument = DocumentExt.createDocument(targetFile)
            val child4 = targetDocument.documentElement.ext.getChildElement("child4")
            assertEquals("child4-value", child4.textContent)
        } finally {
            file?.delete()
        }
    }

    @Test
    fun getDocumentBuilder() {
        DocumentExt.createDocumentBuilder()
    }

}