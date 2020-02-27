package wellusion

import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.util.stream.Collectors
import java.util.stream.IntStream

val NodeList.ext: NodeListExt
get() = object : NodeListExt() {

    /**
     * Provide a NodeList as a List of elements
     *
     * @return List of elements
     */
    override fun asList(): List<Element> {
        return IntStream.range(0, this@ext.length)
            .filter { index -> this@ext.item(index) is Element }
            .mapToObj { index -> this@ext.item(index) as Element }
            .collect(Collectors.toList())
    }

}

