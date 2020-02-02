package com.wellusion

import org.junit.Assert.assertEquals
import org.junit.Test

class ElementExtTest : BaseTest() {

    @Test
    fun createDocumentFromString() {
        val document = ElementExt.createDocument(testDocument)
        assertEquals(document.firstChild.localName, "testXmlDocument")
    }

    @Test
    fun createDocumentFromInputStream() {
        val document = getResourceAsStream(testDocumentPath).use { inputStream ->
            ElementExt.createDocument(inputStream)
        }
        assertEquals(document.firstChild.localName, "testXmlDocument")
    }

    @Test
    fun createSchema() {
        ElementExt.createSchema(testSchema)
    }
}