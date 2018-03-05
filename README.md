# pdfGenerator

An spring boot library for generating PDF files from an Xhtml file. This library also provide an mustache's template motor to dynamically generate pdf. This library use flying saucer for the pdf generation and the .


## USAGE

### Pdf Generator Builder

pdfgenerator is automatically added to spring configuration. The main class is an Builder to specifying template parameters.
Inject directly this instance like this : 

```java
    @Autowired
    private PdfGeneratorBuilder pdfGeneratorBuilder;
```
 

### Simple example - Generation from String template

this is an simple example of generation : 

```java
     Generator generator = pdfGeneratorBuilder
                    .create()
                    .withXhtml("<html><body>the universal response is {{response}}.</body></html>")
                    .build();
     Map<String, Object> data = new HashMap<>();
     data.put("response", "42");
     byte[] pdfBuffer =  generator.toBytes(data);
``` 

