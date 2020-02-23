package wellusion

import org.junit.Test

class TransformerExtTest : BaseTest() {

    @Test
    fun createTransformer() {
        TransformerExt.createXsltTransformer(testXslt)
    }

    @Test
    fun createXmlTransformer() {
         TransformerExt.createXmlTransformer()
    }
}