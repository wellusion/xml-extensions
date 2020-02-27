package wellusion

import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.validation.Schema
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

val Element.ext: ElementExt
    get() = object : ElementExt() {
        private val LOG = LoggerFactory.getLogger(Element::class.java)

        override fun findElementByName(name: String): Element {
            return findElementByNameIfExist(name)!!
        }

        override fun findElementByNameIfExist(name: String): Element? {
            var targetElement: Element? = null
            val elementList = this@ext.childNodes
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

        override fun setValueToElement(name: String, value: String): Element {
            return setValueToElementIfExist(name, value)!!
        }

        override fun setValueToElementIfExist(name: String, value: String): Element? {
            val childElement = findElementByNameIfExist(name)
            childElement?.textContent = value
            return childElement
        }

        override fun asString(): String {
            val stringWriter = StringWriter()
            val transformer = TransformerExt.createXmlTransformer()
            if (this@ext is Document) {
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
            }
            transformer.transform(DOMSource(this@ext), StreamResult(stringWriter))
            return stringWriter.toString()
        }

        override fun schemaValidation(schema: Schema): Boolean {
            val validator = schema.newValidator()
            try {
                validator.validate(DOMSource(this@ext))
            } catch (e: Exception) {
                LOG.error("Schema validation error: ${e.message}. Stacktrace:${e.stackTrace}")
                return false
            }
            return true
        }

        override fun findElementByXpath(sXpath: String): Element {
            val elements = findAllElementsByXpath(sXpath)
            if (elements.isEmpty()) {
                throw Exception("Xml elements by xPath: $sXpath not found.")
            }
            moreThenOneElementWarning(elements)
            return elements[0]
        }

        override fun findElementByXpathIfExist(sXpath: String): Element? {
            val elements = findAllElementsByXpath(sXpath)
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

        override fun hasElementByXpath(sXpath: String): Boolean {
            val elements = findAllElementsByXpath(sXpath)
            return elements.isNotEmpty()
        }

        override fun findAllElementsByXpath(sXpath: String): List<Element> {
            val xPathFactory = XPathFactory.newInstance()
            val xpath = xPathFactory.newXPath()
            val xPathExpression = xpath.compile(sXpath)
            val nodeList = xPathExpression.evaluate(this@ext, XPathConstants.NODESET) as NodeList

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

        override fun add(name: String, value: String?, namespace: String?): Element {
            val element = if (namespace == null) {
                this@ext.ownerDocument.createElement(name)
            } else {
                this@ext.ownerDocument.createElementNS(namespace, name)
            }
            if (value != null) {
                element.textContent = value
            }

            return this@ext.appendChild(element) as Element
        }

        override fun addWithDisabledEscaping(name: String, escapedSymbol: String, value: String?, namespace: String?) {
            val disableEscaping = this@ext.ownerDocument.createProcessingInstruction(
                StreamResult.PI_DISABLE_OUTPUT_ESCAPING, escapedSymbol)
            this@ext.appendChild(disableEscaping)
            this.add(name, value, namespace)
            val enableEscaping = this@ext.ownerDocument.createProcessingInstruction(
                StreamResult.PI_ENABLE_OUTPUT_ESCAPING, escapedSymbol)
            this@ext.appendChild(enableEscaping)
        }

        override fun addClone(element: Element): Element {
            val clone = this@ext.ownerDocument.importNode(element, true) as Element
            this@ext.appendChild(clone)
            return clone
        }

        override fun findAllElementsByAttr(attrName: String): List<Element> {
            return findAllElementsByXpath("*[@$attrName]")
        }

        override fun findAllElementsByName(name: String): List<Element> {
            return findAllElementsByXpath(name)
        }

        override fun getAttr(attrName: String): Node? {
            val attrs = this@ext.attributes
            if (attrs == null || attrs.length == 0) {
                LOG.warn("Element by name \"${this@ext.nodeName}\" hasn't attributes.")
                return null
            }
            val attr = attrs.getNamedItem(attrName)
            if (attr == null) {
                LOG.warn("Attribute by name: \"$attrName\" not found in the node \"${this@ext.nodeName}\"")
            }
            return attr
        }

        override fun getAttrValue(attrName: String): String? {
            val attr = getAttr(attrName)
            return attr?.textContent?.trim()
        }

        override fun nodeBypass(consumer: (Element) -> Unit) {
            val childElements: List<Element> = this@ext.childNodes.ext.asList()
            childElements.forEach { element ->
                element.ext.nodeBypass(consumer)
            }
            consumer(this@ext)
        }

        override fun remove(): Boolean {
            val parent = this@ext.parentNode as Element?
            if (parent == null) {
                LOG.warn("The node \"${this@ext.nodeName}\" wasn't removed. Its parent node is null.")
                return false
            }
            parent.removeChild(this@ext)
            LOG.info("The node \"${this@ext.nodeName}\" is removed.")
            return true
        }
    }