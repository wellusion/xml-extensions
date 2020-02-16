package wellusion

import org.junit.Assert
import org.junit.Test

class SchemaTest : BaseTest()  {

    @Test
    fun validate() {
        val document = DocumentExt.createDocument(testDocumentNoNs)
        val schema = SchemaExt.createSchema(testSchemaNoNs)
        val wrongSchema = SchemaExt.createSchema(testSchemaSub2, testSchemaSub1, testSchema)

        Assert.assertTrue(schema.validate(document))
        Assert.assertFalse(wrongSchema.validate(document))
    }
}