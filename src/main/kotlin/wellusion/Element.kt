package wellusion

import org.slf4j.LoggerFactory
import org.w3c.dom.Attr
import org.w3c.dom.DOMException
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.transform.stream.StreamResult
import javax.xml.validation.Schema
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

val Element.ext: ElementExt
    get() = object : ElementExt() {
        private val LOG = LoggerFactory.getLogger(this::class.java)

        override fun getChildElement(name: String): Element {
            return getChildElementIfExist(name)!!
        }

        override fun getChildElementIfExist(name: String): Element? {
            val elementList = getAllChildElements()
            val searchedElement = elementList.find { element ->
                // Search in an element is performed with a prefix of a namespace. Remove it because it changes.
                return@find (name == element.tagName.substringAfter(":"))
            }

            if (searchedElement == null) {
                LOG.warn("An element by name \"$name\" not found.")
            }
            return searchedElement
        }

        override fun getAllChildElements(name: String): List<Element> {
            return getAllElementsByXpath("*[local-name()='$name']")
        }

        override fun getAllChildElements(): List<Element> {
            return if (hasChildElements()) {
                childNodes.ext.toElementList()
            } else {
                LOG.warn("The element \"${tagName}\" hasn't child elements.")
                emptyList()
            }
        }

        override fun setValue(value: String) {
            textContent = value
        }

        override fun getValue(): String? {
            return if (hasChildElements()) {
                LOG.warn("The element \"${tagName}\" has child elements.")
                null
            } else {
                textContent
            }
        }

        override fun hasChildElements(): Boolean {
            return childNodes.ext.toElementList().isNotEmpty()
        }

        override fun getElementByXpath(sXpath: String): Element {
            return getElementByXpathIfExist(sXpath)!!
        }

        override fun getElementByXpathIfExist(sXpath: String): Element? {
            val elements = getAllElementsByXpath(sXpath)
            return if (elements.isEmpty()) {
                null
            } else {
                if (elements.size > 1) {
                    LOG.warn("Found more than one element. The first one will be returned.")
                }
                elements[0]
            }
        }

        override fun getAllElementsByXpath(sXpath: String): List<Element> {
            val xpath = XPath.xPathFactory.newXPath()
            val xPathExpression = xpath.compile(sXpath)
            val nodeList = xPathExpression.evaluate(this@ext, XPathConstants.NODESET) as NodeList

            val elementList = nodeList.ext.toElementList()
            if (elementList.isEmpty()) {
                LOG.info("Xml elements by xPath: $sXpath not found.")
            }
            return elementList
        }

        override fun hasElementsByXpath(sXpath: String): Boolean {
            val elements = getAllElementsByXpath(sXpath)
            return elements.isNotEmpty()
        }

        override fun addChildElement(name: String, value: String?, namespace: String?): Element {
            val element = if (namespace == null) {
                ownerDocument.createElement(name)
            } else {
                ownerDocument.createElementNS(namespace, name)
            }
            if (value != null) {
                element.textContent = value
            }

            return appendChild(element) as Element
        }

        override fun addChildElementWithDisabledEscaping(
            name: String, escapedSymbol: String, value: String?, namespace: String?
        ) {
            val disableEscaping = ownerDocument.createProcessingInstruction(
                StreamResult.PI_DISABLE_OUTPUT_ESCAPING, escapedSymbol
            )
            appendChild(disableEscaping)
            addChildElement(name, value, namespace)
            val enableEscaping = ownerDocument.createProcessingInstruction(
                StreamResult.PI_ENABLE_OUTPUT_ESCAPING, escapedSymbol
            )
            appendChild(enableEscaping)
        }

        override fun addCloneChildElement(element: Element): Element {
            val cloneElement = ownerDocument.importNode(element, true) as Element
            appendChild(cloneElement)
            return cloneElement
        }

        override fun getChildElementByAttr(name: String, value: String): Element {
            return getChildElementByAttrIfExist(name, value)!!
        }

        override fun getChildElementByAttrIfExist(name: String, value: String): Element? {
            val foundElements = getAllChildElementsByAttr(name, value)
            if (foundElements.isEmpty()) {
                LOG.warn("The element \"${tagName}\" hasn't child element with the attribute \"$name\"=\"$value\".")
                return null
            }
            if (foundElements.size > 1) {
                LOG.warn("The element \"${tagName}\" has more than one child element with the attribute \"$name\"=\"$value\".")
            }
            return foundElements[0]
        }

        override fun getAllChildElementsByAttr(name: String, value: String): List<Element> {
            if (!hasChildElements()) {
                LOG.warn("The element \"${tagName}\" hasn't child elements.")
                return emptyList()
            }
            return childNodes.ext.toElementList().filter { element ->
                element.ext.getAttrValue(name)?.equals(value) ?: false
            }
        }

        override fun getAllChildElementsByExistAttr(attrName: String): List<Element> {
            return getAllElementsByXpath("*[@$attrName]")
        }

        override fun getAttr(name: String): Attr? {
            val attrs = attributes
            if (attrs == null || attrs.length == 0) {
                LOG.trace("Element by name \"${nodeName}\" hasn't attributes.")
                return null
            }

            var attr: Attr? = null
            for (i in 0 until attrs.length) {
                if (attrs.item(i).localName == name) {
                    attr = attrs.item(i) as? Attr
                    break
                }
            }
            if (attr == null) {
                LOG.trace("Attribute by name: \"$name\" not found in the element \"${nodeName}\"")
            }
            return attr
        }

        override fun getAttrValue(attrName: String): String? {
            val attr = getAttr(attrName)
            return attr?.textContent?.trim()
        }

        override fun setAttr(name: String, value: String): Boolean {
            if (nodeType != Node.ELEMENT_NODE) {
                LOG.warn("The node \"${tagName}\" is not an element.")
                return false
            }
            return try {
                val attr = getAttr(name)
                if (attr != null) {
                    attr.value = value
                } else {
                    setAttribute(name, value)
                }
                true
            } catch (e: DOMException) {
                LOG.error("Error setting value \"$value\" to attr \"$name\": $e")
                false
            }
        }

        override fun elementBypass(consumer: (Element) -> Unit) {
            val childElements = childNodes.ext.toElementList()
            childElements.forEach { element ->
                element.ext.elementBypass(consumer)
            }
            consumer(this@ext)
        }

        override fun removeChildElement(elementName: String): Boolean {
            val removingElement = getChildElementIfExist(elementName) ?: return false
            removeChild(removingElement)
            LOG.info("The element \"$elementName\" is removed.")
            return true
        }

        val nodeExt = (this@ext as Node).ext
        
        override fun toFile(file: File) = nodeExt.toFile(file)
        override fun toInputStream() = nodeExt.toInputStream()
        override fun toByteArray() = nodeExt.toByteArray()
        override fun toString() = nodeExt.toString()
        override fun schemaValidation(schema: Schema, threwException: Boolean) = nodeExt.schemaValidation(schema, threwException)
    }

private object XPath {
    val xPathFactory: XPathFactory = XPathFactory.newInstance()
}