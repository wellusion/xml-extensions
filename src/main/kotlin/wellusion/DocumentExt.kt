package wellusion

import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.File
import java.io.FileWriter
import java.io.InputStream
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

abstract class DocumentExt {
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

        /**
         * Create a document builder
         *
         * @return Created document builder
         */
        fun getDocumentBuilder(): DocumentBuilder {
            val documentBuilderFactory = DocumentBuilderFactory.newInstance()
            documentBuilderFactory.isNamespaceAware = true
            return documentBuilderFactory.newDocumentBuilder()
        }

        /**
         * Write the document to the file. Note: The specified file has to exist.
         *
         * @param file The file to write the document to.
         * @param document The document for writing.
         */
        fun writeDocumentToFile(file: File, document: Document) {

            FileWriter(file).use { writer ->
                val result = StreamResult(writer)
                val source = DOMSource(document)
                TransformerExt.createXmlTransformer().transform(source, result)
            }
        }
    }
}