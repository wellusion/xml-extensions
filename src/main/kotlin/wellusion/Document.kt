package wellusion

import org.w3c.dom.Document
import org.w3c.dom.Node
import java.io.File
import javax.xml.validation.Schema

val Document.ext: DocumentExt
    get() = object : DocumentExt() {
        val nodeExt = (this@ext as Node).ext

        override fun toFile(file: File) = nodeExt.toFile(file)
        override fun toInputStream() = nodeExt.toInputStream()
        override fun toByteArray() = nodeExt.toByteArray()
        override fun toString() = nodeExt.toString()
        override fun schemaValidation(schema: Schema, threwException: Boolean) = nodeExt.schemaValidation(schema, threwException)
    }