package wellusion

import org.junit.Assert
import org.junit.Test

class ElementTest : BaseTest() {

    @Test
    fun getChildElement() {
        val document = DocumentExt.createDocument(testDocument)
        var element = document.documentElement.ext.getChildElement("child4")
        assert(element.textContent == "child4-value")

        Assert.assertThrows(KotlinNullPointerException::class.java) {
            document.documentElement.ext.getChildElement("notExistingChild")
        }

        element = document.documentElement.ext.getChildElement("child7")
        assert(element.textContent == "child7-value")
    }

    @Test
    fun getChildElementIfExist() {
        val document = DocumentExt.createDocument(testDocument)
        var element = document.documentElement.ext.getChildElementIfExist("child4")
        Assert.assertNotNull(element)
        assert(element!!.textContent == "child4-value")

        element = document.documentElement.ext.getChildElementIfExist("notExistingChild")
        Assert.assertNull(element)

        element = document.documentElement.ext.getChildElementIfExist("child7")
        Assert.assertNotNull(element)
        assert(element!!.textContent == "child7-value")
    }

    @Test
    fun getAllChildElementsByName() {
        val document = DocumentExt.createDocument(testDocument)
        Assert.assertEquals(1, document.documentElement.ext.getAllChildElements("child4").size)
        Assert.assertEquals(0, document.documentElement.ext.getAllChildElements("child6-1").size)
        val child6 = document.documentElement.ext.getChildElement("child6")
        Assert.assertEquals(0, child6.ext.getAllChildElements("child5").size)
    }

    @Test
    fun getAllChildElements() {
        val document = DocumentExt.createDocument(testDocument)
        Assert.assertEquals(8, document.documentElement.ext.getAllChildElements().size)
    }

    @Test
    fun setValue() {
        val document = DocumentExt.createDocument(testDocument)
        val someValue = "someValue"
        val child1 = document.documentElement.ext.getChildElement("child1")
        child1.ext.setValue(someValue)
        var elements = document.documentElement.getElementsByTagName("child1")
        Assert.assertEquals(1, elements.length)
        Assert.assertEquals(someValue, elements.item(0).textContent)

        val child7 = document.documentElement.ext.getChildElement("child7")
        child7.ext.setValue(someValue)
        elements = document.documentElement.getElementsByTagNameNS("*", "child7")
        Assert.assertEquals(1, elements.length)
        Assert.assertEquals(someValue, elements.item(0).textContent)
    }

    @Test
    fun getValue() {
        val document = DocumentExt.createDocument(testDocument)
        val child4 = document.documentElement.ext.getChildElement("child4")
        Assert.assertEquals("child4-value", child4.ext.getValue())
    }

    @Test
    fun hasChildElements() {
        val document = DocumentExt.createDocument(testDocument)
        val child1 = document.documentElement.ext.getChildElement("child1")
        Assert.assertFalse(child1.ext.hasChildElements())

        val child6 = document.documentElement.ext.getChildElement("child6")
        Assert.assertTrue(child6.ext.hasChildElements())
    }

    @Test
    fun getElementByXpath() {
        val document = DocumentExt.createDocument(testDocument)
        val element = document.documentElement.ext.getElementByXpath("/testXmlDocument/*[local-name()='child7']")
        Assert.assertTrue(element.textContent == "child7-value")

        Assert.assertThrows(Exception::class.java) {
            document.documentElement.ext.getElementByXpath("/testXmlDocument1")
        }
    }

    @Test
    fun getElementByXpathIfExist() {
        val document = DocumentExt.createDocument(testDocument)
        val element = document.documentElement.ext.getElementByXpathIfExist("/testXmlDocument/*[local-name()='child7']")
        Assert.assertNotNull(element)
        Assert.assertTrue(element!!.textContent == "child7-value")

        Assert.assertNull(document.documentElement.ext.getElementByXpathIfExist("/testXmlDocument1"))
    }

    @Test
    fun getAllElementsByXpath() {
        val document = DocumentExt.createDocument(testDocument)
        var elements = document.documentElement.ext.getAllElementsByXpath("/testXmlDocument/*[local-name()='child6']/*")
        Assert.assertEquals(5, elements.size)

        elements = document.documentElement.ext.getAllElementsByXpath("/testXmlDocument1/*")
        Assert.assertEquals(0, elements.size)
    }

    @Test
    fun hasElementsByXpath() {
        val document = DocumentExt.createDocument(testDocument)
        Assert.assertTrue(document.documentElement.ext.hasElementsByXpath("/testXmlDocument/*[local-name()='child7']"))
        Assert.assertFalse(document.documentElement.ext.hasElementsByXpath("/testXmlDocument1"))
    }

    @Test
    fun addChildElement() {
        val document = DocumentExt.createDocument(testDocument)
        document.documentElement.ext.addChildElement("newNode", "newNodeValue")
        var element = document.documentElement.ext.getChildElement("newNode")
        Assert.assertEquals("newNodeValue", element.textContent)

        element = document.documentElement.ext.getChildElement("child6")
        element.ext.addChildElement("newNode1", "newNode1Value")
        Assert.assertEquals("newNode1Value", element.ext.getChildElement("newNode1").ext.getValue())

        document.documentElement.ext.addChildElement("newNode2", "newNode2Value", "someNameSpace")
        element = document.documentElement.ext.getChildElement("newNode2")
        Assert.assertEquals("newNode2Value", element.textContent)
    }

