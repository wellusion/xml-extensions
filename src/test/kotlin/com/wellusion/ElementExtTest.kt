package com.wellusion

import org.junit.Assert.assertEquals
import org.junit.Test

class ElementExtTest : BaseTest() {

    @Test
    fun testCreateDocumentFromString() {
        val document = ElementExt.createDocument(testDocument)
        assertEquals(document.firstChild.localName, "testXmlDocument")
    }

    @Test
    fun testCreateDocumentFromInputStream() {
        val document = getResourceAsStream(testDocumentPath).use { inputStream ->
            ElementExt.createDocument(inputStream)
        }
        assertEquals(document.firstChild.localName, "testXmlDocument")
    }

    @Test
    fun testCreateSchema() {
        ElementExt.createSchema(testSchema)
    }
}