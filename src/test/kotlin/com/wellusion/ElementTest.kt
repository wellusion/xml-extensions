package com.wellusion

import org.junit.Test

class ElementTest: BaseTest() {

    @Test
    fun findElementByName() {
        val document = ElementExt.createDocument(testDocument)
        val element = document.documentElement.findElementByName("child4")
        assert(element.textContent == "child4-value")
    }

    @Test
    fun findElementByNameIfExist() {
    }

    @Test
    fun setValueToElement() {
    }

    @Test
    fun asString() {
    }

    @Test
    fun schemaValidation() {
    }

    @Test
    fun findElementByXpath() {
    }

    @Test
    fun findElementByXpathIfExist() {
    }

    @Test
    fun hasElementByXpath() {
    }

    @Test
    fun findAllElementByXpath() {
    }

    @Test
    fun add() {
    }

    @Test
    fun addWithDisabledEscaping() {
    }

    @Test
    fun addClone() {
    }

    @Test
    fun asList() {
    }
}