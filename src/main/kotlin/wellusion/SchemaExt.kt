package wellusion

import org.w3c.dom.Document
import org.w3c.dom.Node
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
     * @param node The Node for checking
     * @return Whether the document is valid or not
     */
    abstract fun isValid(node: Node): Boolean

    /**
     * Check the document for schema compliance
     *
     * @param node The Node for checking
     */
    abstract fun validate(node: Node)

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
