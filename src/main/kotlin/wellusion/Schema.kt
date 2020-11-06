package wellusion

import org.slf4j.LoggerFactory
import org.w3c.dom.Node
import javax.xml.transform.dom.DOMSource
import javax.xml.validation.Schema

val Schema.ext: SchemaExt
    get() = object : SchemaExt() {
        private val LOG = LoggerFactory.getLogger(Schema::class.java)

        override fun isValid(node: Node): Boolean {
            val validator = newValidator()
            try {
                validator.validate(DOMSource(node))
            } catch (e: Exception) {
                LOG.error("Schema validation error: ${e.message}. Stacktrace:${e.stackTrace}")
                return false
            }
            return true
        }

        override fun validate(node: Node) {
            newValidator().validate(DOMSource(node))
        }
    }