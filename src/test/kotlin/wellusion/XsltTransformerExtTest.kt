package wellusion

import org.junit.Test

class XsltTransformerExtTest : BaseTest() {

    @Test
    fun createTransformer() {
        XsltTransformerExt.createTransformer(testXslt)
    }
}