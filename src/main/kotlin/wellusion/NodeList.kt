package wellusion

import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.util.stream.Collectors
import java.util.stream.IntStream

val NodeList.ext: NodeListExt
get() = object : NodeListExt() {

    override fun toElementList(): List<Element> {
        return IntStream.range(0, length)
            .filter { index -> item(index) is Element }
            .mapToObj { index -> item(index) as Element }
            .collect(Collectors.toList())
    }
}

