package wellusion

import org.apache.commons.io.IOUtils
import org.junit.Assert
import java.io.InputStream
import java.nio.charset.StandardCharsets

open class BaseTest {

    companion object {
        const val testDocumentPath = "testDocument.xml"
        private const val testDocumentNoNsPath = "testDocumentNoNs.xml"
        private const val testSchemaNoNsPath = "testSchemaNoNs.xsd"
        private const val testSchemaPath = "testSchema.xsd"
        private const val testSchemaSub1Path = "testSchemaSub1.xsd"
        private const val testSchemaSub2Path = "testSchemaSub2.xsd"
        private const val testXsltPath = "XsltTemplate.xsl"

        val testDocument = getResourceAsString(testDocumentPath)
        val testDocumentNoNs = getResourceAsString(testDocumentNoNsPath)
        val testSchemaNoNs = getResourceAsString(testSchemaNoNsPath)
        val testSchema = getResourceAsString(testSchemaPath)
        val testSchemaSub1 = getResourceAsString(testSchemaSub1Path)
        val testSchemaSub2 = getResourceAsString(testSchemaSub2Path)
        val testXslt = getResourceAsString(testXsltPath)

        private fun getResourceAsString(path: String): String {
            val classLoader: ClassLoader = this::class.java.classLoader
            val resourceUrl = classLoader.getResource(path)
            Assert.assertNotNull(resourceUrl)
            return IOUtils.toString(resourceUrl, StandardCharsets.UTF_8)
        }

        fun getResourceAsStream(path: String): InputStream {
            val classLoader: ClassLoader = this::class.java.classLoader
            val resource = classLoader.getResourceAsStream(path)
            Assert.assertNotNull(resource)
            return resource!!
        }
    }
}