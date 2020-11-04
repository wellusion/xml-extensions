package wellusion

import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.File
import java.io.InputStream
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

abstract class DocumentExt: NodeExt() {

    companion object {

        /**
         * Create a document from a string.
         *
         * @param sDocument Document as string
         * @return Created document
         */
        fun createDocument(sDocument: String): Document {
            val documentBuilder = createDocumentBuilder()
            StringReader(sDocument).use { stringReader ->
                return documentBuilder.parse(InputSource(stringReader))
            }
        }

        /**
         * Create a document from an input stream
         *
         * @param streamDocument Document as input stream
         */
        fun createDocument(streamDocument: InputStream): Document {
            val documentBuilder = createDocumentBuilder()
            return documentBuilder.parse(streamDocument)
        }

        /**
         * Create a document from an byte array
         *
         * @param bDocument Document as byte array
         */
        fun createDocument(bDocument: ByteArray): Document {
            val documentBuilder = createDocumentBuilder()
            return documentBuilder.parse(bDocument.inputStream())
        }

        /**
         * Create a document from a file
         *
         * @param fDocument Document as File
         */
        fun createDocument(fDocument: File): Document {
            val documentBuilder = createDocumentBuilder()
            return documentBuilder.parse(fDocument)
        }

        /**
         * Create a document builder
         *
         * @return Created document builder
         */
        fun createDocumentBuilder(): DocumentBuilder {
            val documentBuilderFactory = DocumentBuilderFactory.newInstance()
            documentBuilderFactory.isNamespaceAware = true
            return documentBuilderFactory.newDocumentBuilder()
        }
    }
}