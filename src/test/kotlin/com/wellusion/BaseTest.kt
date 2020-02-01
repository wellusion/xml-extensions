package com.wellusion

import org.apache.commons.io.IOUtils
import org.junit.Assert.assertNotNull
import java.io.InputStream
import java.nio.charset.StandardCharsets

open class BaseTest {

    protected val testDocumentPath = "commonTestDocument.xml"
    protected var testDocument = ""
        get() {
            if (field == "") {
                field = getResourceAsString(testDocumentPath)
            }
            return field
        }

    protected val testSchemaPath = "commonTestSchema.xml"
    protected var testSchema = ""
        get() {
            if (field == "") {
                field = getResourceAsString(testSchemaPath)
            }
            return field
        }

    private fun getResourceAsString(path: String): String {
        val classLoader: ClassLoader = this.javaClass.classLoader
        val resourceUrl = classLoader.getResource(path)
        assertNotNull(resourceUrl)
        return IOUtils.toString(resourceUrl, StandardCharsets.UTF_8)
    }

    protected fun getResourceAsStream(path: String): InputStream {
        val classLoader: ClassLoader = this.javaClass.classLoader
        val resource = classLoader.getResourceAsStream(path)
        assertNotNull(resource)
        return resource!!
    }
}