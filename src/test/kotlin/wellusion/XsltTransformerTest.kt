package wellusion

import org.junit.Assert
import org.junit.Test

class XsltTransformerTest : BaseTest() {

    @Test
    fun xsltTransformation() {
        val transformer = XsltTransformerExt.createTransformer(testXslt)
        val sourceDocument = DocumentExt.createDocument(testDocumentNoNs)
        val transformedDocument = transformer.xsltTransform(sourceDocument)
        Assert.assertEquals("child7-value", transformedDocument.documentElement.textContent)
    }
}