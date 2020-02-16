package wellusion

import org.junit.Test

class SchemaExtTest : BaseTest()  {
    @Test
    fun createSchema() {
        SchemaExt.createSchema(testSchemaNoNs)
        SchemaExt.createSchema(testSchemaSub2, testSchemaSub1, testSchema)
    }
}