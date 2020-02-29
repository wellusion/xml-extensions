package wellusion

import org.w3c.dom.Document
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamSource

abstract class TransformerExt {

    /**
     * Transform a document using a xslt template
     *
     * @param document A document for transformation
     * @return A document after transformation
     * */
    abstract fun xsltTransform(document: Document): Document

    companion object {

        /**
         * Create a transformer instance for xslt transformations from string
         *
         * @param sXslt Xslt document as String
         * @return Created transformer
         */
        fun createXsltTransformer(sXslt: String): Transformer {
            // val streamSource = StreamSource(ByteArrayInputStream(sXslt.toByteArray(StandardCharsets.UTF_8)))
            ByteArrayInputStream(sXslt.toByteArray(StandardCharsets.UTF_8)).use { inputStream ->
                val streamSource = StreamSource(inputStream)
                return TransformerFactory.newInstance().newTransformer(streamSource)
            }
        }

        /**
         * Create a transformer instance for xml transformations
         *
         * @return Created transformer
         */
        fun createXmlTransformer(): Transformer {
            val transformerFactory = TransformerFactory.newInstance()
            val transformer = transformerFactory.newTransformer()
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
            transformer.setOutputProperty(OutputKeys.METHOD, "xml")
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0")

            return transformer
        }
    }
}
