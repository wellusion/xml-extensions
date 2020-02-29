package wellusion

import org.junit.Assert
import org.junit.Test

class ElementTest : BaseTest() {

    @Test
    fun findElementByName() {
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
    fun findElementByNameIfExist() {
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
    fun setValueToElement() {
        val document = DocumentExt.createDocument(testDocument)
        val someValue = "someValue"

        document.documentElement.ext.setValue("child1", someValue)
        var elements = document.documentElement.getElementsByTagName("child1")
        Assert.assertEquals(1, elements.length)
        Assert.assertEquals(someValue, elements.item(0).textContent)

        document.documentElement.ext.setValue("child7", someValue)
        elements = document.documentElement.getElementsByTagNameNS("*", "child7")
        Assert.assertEquals(1, elements.length)
        Assert.assertEquals(someValue, elements.item(0).textContent)

        Assert.assertThrows(java.lang.Exception::class.java) {
            document.documentElement.ext.setValue("notExistChild", someValue)
        }
    }

    @Test
    fun setValueToElementIfExist() {
        val document = DocumentExt.createDocument(testDocument)
        val someValue = "someValue"

        document.documentElement.ext.setValueToElementIfExist("child1", someValue)
        val elements = document.documentElement.getElementsByTagName("child1")
        Assert.assertEquals(1, elements.length)
        Assert.assertEquals(someValue, elements.item(0).textContent)

        Assert.assertEquals(null, document.documentElement.ext.setValueToElementIfExist("notExistChild", someValue))
    }

    @Test
    fun asString() {
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
    }

    @Test
    fun findElementByXpath() {
        val document = DocumentExt.createDocument(testDocument)
        val element = document.documentElement.ext.getElementByXpath("/testXmlDocument/*[local-name()='child7']")
        Assert.assertTrue(element.textContent == "child7-value")

        Assert.assertThrows(Exception::class.java) {
            document.documentElement.ext.getElementByXpath("/testXmlDocument1")
        }
    }

    @Test
    fun findElementByXpathIfExist() {
        val document = DocumentExt.createDocument(testDocument)
        val element = document.documentElement.ext.getElementByXpathIfExist("/testXmlDocument/*[local-name()='child7']")
        Assert.assertNotNull(element)
        Assert.assertTrue(element!!.textContent == "child7-value")

        Assert.assertNull(document.documentElement.ext.getElementByXpathIfExist("/testXmlDocument1"))
    }

    @Test
    fun hasElementByXpath() {
        val document = DocumentExt.createDocument(testDocument)
        Assert.assertTrue(document.documentElement.ext.hasElementsByXpath("/testXmlDocument/*[local-name()='child7']"))
        Assert.assertFalse(document.documentElement.ext.hasElementsByXpath("/testXmlDocument1"))
    }

    @Test
    fun findAllElementByXpath() {
        val document = DocumentExt.createDocument(testDocument)
        var elements = document.documentElement.ext.getAllElementsByXpath("/testXmlDocument/*[local-name()='child6']/*")
        Assert.assertEquals(5, elements.size)

        elements = document.documentElement.ext.getAllElementsByXpath("/testXmlDocument1/*")
        Assert.assertEquals(0, elements.size)
    }

    @Test
    fun add() {
        val document = DocumentExt.createDocument(testDocument)
        document.documentElement.ext.addChildElement("newNode", "newNodeValue")
        var element = document.documentElement.ext.getChildElement("newNode")
        Assert.assertEquals("newNodeValue", element.textContent)

        document.documentElement.ext.addChildElement("newNode1", "newNode1Value", "someNameSpace")
        element = document.documentElement.ext.getChildElement("newNode1")
        Assert.assertEquals("newNode1Value", element.textContent)
    }

    @Test
    fun addWithDisabledEscaping() {
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
        child1.ext.addChildElementWithDisabledEscaping(
            "newNode", "&",
            "newNode&Value"
        )

        // And with disabled escaping symbol the "&" are remained "&"
        Assert.assertEquals(
            "<child1><newNode>newNode&Value</newNode></child1>",
            child1.ext.toString().replace("[\n\r]".toRegex(), "")
        )
    }

    @Test
    fun addClone() {
        val document = DocumentExt.createDocument(testDocument)
        val newElement = document.documentElement.ext.addChildElement("newElement")

        val newDocument = DocumentExt.createDocument(testDocument)
        val child1 = newDocument.documentElement.ext.getChildElement("child1")
        child1.ext.addCloneChildElement(newElement)

        Assert.assertEquals("<child1><newElement/></child1>", child1.ext.toString().replace("[\n\r]".toRegex(), ""))
    }

    @Test
    fun findAllElementsByAttr() {
        val document = DocumentExt.createDocument(testDocument)
        val child6 = document.documentElement.ext.getChildElement("child6")
        Assert.assertEquals(1, child6.ext.getAllElementsByExistAttr("child6-2-attr1").size)
        Assert.assertEquals(0, child6.ext.getAllElementsByExistAttr("child6-attr1").size)
        Assert.assertEquals(0, child6.ext.getAllElementsByExistAttr("child2-attr1").size)
        Assert.assertEquals(0, document.documentElement.ext.getAllElementsByExistAttr("child6-2-attr1").size)
    }

    @Test
    fun findAllElementsByName() {
        val document = DocumentExt.createDocument(testDocument)
        Assert.assertEquals(1, document.documentElement.ext.getAllChildElements("child4").size)
        Assert.assertEquals(0, document.documentElement.ext.getAllChildElements("child6-1").size)
        val child6 = document.documentElement.ext.getChildElement("child6")
        Assert.assertEquals(0, child6.ext.getAllChildElements("child5").size)
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
    fun nodeBypass() {
        val document = DocumentExt.createDocument(testDocument)
        val dElement = document.documentElement!!
        dElement.ext.elementBypass { element ->
            if (element.childNodes.ext.toElementList().isEmpty()) {
                element.textContent = "replacedValue"
            }
        }
        Assert.assertEquals("replacedValue", dElement.ext.getChildElement("child4").textContent)
        Assert.assertEquals("replacedValue", dElement.ext.getElementByXpath("/testXmlDocument//*[local-name()='child6-1']").textContent)
    }

    @Test
    fun remove() {
        val document = DocumentExt.createDocument(testDocument)
        val dElement = document.documentElement!!
        val child4 = dElement.ext.getChildElement("child4")
        child4.ext.removeChildElement()
        Assert.assertNull(dElement.ext.getChildElementIfExist("child4"))
    }
}