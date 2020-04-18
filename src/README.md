# xml-extensions
Xml-extensions is a slight library that contains a set of extensions for working with `org.w3c.dom` package. This library is kind of facade behind which hides the implementation of the most common cases of working with xml objects, such as searching child elements, getting node values, adding elements, creating documents and so on.
<br />Access to the extensions are performed via the extension property `ext`, for example:
```kotlin
element.ext.getChildElement("someChildName")
```
The library contains extensions for the `Element`, `Document`, `NodeList`, `Schema`, and `Transformer` classes. There are also auxiliary classes `DocumentExt`, `ElementExt`, `NodeListExt`, `SchemaExt`, and `TransformerExt` for providing static functionality, for example:
```kotlin
DocumentExt.writeDocumentToFile(file, document)
```

##Setup
_

##Examples
Create a document from a string:
```kotlin                   
val documentAsString = "<?xml version="1.0" encoding="UTF-8"?><document></docuemnt>"
val document = DocumentExt.createDocument(documentAsString)
```
Find nested single-level element by its name without a given namespace:
```kotlin 
val document = DocumentExt.createDocument(documentAsString)
document.documentElement.ext.getChildElement("childElement")
```
Get the text value of the element if the value exists. If the element has nested elements, a null value is returned:
```kotlin 
val childElement = document.documentElement.ext.getChildElement("childElement")
childElement.ext.getValue()
```
Check the element for having nested elements:
```kotlin                                                                      
val childElement = document.documentElement.ext.getChildElement("childElement")
childElement.ext.hasChildElements()
```
Provide a NodeList as a List of elements:
``` kotlin   
val element = document.documentElement.ext.getChildElement("element")
element.childNodes.ext.toElementList()
```
Check the document for schema compliance:
```kotlin                                                  
val document = DocumentExt.createDocument(documentAsString)
val schema = SchemaExt.createSchema(schemaAsString)
schema.ext.isValid(document)
```
Create a transformer instance for xslt transformations from string:
```kotlin 
val transformer = TransformerExt.createXsltTransformer(someXsltTemplate)
```
More working examples of using all extensions can be found in the project's test directory.