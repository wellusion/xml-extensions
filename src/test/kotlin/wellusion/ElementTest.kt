package wellusion

import org.junit.Assert
import org.junit.Test
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.util.stream.IntStream

class ElementTest : BaseTest() {

    @Test
    fun findElementByName() {
        val document = ElementExt.createDocument(testDocument)
        var element = document.documentElement.findElementByName("child4")
        assert(element.textContent == "child4-value")

        Assert.assertThrows(KotlinNullPointerException::class.java) {
            document.documentElement.findElementByName("notExistingChild")
        }

        element = document.documentElement.findElementByName("child7")
        assert(element.textContent == "child7-value")
    }

    @Test
    fun findElementByNameIfExist() {
        val document = ElementExt.createDocument(testDocument)
        var element = document.documentElement.findElementByNameIfExist("child4")
        Assert.assertNotNull(element)
        assert(element!!.textContent == "child4-value")

        element = document.documentElement.findElementByNameIfExist("notExistingChild")
        Assert.assertNull(element)

        element = document.documentElement.findElementByName("child7")
        assert(element.textContent == "child7-value")
    }

    @Test
    fun setValueToElement() {
        val document = ElementExt.createDocument(testDocument)
        val someValue = "someValue"

        document.documentElement.setValueToElement("child1", someValue)
        var elements = document.documentElement.getElementsByTagName("child1")
        Assert.assertEquals(1, elements.length)
        Assert.assertEquals(someValue, elements.item(0).textContent)

        document.documentElement.setValueToElement("child7", someValue)
        elements = document.documentElement.getElementsByTagNameNS("*", "child7")
        Assert.assertEquals(1, elements.length)
        Assert.assertEquals(someValue, elements.item(0).textContent)

        Assert.assertThrows(java.lang.Exception::class.java) {
            document.documentElement.setValueToElement("notExistChild", someValue)
        }
    }

    @Test
    fun setValueToElementIfExist() {
        val document = ElementExt.createDocument(testDocument)
        val someValue = "someValue"

        document.documentElement.setValueToElementIfExist("child1", someValue)
        val elements = document.documentElement.getElementsByTagName("child1")
        Assert.assertEquals(1, elements.length)
        Assert.assertEquals(someValue, elements.item(0).textContent)

        Assert.assertEquals(null, document.documentElement.setValueToElementIfExist("notExistChild", someValue))
    }

    @Test
    fun asString() {
        val document = ElementExt.createDocument(testDocument)
        val sDocument = document.documentElement.asString()

        // The strings sDocument and testDocument don't compare here because a document as a string after
        // transformations may have mixed nodes or attributes.
        assert(sDocument.contains("child8|child1|child4-value".toRegex()))
    }

    @Test
    fun schemaValidation() {
        val document = ElementExt.createDocument(testDocumentNoNs)
        val schema = ElementExt.createSchema(testSchemaNoNs)
        val wrongSchema = ElementExt.createSchema(testSchemaSub2, testSchemaSub1, testSchema)

        Assert.assertTrue(document.documentElement.schemaValidation(schema))
        Assert.assertFalse(document.documentElement.schemaValidation(wrongSchema))
    }

    @Test
    fun findElementByXpath() {
        val document = ElementExt.createDocument(testDocument)
        val element = document.documentElement.findElementByXpath("/testXmlDocument/*[local-name()='child7']")
        Assert.assertTrue(element.textContent == "child7-value")

        Assert.assertThrows(Exception::class.java) {
            document.documentElement.findElementByXpath("/testXmlDocument1")
        }
    }

    @Test
    fun findElementByXpathIfExist() {
        val document = ElementExt.createDocument(testDocument)
        val element = document.documentElement.findElementByXpathIfExist("/testXmlDocument/*[local-name()='child7']")
        Assert.assertNotNull(element)
        Assert.assertTrue(element!!.textContent == "child7-value")

        Assert.assertNull(document.documentElement.findElementByXpathIfExist("/testXmlDocument1"))
    }

    @Test
    fun hasElementByXpath() {
        val document = ElementExt.createDocument(testDocument)
        Assert.assertTrue(document.documentElement.hasElementByXpath("/testXmlDocument/*[local-name()='child7']"))
        Assert.assertFalse(document.documentElement.hasElementByXpath("/testXmlDocument1"))
    }

    @Test
    fun findAllElementByXpath() {
        val document = ElementExt.createDocument(testDocument)
        var elements = document.documentElement.findAllElementByXpath("/testXmlDocument/*[local-name()='child6']/*")
        Assert.assertEquals(5, elements.size)

        elements = document.documentElement.findAllElementByXpath("/testXmlDocument1/*")
        Assert.assertEquals(0, elements.size)
    }

    @Test
    fun add() {
        val document = ElementExt.createDocument(testDocument)
        document.documentElement.add("newNode", "newNodeValue")
        var element = document.documentElement.findElementByName("newNode")
        Assert.assertEquals("newNodeValue", element.textContent)

        document.documentElement.add("newNode1", "newNode1Value", "someNameSpace")
        element = document.documentElement.findElementByName("newNode1")
        Assert.assertEquals("newNode1Value", element.textContent)
    }

    @Test
    fun addWithDisabledEscaping() {
        var document = ElementExt.createDocument(testDocument)
        var child1 = document.documentElement.findElementByName("child1")
        child1.add("newNode", "newNode&Value")

        // Without disabled escaping symbol "&" is replaced by "&amp;"
        Assert.assertEquals(
            "<child1><newNode>newNode&amp;Value</newNode></child1>",
            child1.asString().replace("[\n\r]".toRegex(), "")
        )

        document = ElementExt.createDocument(testDocument)
        child1 = document.documentElement.findElementByName("child1")
        child1.addWithDisabledEscaping(
            "newNode", "&",
            "newNode&Value"
        )

        // And with disabled escaping symbol the "&" are remained "&"
        Assert.assertEquals(
            "<child1><newNode>newNode&Value</newNode></child1>",
            child1.asString().replace("[\n\r]".toRegex(), "")
        )
    }

    @Test
    fun addClone() {
        val document = ElementExt.createDocument(testDocument)
        val newElement = document.documentElement.add("newElement")

        val newDocument = ElementExt.createDocument(testDocument)
        val child1 = newDocument.documentElement.findElementByName("child1")
        child1.addClone(newElement)

        Assert.assertEquals("<child1><newElement/></child1>", child1.asString().replace("[\n\r]".toRegex(), ""))
    }

    @Test
    fun asList() {
        val document = ElementExt.createDocument(testDocument)
        val nodeList: NodeList? = document.documentElement.getElementsByTagName("child6")?.item(0)?.childNodes
        Assert.assertNotNull(nodeList)

        val elementsCount =
            IntStream.range(0, nodeList!!.length).filter { nodeList.item(it) is Element }.count().toInt()

        Assert.assertEquals(elementsCount, nodeList.asList().size)
    }
}