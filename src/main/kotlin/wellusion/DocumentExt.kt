package wellusion

import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.InputStream
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class DocumentExt {
    companion object {

        /**
         * Create a document from a string.
         *
         * @param sDocument Document as string
         * @return Created document
         */
        fun createDocument(sDocument: String): Document {
            val documentBuilder = getDocumentBuilder()
            StringReader(sDocument).use { stringReader ->
                return documentBuilder.parse(
                    InputSource(stringReader)
                )
            }
        }

        /**
         * Create a document from an input stream
         *
         * @param streamDocument Document as input stream
         */
        fun createDocument(streamDocument: InputStream): Document {
            val documentBuilder = getDocumentBuilder()
            return documentBuilder.parse(streamDocument)
        }

        private fun getDocumentBuilder(): DocumentBuilder {
            val documentBuilderFactory = DocumentBuilderFactory.newInstance()
            documentBuilderFactory.isNamespaceAware = true
            return documentBuilderFactory.newDocumentBuilder()
        }
    }
}