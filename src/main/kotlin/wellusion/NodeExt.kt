package wellusion

import java.io.File
import java.io.InputStream
import javax.xml.validation.Schema

abstract class NodeExt {

    /**
     * Write the Node to the file. Note: The specified file has to exist.
     *
     * @param file The file to write the Node to.
     */
    abstract fun toFile(file: File)

    /**
     * Write the Node to an InputStream.
     *
     * @return The Node as an InputStream
     */
    abstract fun toInputStream(): InputStream

    /**
     * Write the Node to a ByteArray.
     *
     * @return The Node as a ByteArray.
     */
    abstract fun toByteArray(): ByteArray

    /**
     * Write the Node to a String.
     *
     * @return The Node as a String.
     */
    abstract override fun toString(): String

    /**
     * Check the document for schema compliance
     *
     * @param schema Schema for checking
     * @return Whether the document is valid or not.
     */
    abstract fun schemaValidation(schema: Schema, threwException: Boolean = false): Boolean
}