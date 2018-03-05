package net.christophe.genin.spring.boot.pdfgenerator.core;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class PdfGeneratorBuilderTest {
    @Autowired
    PdfGeneratorBuilder pdfGeneratorBuilder;

    @Test
    public void should_create() {
        PdfBuilder pdfBuilder = pdfGeneratorBuilder.create();
        Assert.assertNotNull(pdfBuilder);
    }
}
