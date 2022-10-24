# xml-extensions
**Xml-extensions** is a slight library that contains a set of extensions for working 
with `org.w3c.dom` package. This library is kind of facade behind which hides the 
implementation of the most common cases of working with xml objects, such as searching 
child elements, getting node values, adding elements, creating documents and so on.

Often it is necessary to write additional classes like `Utils` that contains base 
functions for working with `XML` objects. Often these classes are even moved from project 
to project with some modifications. **Xml-extensions** offer a number of mostly used 
such functions.

Access to the extensions is performed via the extension property `ext`.

Frequently when debugging, you wonder whether the variable contains exactly the 
right `XML` document. It is would be great to see the document as string in a debugger. 
With **Xml-extensions** you can just invoke overridden method toString on the property's 
extension `ext`:
```kotlin 
val document: Document = ...
document.ext.toString()
```
and see something like:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<testXmlDocument xmlns:ns1="http://someaddress1">someValue</testXmlDocument>
```

It is often necessary to transform a document into other types in order to transmit it. 
Using this library, it is also easy to do this:
```kotlin                   
val document: Document = ...
document.ext.toFile(file)
document.ext.toInputStream()
document.ext.toByteArray()
```

Reverse transformation is also performed using just one method:
```kotlin                              
DocumentExt.createDocument(file)
DocumentExt.createDocument(inputStream)
DocumentExt.createDocument(byteArray)
```                                  
There are also auxiliary classes `DocumentExt`, `ElementExt`, `NodeListExt`, `SchemaExt`, 
and `TransformerExt` for providing static functionality.

Usually you need to access to some element of a document without defining a namespace, 
but just by name. **Xml-extensions** finds elements in a document without namespaces.
<br />For example, to find the element `ns1:child7` from code snippet below, it is enough 
to specify only its name:
```xml 
<testXmlDocument xmlns:ns1="http://someaddress1">
    <ns1:child7>child7-value</ns1:child7>
</testXmlDocument>
```               
```kotlin 
document.ext.getChildElement("child7")
```
The library also provides other useful extensions for work with `Element`, `Document`, 
`NodeList`, `Schema`, and `Transformer` classes. Examples for some of them can be seen 
in the section below.

## Usage

### Gradle
Add `mavenCentral()` to your list of repositories:
```groovy
repositories {
    mavenCentral()
}
```

Add this library to your list of dependencies:
```groovy
dependencies {
    compile 'com.github.wellusion:xml-extensions:1.12'
}
```

## Examples
Create a `Document` from a `String`:
```kotlin                   
val documentAsString = "<?xml version="1.0" encoding="UTF-8"?><document></docuemnt>"
val document = DocumentExt.createDocument(documentAsString)
```     
Write a `Document` as a `String`:
```kotlin 
val sDocument = document.ext.toString()
```
Find nested single-level `Element` by its name without a given namespace:
```kotlin 
val document = DocumentExt.createDocument(documentAsString)
document.documentElement.ext.getChildElement("childElement")
```
Get the text value of the `Element` if the value exists. If the `Element` has nested 
elements, a `null` value is returned:
```kotlin 
val childElement = document.documentElement.ext.getChildElement("childElement")
childElement.ext.getValue()
```
Check the `Element` for having nested elements:
```kotlin                                                                      
val childElement = document.documentElement.ext.getChildElement("childElement")
childElement.ext.hasChildElements()
```
Provide a `NodeList` as a `List` of elements:
``` kotlin   
val element = document.documentElement.ext.getChildElement("element")
element.childNodes.ext.toElementList()
```
Check the `Document` for `Schema` compliance:
```kotlin                                                  
val document = DocumentExt.createDocument(documentAsString)
val schema = SchemaExt.createSchema(schemaAsString)
schema.ext.isValid(document)
```
Create a `Transformer` instance for xslt transformations from `String`:
```kotlin 
val transformer = TransformerExt.createXsltTransformer(someXsltTemplate)
```                                                           

**More working examples of using all extensions can be found in the project's test 
directory.**
