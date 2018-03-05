package net.christophe.genin.spring.boot.pdfgenerator.core;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class PdfBuilderTest {

    @Autowired
    PdfGeneratorBuilder builder;
    private Map<String, Object> parameters;

    @Before
    public void initialize() {
        parameters = new HashMap<>();
        parameters.put("next", "42");
    }


    @Test
    public void should_generate_pdf_from_file_system() {
        Generator generator = builder.create().withFileSystem("src/test/resources/test.html").build();
        assertNotNull(generator);
        byte[] bytes = generator.toBytes(parameters);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test
    public void should_generate_pdf_from_classpath() {
        Generator generator = builder.create()
                .withClasspathResource("test.html")
                .build();
        assertNotNull(generator);
        byte[] bytes = generator.toBytes(parameters);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test(expected = Exception.class)
    public void should_throw_exception_if_no_type() {
        builder.create().build();
    }

        @Test
    public void should_generate_pdf_from_string() {
        Generator generator = builder.create()
                .withCompilerOptions(template -> {
                    return template.escapeHTML(true);
                })
                .withXhtml("<html><body>The response is {{next}}</body></html>")
                .build();
        assertNotNull(generator);
        byte[] bytes = generator.toBytes(parameters);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }
}
