package wellusion

import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import javax.xml.transform.dom.DOMSource
import javax.xml.validation.Schema

val Schema.ext: SchemaExt
    get() = object : SchemaExt() {
        private val LOG = LoggerFactory.getLogger(Schema::class.java)

        override fun validate(document: Document): Boolean {
            val validator = this@ext.newValidator()
            try {
                validator.validate(DOMSource(document))
            } catch (e: Exception) {
                LOG.error("Schema validation error: ${e.message}. Stacktrace:${e.stackTrace}")
                return false
            }
            return true
        }

        override fun validateStrict(document: Document) {
            TODO()
        }
    }


