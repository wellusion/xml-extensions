package wellusion

import org.junit.Assert
import org.junit.Test

class SchemaTest : BaseTest()  {

    @Test
    fun validate() {
        val document = DocumentExt.createDocument(testDocumentNoNs)
        val schema = SchemaExt.createSchema(testSchemaNoNs)
        val wrongSchema = SchemaExt.createSchema(testSchemaSub2, testSchemaSub1, testSchema)

        Assert.assertTrue(schema.ext.validate(document))
        Assert.assertFalse(wrongSchema.ext.validate(document))
    }

    @Test
    fun validateStrict() {
        val document = DocumentExt.createDocument(testDocumentNoNs)
        val schema = SchemaExt.createSchema(testSchemaNoNs)
        val wrongSchema = SchemaExt.createSchema(testSchemaSub2, testSchemaSub1, testSchema)

        schema.ext.validateStrict(document)
        Assert.assertThrows(Exception::class.java) {
            wrongSchema.ext.validateStrict(document)
        }
    }
}