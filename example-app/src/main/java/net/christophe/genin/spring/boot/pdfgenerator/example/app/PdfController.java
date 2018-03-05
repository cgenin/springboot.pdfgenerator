package net.christophe.genin.spring.boot.pdfgenerator.example.app;

import net.christophe.genin.spring.boot.pdfgenerator.core.Generator;
import net.christophe.genin.spring.boot.pdfgenerator.core.PdfGeneratorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class PdfController {

    private static final Logger logger = LoggerFactory.getLogger(PdfController.class);

    @Autowired
    private PdfGeneratorBuilder pdfGeneratorBuilder;

    @RequestMapping(path = "/send-static-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamSource> pdfFile() {
        long l = System.currentTimeMillis();
        Generator generator = pdfGeneratorBuilder
                .create()
                .cache(true)
                .withClasspathResource("pdf/test.html")
                .build();

        byte[] bytes = generator.toBytes(parameters());
        InputStreamResource body = new InputStreamResource(new ByteArrayInputStream(bytes));
        logger.info("Time : " + (System.currentTimeMillis() - l) + " ms");
        return ResponseEntity.ok(body);
    }

    @RequestMapping("/write-file")
    public String index() {
        Generator generator = pdfGeneratorBuilder
                .create()
                .cache(true)
                .withClasspathResource("pdf/test.html")
                .build();
        final File pdfFile = new File("exemple6.pdf");
        HashMap<String, Object> parameters = parameters();
        generator.toFile(parameters, pdfFile);
        return "file generated";
    }

    private HashMap<String, Object> parameters() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("next", "three");
        return parameters;
    }
}
