package wellusion

import org.junit.Assert
import org.junit.Test
import java.io.File
class DocumentTest : BaseTest() {

    @Test
    fun documentToFile() {
        var file: File? = null
        try {
            val document = DocumentExt.createDocument(testDocumentNoNs)
            file = File.createTempFile("tmp12345", null)
            document.ext.toFile(file)
        } finally {
            file?.delete()
        }
    }

    @Test
    fun documentToInputStream() {
        val document = DocumentExt.createDocument(testDocumentNoNs)
        document.ext.toInputStream().use { inputStream ->
            val checkingDocument = DocumentExt.createDocument(inputStream)
            val child4 = checkingDocument.documentElement.ext.getChildElement("child4")
            Assert.assertEquals("child4-value", child4.textContent)
        }
    }

    @Test
    fun documentToByteArray() {
        val document = DocumentExt.createDocument(testDocumentNoNs)
        val bDocument = document.ext.toByteArray()
        val checkingDocument = DocumentExt.createDocument(bDocument)
        val child4 = checkingDocument.documentElement.ext.getChildElement("child4")
        Assert.assertEquals("child4-value", child4.textContent)
    }

    @Test
    fun documentToString() {
        val document = DocumentExt.createDocument(testDocument)
        val sDocument = document.ext.toString()

        // The strings sDocument and testDocument don't compare here because a document as a string after
        // transformations may have mixed nodes or attributes.
        assert(sDocument.contains("child8|child1|child4-value".toRegex()))
    }

}