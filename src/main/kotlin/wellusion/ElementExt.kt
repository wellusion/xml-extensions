package wellusion

import org.w3c.dom.Attr
import org.w3c.dom.Element

abstract class ElementExt: NodeExt() {

    /**
     * Find nested single-level element by its name without a given namespace
     *
     * @param name The name of element for search
     * @return The found element.
     * @throws NullPointerException if no element was found.
     */
    abstract fun getChildElement(name: String): Element

    /**
     * Find nested single-level element by its name without a given namespace
     *
     * @param name - The name of element for search
     * @return The found element or null.
     */
    abstract fun getChildElementIfExist(name: String): Element?

    /**
     * Find all nested single-level elements by name without a given namespace
     *
     * @param name Element name for searching
     * @return List of found elements
     */
    abstract fun getAllChildElements(name: String): List<Element>

    /**
     * Find all nested single-level elements without a given namespace
     *
     * @return List of found elements
     */
    abstract fun getAllChildElements(): List<Element>

    /**
     * Set the specified value for the element.
     *
     * @param value Set value
     * */
    abstract fun setValue(value: String)

    /**
     * Get the text value of the element if the value exists. If the element has nested elements, a null value is returned.
     *
     */
    abstract fun getValue(): String?

    /**
     * Check the element for having nested elements
     *
     * @return Whether the element has nested elements
     */
    abstract fun hasChildElements(): Boolean

    /**
     * Find element by xPath
     *
     * @param sXpath XPath string to search for an element
     * @return The found element
     * @throws NullPointerException if no element was found.
     */
    abstract fun getElementByXpath(sXpath: String): Element

    /**
     * Find element by xPath
     *
     * @param sXpath XPath string to search for an element
     * @return The found element or null
     */
    abstract fun getElementByXpathIfExist(sXpath: String): Element?

    /**
     * Find all elements by xPath
     *
     * @param sXpath XPath string to search for elements
     * @return List of finding elements
     */
    abstract fun getAllElementsByXpath(sXpath: String): List<Element>

    /**
     * Check an existence of an element by xPath
     *
     * @param sXpath XPath string to search for an element
     * @return Whether the element exists.
     */
    abstract fun hasElementsByXpath(sXpath: String): Boolean

    /**
     * Add new nested single-level element
     *
     * @param name The name of creating element
     * @param value The text value of creating element
     * @param namespace The namespace of creating element
     * @return Created element
     * */
    abstract fun addChildElement(name: String, value: String? = null, namespace: String? = null): Element

    /**
     * Add a new single-level element with disabled escaping of the specified symbol.
     * For example symbol "&" in element value will be escaped by "&amp;" if escaping don't disable.
     *
     * @param name The name of creating element
     * @param escapedSymbol The symbol that needs to be escaped
     * @param value The text value of creating element
     * @param namespace The namespace of creating element
     * */
    abstract fun addChildElementWithDisabledEscaping(
        name: String, escapedSymbol: String, value: String? = null, namespace: String? = null
    )

    /**
     * Add a clone of given element
     *
     * @param element Appended element
     * @return The link to an appended element
     * */
    abstract fun addCloneChildElement(element: Element): Element

    /**
     * Find nested single-level element with the attribute value without a given namespace
     *
     * @param name Name of the attribute
     * @param value Value of the attribute
     * @return Found element
     * @throws NullPointerException if no element was found.
     */
    abstract fun getChildElementByAttr(name: String, value: String): Element

    /**
     * Find nested single-level element with the attribute value without a given namespace
     *
     * @param name Name of the attribute
     * @param value Value of the attribute
     * @return Found element
     */
    abstract fun getChildElementByAttrIfExist(name: String, value: String): Element?

    /**
     * Find all nested single-level elements with the attribute value without a given namespace
     *
     * @param name Name of the attribute
     * @param value Value of the attribute
     * @return List of found elements
     */
    abstract fun getAllChildElementsByAttr(name: String, value: String): List<Element>

    /**
     * Find all nested single-level element with the specified attribute name without a given namespace
     *
     * @param attrName Attribute name for search
     * @return List of found elements
     */
    abstract fun getAllChildElementsByExistAttr(attrName: String): List<Element>

    /**
     * Getting an attribute by his name without a given namespace
     *
     * @param name Attribute name
     * @return Found element
     */
    abstract fun getAttr(name: String): Attr?

    /**
     * Getting value of an attribute by his name without a given namespace
     *
     * @param attrName Attribute name
     * @return Value of found attribute
     */
    abstract fun getAttrValue(attrName: String): String?

    /**
     * Add new attribute or update existing one.
     *
     * @param name Attribute name
     * @param value Attribute name
     * @return True if the operation is successful
     */
    abstract fun setAttr(name: String, value: String): Boolean

    /**
     * A recursive bypass of element tree with a custom function executing.
     *
     * @param consumer The custom function to execute on an every element in this element.
     */
    abstract fun elementBypass(consumer: (Element) -> Unit)

    /**
     * Remove nested element by his name.
     *
     * @return Whether the element was removed
     */
    abstract fun removeChildElement(elementName: String): Boolean
}