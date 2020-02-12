package wellusion

import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerConfigurationException
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.validation.Schema
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

private val LOG = LoggerFactory.getLogger(Element::class.java)

private fun Element.innerFindElementByName(name: String): Element? {
    var targetElement: Element? = null
    val elementList = this.childNodes
    for (i in 0 until elementList.length) {
        val currentElement = elementList.item(i)

        // Search in an element is performed with a prefix of a namespace. Remove it because it changes.
        if (name == currentElement.nodeName.substringAfter(":")) {
            targetElement = currentElement as Element
            break
        }
    }

    if (targetElement == null) {
        LOG.warn("Xml element by name: $name not found.")
    }
    return targetElement
}

/**
 * Find nested single-level element by its name without a given namespace
 *
 * @param name The name of element for search
 * @return The found element.
 * @throws NullPointerException if no element was found.
 */
fun Element.findElementByName(name: String): Element {
    return innerFindElementByName(name)!!
}

/**
 * Find nested single-level element by its name without a given namespace
 *
 * @param name - The name of element for search
 * @return The found element or null.
 */
fun Element.findElementByNameIfExist(name: String): Element? {
    return innerFindElementByName(name)
}

/**
 * Find nested single-level element by its name without a given namespace and set specified value.
 *
 * @param name The name of an element for which it is needed to set the value.
 * @param value Set value
 * @return The element with specified name
 * @throws NullPointerException if no element was found.
 * */
fun Element.setValueToElement(name: String, value: String): Element {
    return this.setValueToElementIfExist(name, value)!!
}

/**
 * Find nested single-level element by its name without a given namespace and set specified value.
 *
 * @param name The name of an element for which it is needed to set the value.
 * @param value Set value
 * @return The element with specified name or null
 * */
fun Element.setValueToElementIfExist(name: String, value: String): Element? {
    val childElement = this.findElementByNameIfExist(name)
    childElement?.textContent = value
    return childElement
}

/**
 * Represent the element as a string
 *
 * @return The element as a string
 */
fun Element.asString(): String {
    val stringWriter = StringWriter()
    val transformer = getTransformer()
    if (this is Document) {
        transformer.addNoXmlDeclaration()
    }
    transformer.transform(DOMSource(this), StreamResult(stringWriter))
    return stringWriter.toString()
}

private fun getTransformer(): Transformer {
    val transformerFactory = TransformerFactory.newInstance()
    val transformer = transformerFactory.newTransformer()
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
    transformer.setOutputProperty(OutputKeys.METHOD, "xml")
    transformer.setOutputProperty(OutputKeys.INDENT, "yes")
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
    transformer.setOutputProperty(OutputKeys.VERSION, "1.0")

    return transformer
}

// If a document is used, then property OMIT_XML_DECLARATION have to have value "no"
private fun Transformer.addNoXmlDeclaration(): Transformer {
    this.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
    return this
}

/**
 * Check the document for schema compliance
 *
 * @param schema Schema for checking
 * @return Whether the document is valid or not.
 */
fun Element.schemaValidation(schema: Schema): Boolean {

    val validator = schema.newValidator()
    try {
        validator.validate(DOMSource(this))
    } catch (e: Exception) {
        LOG.error("Schema validation error: ${e.message}. Stacktrace:${e.stackTrace}")
        return false
    }
    return true
}

/**
 * Find node by xPath
 *
 * @param sXpath XPath string to search for an element
 * @return The found element
 * @throws NullPointerException if no element was found.
 */
fun Element.findElementByXpath(sXpath: String): Element {
    val elements = this.findAllElementByXpath(sXpath)
    if (elements.isEmpty()) {
        throw Exception("Xml elements by xPath: $sXpath not found.")
    }
    moreThenOneElementWarning(elements)
    return elements[0]
}

/**
 * Find node by xPath
 *
 * @param sXpath XPath string to search for an element
 * @return The found element or null
 */
fun Element.findElementByXpathIfExist(sXpath: String): Element? {
    val elements = this.findAllElementByXpath(sXpath)
    return if (elements.isEmpty()) {
        null
    } else {
        moreThenOneElementWarning(elements)
        elements[0]
    }
}

private fun moreThenOneElementWarning(elements: List<Element>) {
    if (elements.size > 1) {
        LOG.warn("Found more than one element. The first one will be returned.")
    }
}

/**
 * Check an existence of an element by xPath
 *
 * @param sXpath XPath string to search for an element
 * @return Whether the element exists.
 */
fun Element.hasElementByXpath(sXpath: String): Boolean {
    val elements = this.findAllElementByXpath(sXpath)
    return elements.isNotEmpty()
}

/**
 * Find all nodes by xPath
 *
 * @param sXpath XPath string to search for elements
 * @return List of finding elements
 */
fun Element.findAllElementByXpath(sXpath: String): List<Element> {
    val xPathFactory = XPathFactory.newInstance()
    val xpath = xPathFactory.newXPath()
    val xPathExpression = xpath.compile(sXpath)
    val nodeList = xPathExpression.evaluate(this, XPathConstants.NODESET) as NodeList

    val filterElementList = ArrayList<Element>()
    for (i in 0 until nodeList.length) {
        val node = nodeList.item(i)
        if (node !is Element) {
            LOG.info("Node with name \"${node.localName}\" isn't an Element.")
            continue
        }
        filterElementList.add(node)
    }

    if (filterElementList.size == 0) {
        LOG.info("Xml elements by xPath: $sXpath not found.")
    }
    return filterElementList
}

/**
 * Add new nested single-level node
 *
 * @param name The name of creating node
 * @param value The text value of creating node
 * @param namespace The namespace of creating node
 * @return Created node
 * */
fun Element.add(name: String, value: String? = null, namespace: String? = null): Element {

    val element = if (namespace == null) {
        this.ownerDocument.createElement(name)
    } else {
        this.ownerDocument.createElementNS(namespace, name)
    }
    if (value != null) {
        element.textContent = value
    }

    return this.appendChild(element) as Element
}

/**
 * Add a new single-level node with disabled escaping of the specified symbol.
 * For example symbol "&" in node value will be escaped by "&amp;" if escaping don't disable.
 *
 * @param name The name of creating node
 * @param escapedSymbol The symbol that needs to be escaped
 * @param value The text value of creating node
 * @param namespace The namespace of creating node
 * */
fun Element.addWithDisabledEscaping(
    name: String, escapedSymbol: String, value: String? = null, namespace: String? = null
) {
    val disableEscaping =
        this.ownerDocument.createProcessingInstruction(StreamResult.PI_DISABLE_OUTPUT_ESCAPING, escapedSymbol)
    this.appendChild(disableEscaping)
    this.add(name, value, namespace)
    val enableEscaping =
        this.ownerDocument.createProcessingInstruction(StreamResult.PI_ENABLE_OUTPUT_ESCAPING, escapedSymbol)
    this.appendChild(enableEscaping)
}

/**
 * Add a clone of given node
 *
 * @param element Appended node
 * @return The link to an appended node
 * */
fun Element.addClone(element: Element): Element {
    val clone = this.ownerDocument.importNode(element, true) as Element
    this.appendChild(clone)
    return clone
}