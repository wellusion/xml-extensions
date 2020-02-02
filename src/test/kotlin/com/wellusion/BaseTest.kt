package com.wellusion

import org.apache.commons.io.IOUtils
import org.junit.Assert.assertNotNull
import java.io.InputStream
import java.nio.charset.StandardCharsets

open class BaseTest {

    companion object {
        const val testDocumentPath = "commonTestDocument.xml"
        val testDocument = getResourceAsString(testDocumentPath)

        const val testSchemaPath = "commonTestSchema.xml"
        val testSchema = getResourceAsString(testSchemaPath)

        private fun getResourceAsString(path: String): String {
            val classLoader: ClassLoader = this::class.java.classLoader
            val resourceUrl = classLoader.getResource(path)
            assertNotNull(resourceUrl)
            return IOUtils.toString(resourceUrl, StandardCharsets.UTF_8)
        }

        fun getResourceAsStream(path: String): InputStream {
            val classLoader: ClassLoader = this::class.java.classLoader
            val resource = classLoader.getResourceAsStream(path)
            assertNotNull(resource)
            return resource!!
        }
    }
}