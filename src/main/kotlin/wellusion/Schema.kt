package wellusion

import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import javax.xml.transform.dom.DOMSource
import javax.xml.validation.Schema

private val LOG = LoggerFactory.getLogger(Schema::class.java)

/**
 * Check the document for schema compliance
 *
 * @param document The document for checking
 * @return Whether the document is valid or not
 */
fun Schema.validate(document: Document): Boolean {
    val validator = this.newValidator()
    try {
        validator.validate(DOMSource(document))
    } catch (e: Exception) {
        LOG.error("Schema validation error: ${e.message}. Stacktrace:${e.stackTrace}")
        return false
    }
    return true
}
