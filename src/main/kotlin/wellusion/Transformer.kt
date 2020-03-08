package wellusion

import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.xml.transform.Transformer
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

val Transformer.ext: TransformerExt
    get() = object : TransformerExt() {

        override fun xsltTransform(document: Document): Document {
            val documentBuilder = DocumentExt.createDocumentBuilder()
            ByteArrayOutputStream().use { outputStream ->
                val xmlSource = DOMSource(document)
                val outputTarget = StreamResult(outputStream)
                transform(xmlSource, outputTarget)
                ByteArrayInputStream(outputStream.toByteArray()).use { inputStream ->
                    return documentBuilder.parse(InputSource(inputStream))
                }
            }
        }
    }
