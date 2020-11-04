package wellusion

import java.io.File

abstract class NodeExt {

    /**
     * Write the document to the file. Note: The specified file has to exist.
     *
     * @param file The file to write the document to.
     */
    abstract fun toFile(file: File)

    companion object {

    }
}