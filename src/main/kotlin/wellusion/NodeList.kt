package wellusion

import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.util.stream.Collectors
import java.util.stream.IntStream

fun NodeList.asList(): List<Element> {
    return IntStream.range(0, this.length)
        .filter { index -> this.item(index) is Element }
        .mapToObj { index -> this.item(index) as Element }
        .collect(Collectors.toList())
}