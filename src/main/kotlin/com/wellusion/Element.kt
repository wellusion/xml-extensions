package com.wellusion

import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.StringReader
import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors
import java.util.stream.IntStream
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerConfigurationException
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
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
        LOG.warn("Xml element by name: {} not found.", name)
    }
    return targetElement
}

/*
* Find nested single-level element by its name without a given namespace
* */
fun Element.findElementByName(name: String): Element {
    return innerFindElementByName(name)!!
}

fun Element.findElementByNameIfExist(name: String): Element? {
    return innerFindElementByName(name)
}

/*
* Find nested single-level element by its name without a given namespace and set specified value.
* */
fun Element.setValueToElement(name: String, value: String) {
    val childElement = this.findElementByName(name)
    childElement.textContent = value
}

/*
* Represent element as a string
* */
@Throws(TransformerException::class)
fun Element.asString(): String {
    val r = 1
    r.toString()
    val stringWriter = StringWriter()
    val transformer = getTransformer()
    if (this is Document) {
        transformer.addNoXmlDeclaration()
    }
    transformer.transform(DOMSource(this), StreamResult(stringWriter))
    return stringWriter.toString()
}

@Throws(TransformerConfigurationException::class)
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

/*
* Check the document for schema compliance
* */
@Throws(IOException::class, SAXException::class)
fun schemaValidation(schema: Schema, document: Document): Boolean {

    val validator = schema.newValidator()
    validator.validate(DOMSource(document))

    return true
}

/*
* Find node by xPath
* */
fun Element.findElementByXpath(sXpath: String): Element {
    val elements = this.findAllElementByXpath(sXpath)
    if (elements.isEmpty()) {
        throw Exception("Xml elements by xPath: $sXpath not found.")
    }
    return elements[0]
}

/*
* Find node by xPath if exist
* */
fun Element.findElementByXpathIfExist(sXpath: String): Element? {
    val elements = this.findAllElementByXpath(sXpath)
    return if (elements.isEmpty()) {
        null
    } else {
        elements[0]
    }
}

/*
* Check an existence of an element by xPath
* */
fun Element.hasElementByXpath(sXpath: String): Boolean {
    val elements = this.findAllElementByXpath(sXpath)
    return elements.isNotEmpty()
}

/*
* Find all nodes by xPath
* */
fun Element.findAllElementByXpath(sXpath: String): List<Element> {
    val xPathFactory = XPathFactory.newInstance()
    val xpath = xPathFactory.newXPath()
    val xPathExpression = xpath.compile(sXpath)
    val nodeList = xPathExpression.evaluate(this, XPathConstants.NODESET) as NodeList

    val filterElementList = ArrayList<Element>()
    for (i in 0 until nodeList.length) {
        val node = nodeList.item(i)
        filterElementList.add(node as Element)
    }

    if (filterElementList.size == 0) {
        LOG.warn("Xml elements by xPath: $sXpath not found.")
    }
    return filterElementList
}

/**
 * Add new nested single-level node
 *
 * @param name - The name of creating node
 * @param namespace - The namespace of creating node
 * @param value - The text value of creating node
 * */
fun Element.add(name: String, namespace: String? = null, value: String? = null): Element {

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
 * Add a new single-level node with disabling escaping of the specified symbol
 *
 * @param name - The name of creating node
 * @param namespace - The namespace of creating node
 * @param value - The text value of creating node
 * @param escapedSymbol - The symbol that needs to be escaped.
 * */
fun Element.addWithDisabledEscaping(name: String, namespace: String? = null, value: String? = null,
                                    escapedSymbol: String) {
    val disableEscaping =
        this.ownerDocument.createProcessingInstruction(StreamResult.PI_DISABLE_OUTPUT_ESCAPING, escapedSymbol)
    this.appendChild(disableEscaping)
    this.add(name, namespace, value)
    val enableEscaping =
        this.ownerDocument.createProcessingInstruction(StreamResult.PI_ENABLE_OUTPUT_ESCAPING, escapedSymbol)
    this.appendChild(enableEscaping)
}

/**
 * Add a clone of given node
 *
 * @param element - Appended node
 * @return The link to appended node
 * */
fun Element.addClone(element: Element): Element {
    val clone = this.ownerDocument.importNode(element, true) as Element
    this.appendChild(clone)
    return clone
}

fun NodeList.asList(): List<Element> {
    return IntStream.range(0, this.length)
        .mapToObj { index: Int -> this.item(index) as Element }
        .collect(Collectors.toList())
}

class ElementExt {
    companion object {
        fun createDocument(sDocument: String): Document {
            val documentBuilder = getDocumentBuilder()
            StringReader(sDocument).use { stringReader -> return documentBuilder.parse(InputSource(stringReader)) }
        }

        fun createDocument(streamDocument: InputStream): Document {
            val documentBuilder = getDocumentBuilder()
            return documentBuilder.parse(streamDocument)
        }

        private fun getDocumentBuilder(): DocumentBuilder {
            val dbf = DocumentBuilderFactory.newInstance()
            dbf.isNamespaceAware = true
            return dbf.newDocumentBuilder()
        }

        /*
        * Create an schema from string
        * */
        fun createSchema(sScheme: String): Schema {
            val factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
            val streamSource = StreamSource(ByteArrayInputStream(sScheme.toByteArray(StandardCharsets.UTF_8)))
            return factory.newSchema(streamSource)
        }
    }
}