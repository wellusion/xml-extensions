package wellusion

import org.junit.Assert.assertEquals
import org.junit.Test

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
}