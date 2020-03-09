package wellusion

import org.junit.Assert
import org.junit.Test

class SchemaTest : BaseTest()  {

    @Test
    fun validate() {
        val document = DocumentExt.createDocument(testDocumentNoNs)
        val schema = SchemaExt.createSchema(testSchemaNoNs)
        val wrongSchema = SchemaExt.createSchema(testSchemaSub2, testSchemaSub1, testSchema)

        Assert.assertTrue(schema.ext.isValid(document))
        Assert.assertFalse(wrongSchema.ext.isValid(document))
    }

    @Test
    fun validateStrict() {
        val document = DocumentExt.createDocument(testDocumentNoNs)
        val schema = SchemaExt.createSchema(testSchemaNoNs)
        val wrongSchema = SchemaExt.createSchema(testSchemaSub2, testSchemaSub1, testSchema)

        schema.ext.validate(document)
        Assert.assertThrows(Exception::class.java) {
            wrongSchema.ext.validate(document)
        }
    }
}