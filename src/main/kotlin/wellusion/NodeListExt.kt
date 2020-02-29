package wellusion

import org.w3c.dom.Element

abstract class NodeListExt {

    /**
     * Provide a NodeList as a List of elements
     *
     * @return List of elements
     */
    abstract fun toElementList(): List<Element>
}