package wellusion

import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.validation.Schema

abstract class ElementExt {

    /**
     * Find nested single-level element by its name without a given namespace
     *
     * @param name The name of element for search
     * @return The found element.
     * @throws NullPointerException if no element was found.
     */
    abstract fun findElementByName(name: String): Element

    /**
     * Find nested single-level element by its name without a given namespace
     *
     * @param name - The name of element for search
     * @return The found element or null.
     */
    abstract fun findElementByNameIfExist(name: String): Element?

    /**
     * Find nested single-level element by its name without a given namespace and set specified value.
     *
     * @param name The name of an element for which it is needed to set the value.
     * @param value Set value
     * @return The element with specified name
     * @throws NullPointerException if no element was found.
     * */
    abstract fun setValueToElement(name: String, value: String): Element

    /**
     * Find nested single-level element by its name without a given namespace and set specified value.
     *
     * @param name The name of an element for which it is needed to set the value.
     * @param value Set value
     * @return The element with specified name or null
     * */
    abstract fun setValueToElementIfExist(name: String, value: String): Element?

    /**
     * Represent the element as a string
     *
     * @return The element as a string
     */
    abstract fun asString(): String

    /**
     * Check the document for schema compliance
     *
     * @param schema Schema for checking
     * @return Whether the document is valid or not.
     */
    abstract fun schemaValidation(schema: Schema): Boolean

    /**
     * Find node by xPath
     *
     * @param sXpath XPath string to search for an element
     * @return The found element
     * @throws NullPointerException if no element was found.
     */
    abstract fun findElementByXpath(sXpath: String): Element

    /**
     * Find node by xPath
     *
     * @param sXpath XPath string to search for an element
     * @return The found element or null
     */
    abstract fun findElementByXpathIfExist(sXpath: String): Element?

    /**
     * Check an existence of an element by xPath
     *
     * @param sXpath XPath string to search for an element
     * @return Whether the element exists.
     */
    abstract fun hasElementByXpath(sXpath: String): Boolean

    /**
     * Find all nodes by xPath
     *
     * @param sXpath XPath string to search for elements
     * @return List of finding elements
     */
    abstract fun findAllElementsByXpath(sXpath: String): List<Element>

    /**
     * Add new nested single-level node
     *
     * @param name The name of creating node
     * @param value The text value of creating node
     * @param namespace The namespace of creating node
     * @return Created node
     * */
    abstract fun add(name: String, value: String? = null, namespace: String? = null): Element

    /**
     * Add a new single-level node with disabled escaping of the specified symbol.
     * For example symbol "&" in node value will be escaped by "&amp;" if escaping don't disable.
     *
     * @param name The name of creating node
     * @param escapedSymbol The symbol that needs to be escaped
     * @param value The text value of creating node
     * @param namespace The namespace of creating node
     * */
    abstract fun addWithDisabledEscaping(name: String, escapedSymbol: String, value: String? = null,
                                         namespace: String? = null)

    /**
     * Add a clone of given node
     *
     * @param element Appended node
     * @return The link to an appended node
     * */
    abstract fun addClone(element: Element): Element

    /**
     * Find all nested single-level element by attribute name
     *
     * @param attrName Attribute name for search
     * @return List of found elements
     */
    abstract fun findAllElementsByAttr(attrName: String): List<Element>

    /**
     * Find all nested single-level element by name
     *
     * @param name Element name for searching
     * @return List of found elements
     */
    abstract fun findAllElementsByName(name: String): List<Element>

    /**
     * Getting an attribute by name
     * todo Not realized element interface.
     *
     * @param attrName Attribute name
     * @return Found element
     */
    abstract fun getAttr(attrName: String): Node?

    /**
     * Getting value of an attribute by his name
     *
     * @param attrName Attribute name
     * @return Value of found attribute
     */
    abstract fun getAttrValue(attrName: String): String?

    /**
     * A recursive bypass of element tree with a custom function executing.
     *
     * @param consumer The custom function to execute on an every element in this element.
     */
    abstract fun nodeBypass(consumer: (Element) -> Unit)

    /**
     * Remove the element from its parent element
     *
     * @return Whether the element was removed
     */
    abstract fun remove(): Boolean
}