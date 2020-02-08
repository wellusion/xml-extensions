package wellusion

import org.apache.commons.io.IOUtils
import org.junit.Assert
import java.io.InputStream
import java.nio.charset.StandardCharsets

open class BaseTest {

    companion object {
        const val testDocumentPath = "testDocument.xml"
        const val testDocumentNoNsPath = "testDocumentNoNs.xml"
        const val testSchemaNoNsPath = "testSchemaNoNs.xml"

        val testDocument = getResourceAsString(testDocumentPath)
        val testDocumentNoNs = getResourceAsString(testDocumentNoNsPath)
        val testSchemaNoNs = getResourceAsString(testSchemaNoNsPath)

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