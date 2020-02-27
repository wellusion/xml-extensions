package wellusion

import org.junit.Assert
import org.junit.Test

class TransformerTest : BaseTest() {

    @Test
    fun xsltTransformation() {
        val transformer = TransformerExt.createXsltTransformer(testXslt)
        val sourceDocument = DocumentExt.createDocument(testDocumentNoNs)
        val transformedDocument = transformer.ext.xsltTransform(sourceDocument)
        Assert.assertEquals("child7-value", transformedDocument.documentElement.textContent)
    }
}