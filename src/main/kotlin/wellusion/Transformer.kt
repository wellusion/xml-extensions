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

        /**
         * Transform a document using a xslt template
         *
         * @param document A document for transformation
         * @return A document after transformation
         * */
        override fun xsltTransform(document: Document): Document {

            val documentBuilder = DocumentExt.getDocumentBuilder()
            ByteArrayOutputStream().use { outputStream ->
                val xmlSource = DOMSource(document)
                val outputTarget = StreamResult(outputStream)
                this@ext.transform(xmlSource, outputTarget)
                ByteArrayInputStream(outputStream.toByteArray()).use { inputStream ->
                    return documentBuilder.parse(InputSource(inputStream))
                }
            }
        }

    }
