package wellusion

import org.junit.Assert
import org.junit.Test
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.util.stream.IntStream

class NodeListTest : BaseTest() {

    @Test
    fun asList() {
        val document = DocumentExt.createDocument(testDocument)
        val nodeList: NodeList? = document.documentElement.getElementsByTagName("child6")?.item(0)?.childNodes
        Assert.assertNotNull(nodeList)

        val elementsCount =
            IntStream.range(0, nodeList!!.length).filter { nodeList.item(it) is Element }.count().toInt()

        Assert.assertEquals(elementsCount, nodeList.asList().size)
    }
}