package wellusion

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamSource

class XsltTransformerExt {
    companion object {

        /**
         * Create a transformer object for xslt transformations
         *
         * @param sXslt Xslt document as String
         * @return Created transformer
         */
        fun createTransformer(sXslt: String): Transformer {
            // val streamSource = StreamSource(ByteArrayInputStream(sXslt.toByteArray(StandardCharsets.UTF_8)))
            ByteArrayInputStream(sXslt.toByteArray(StandardCharsets.UTF_8)).use { inputStream ->
                val streamSource = StreamSource(inputStream)
                return TransformerFactory.newInstance().newTransformer(streamSource)
            }
        }
    }
}
