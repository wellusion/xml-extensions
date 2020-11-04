package wellusion

import org.slf4j.LoggerFactory
import org.w3c.dom.Node
import java.io.File
import java.io.FileWriter
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

val Node.ext: NodeExt
    get() = object : NodeExt() {
        private val log = LoggerFactory.getLogger(this::class.java)

        override fun toFile(file: File) {
            FileWriter(file).use { writer ->
                val result = StreamResult(writer)
                val source = DOMSource(this@ext)
                TransformerExt.createXmlTransformer().transform(source, result)
            }
        }
    }
