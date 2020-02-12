package wellusion

import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.StringReader
import java.nio.charset.StandardCharsets
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory

class ElementExt {
    companion object {
        fun createDocument(sDocument: String): Document {
            val documentBuilder = getDocumentBuilder()
            StringReader(sDocument).use { stringReader -> return documentBuilder.parse(
                InputSource(stringReader)
            ) }
        }

        fun createDocument(streamDocument: InputStream): Document {
            val documentBuilder = getDocumentBuilder()
            return documentBuilder.parse(streamDocument)
        }

        private fun getDocumentBuilder(): DocumentBuilder {
            val documentBuilderFactory = DocumentBuilderFactory.newInstance()
            documentBuilderFactory.isNamespaceAware = true
            return documentBuilderFactory.newDocumentBuilder()
        }

        /*
        * Creating a schema from a few string names.
        *
        * If the result scheme has to consist of a few schemes then you should specify all of them. Note that the sequence
        * of schemes is important: if the scheme №1 includes elements from the scheme №2, then the scheme №2 must be
        * specified first, and then - scheme №1.
        * */
        fun createSchema(vararg sSchemes: String): Schema {
            val factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
            val streamSources = sSchemes.map { sScheme ->
                StreamSource(
                    ByteArrayInputStream(
                        sScheme.toByteArray(StandardCharsets.UTF_8)
                    )
                )
            }.toTypedArray()
            return factory.newSchema(streamSources)
        }
    }
}