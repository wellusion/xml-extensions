package wellusion

import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Node
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileWriter
import java.io.InputStream
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.validation.Schema

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

        override fun toInputStream(): InputStream {
            return ByteArrayOutputStream().use { outputStream ->
                val xmlSource = DOMSource(this@ext)
                val outputTarget = StreamResult(outputStream)
                TransformerExt.createXmlTransformer().transform(xmlSource, outputTarget)
                ByteArrayInputStream(outputStream.toByteArray())
            }
        }

        override fun toByteArray(): ByteArray {
            return ByteArrayOutputStream().use { outputStream ->
                val xmlSource = DOMSource(this@ext)
                val outputTarget = StreamResult(outputStream)
                TransformerExt.createXmlTransformer().transform(xmlSource, outputTarget)
                outputStream.toByteArray()
            }
        }

        override fun toString(): String {
            val stringWriter = StringWriter()
            val transformer = TransformerExt.createXmlTransformer()
            if (this@ext is Document) {
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
            }
            transformer.transform(DOMSource(this@ext), StreamResult(stringWriter))
            return stringWriter.toString()
        }

        override fun schemaValidation(schema: Schema, threwException: Boolean): Boolean {
            val validator = schema.newValidator()
            try {
                validator.validate(DOMSource(this@ext))
            } catch (e: Exception) {
                if (threwException) {
                    throw Exception(e)
                } else {
                    log.error("Schema validation error: ${e.message}. Stacktrace:${e.stackTrace}")
                    return false
                }
            }
            return true
        }
    }
