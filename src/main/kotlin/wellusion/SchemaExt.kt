package wellusion

import org.w3c.dom.Document
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory

abstract class SchemaExt {

    /**
     * Check the document for schema compliance
     *
     * @param document The document for checking
     * @return Whether the document is valid or not
     */
    abstract fun isValid(document: Document): Boolean

    /**
     * Check the document for schema compliance
     *
     * @param document The document for checking
     */
    abstract fun validate(document: Document)

    companion object {

        /*
        * Creating a schema from a few string names.
        *
        * If the result scheme has to consist of a few schemes then you should specify all of them. Note that the sequence
        * of schemes is important: if the scheme №1 includes elements from the scheme №2, then the scheme №2 must be
        * specified first, and then - scheme №1.
        * */
        fun createSchema(vararg sSchemes: String): Schema {
            val factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
            val streamSources = sSchemes.map { sScheme ->
                StreamSource(ByteArrayInputStream(sScheme.toByteArray(StandardCharsets.UTF_8)))
            }.toTypedArray()
            return factory.newSchema(streamSources)
        }
    }
}