    @Test
    fun addChildElementWithDisabledEscaping() {
        var document = DocumentExt.createDocument(testDocument)
        var child1 = document.documentElement.ext.getChildElement("child1")
        child1.ext.addChildElement("newNode", "newNode&Value")

        // Without disabled escaping symbol "&" is replaced by "&amp;"
        Assert.assertEquals(
            "<child1><newNode>newNode&amp;Value</newNode></child1>",
            child1.ext.toString().replace("[\n\r]".toRegex(), "")
        )

        document = DocumentExt.createDocument(testDocument)
        child1 = document.documentElement.ext.getChildElement("child1")
        child1.ext.addChildElementWithDisabledEscaping("newNode", "&", "newNode&Value")

        // And with disabled escaping symbol the "&" are remained "&"
        Assert.assertEquals(
            "<child1><newNode>newNode&Value</newNode></child1>",
            child1.ext.toString().replace("[\n\r]".toRegex(), "")
        )
    }

    @Test
    fun addCloneChildElement() {
        val document = DocumentExt.createDocument(testDocument)
        val newElement = document.documentElement.ext.addChildElement("newElement")

        val newDocument = DocumentExt.createDocument(testDocument)
        val child1 = newDocument.documentElement.ext.getChildElement("child1")
        child1.ext.addCloneChildElement(newElement)

        Assert.assertEquals("<child1><newElement/></child1>", child1.ext.toString().replace("[\n\r]".toRegex(), ""))
    }

    @Test
    fun getChildElementByAttr() {
        val document = DocumentExt.createDocument(testDocument)
        val child2 = document.documentElement.ext.getChildElementByAttr("child2-attr1", "child2-attr1-value")
        Assert.assertEquals(child2.ext.getAttrValue("child2-attr1"), "child2-attr1-value")

        Assert.assertThrows(Exception::class.java) {
            document.documentElement.ext.getChildElementByAttr("child2-attr1", "notExistValue")
        }
    }

    @Test
    fun getChildElementByAttrIfExist() {
        val document = DocumentExt.createDocument(testDocument)
        val child2 = document.documentElement.ext.getChildElementByAttrIfExist("child2-attr1", "child2-attr1-value")
        Assert.assertNotNull(child2)
        Assert.assertEquals(child2!!.ext.getAttrValue("child2-attr1"), "child2-attr1-value")

        val notExistElement = document.documentElement.ext.getChildElementByAttrIfExist("child2-attr1", "notExistValue")
        Assert.assertNull(notExistElement)
    }

    @Test
    fun getAllChildElementsByAttr() {
        val document = DocumentExt.createDocument(testDocument)
        Assert.assertEquals(2, document.documentElement.ext.getAllChildElementsByAttr("commonAttr", "commonAttrValue").size)
    }

    @Test
    fun getAllChildElementsByExistAttr() {
        val document = DocumentExt.createDocument(testDocument)
        val child6 = document.documentElement.ext.getChildElement("child6")
        Assert.assertEquals(1, child6.ext.getAllChildElementsByExistAttr("child6-2-attr1").size)
        Assert.assertEquals(0, child6.ext.getAllChildElementsByExistAttr("child6-attr1").size)
        Assert.assertEquals(0, child6.ext.getAllChildElementsByExistAttr("child2-attr1").size)
        Assert.assertEquals(0, document.documentElement.ext.getAllChildElementsByExistAttr("child6-2-attr1").size)
    }

    @Test
    fun getAttr() {
        val document = DocumentExt.createDocument(testDocument)
        val child3 = document.documentElement.ext.getChildElement("child3")
        val attr = child3.ext.getAttr("child3-attr1")
        Assert.assertNotNull(attr)
        Assert.assertEquals("child3-attr1-value", attr!!.nodeValue)
        Assert.assertNull(child3.ext.getAttr("notExistAttr"))
    }

    @Test
    fun getAttrValue() {
        val document = DocumentExt.createDocument(testDocument)
        val child3 = document.documentElement.ext.getChildElement("child3")
        Assert.assertEquals("child3-attr1-value", child3.ext.getAttrValue("child3-attr1"))
        Assert.assertNull(child3.ext.getAttrValue("notExistAttr"))
    }

    @Test
    fun toStringTest() {
        val document = DocumentExt.createDocument(testDocument)
        val sDocument = document.documentElement.ext.toString()

        // The strings sDocument and testDocument don't compare here because a document as a string after
        // transformations may have mixed nodes or attributes.
        assert(sDocument.contains("child8|child1|child4-value".toRegex()))
    }

    @Test
    fun schemaValidation() {
        val document = DocumentExt.createDocument(testDocumentNoNs)
        val schema = SchemaExt.createSchema(testSchemaNoNs)
        val wrongSchema = SchemaExt.createSchema(testSchemaSub2, testSchemaSub1, testSchema)

        Assert.assertTrue(document.documentElement.ext.schemaValidation(schema))
        Assert.assertFalse(document.documentElement.ext.schemaValidation(wrongSchema))
        Assert.assertThrows(Exception::class.java) {
            document.documentElement.ext.schemaValidation(wrongSchema, true)
        }
    }

    @Test
    fun elementBypass() {
        val document = DocumentExt.createDocument(testDocument)
        val dElement = document.documentElement!!
        dElement.ext.elementBypass { element ->
            if (element.childNodes.ext.toElementList().isEmpty()) {
                element.textContent = "replacedValue"
            }
        }
        Assert.assertEquals("replacedValue", dElement.ext.getChildElement("child4").textContent)
        Assert.assertEquals(
            "replacedValue",
            dElement.ext.getElementByXpath("/testXmlDocument//*[local-name()='child6-1']").textContent
        )
    }

    @Test
    fun removeChildElement() {
        val document = DocumentExt.createDocument(testDocument)
        val dElement = document.documentElement!!
        dElement.ext.removeChildElement("child4")
        Assert.assertNull(dElement.ext.getChildElementIfExist("child4"))
    }
}