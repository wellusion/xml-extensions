package wellusion

import org.w3c.dom.Element

abstract class NodeListExt {
    abstract fun asList(): List<Element>
}