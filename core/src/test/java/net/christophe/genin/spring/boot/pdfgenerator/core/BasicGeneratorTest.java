package net.christophe.genin.spring.boot.pdfgenerator.core;

import com.google.gson.Gson;
import com.samskivert.mustache.Mustache;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class BasicGeneratorTest {

    public static final String TARGET_TMP_TEST = "target/tmp-test";
    private BasicGenerator bas;
    private HashMap<String, Object> parameters;
    private Generator testBean;
    private BeanA a;
    private String json;

    @Before
    public void initialize() {
        bas = new BasicGenerator(() -> Mustache.compiler().compile("<html><body>{{test}}</body></html>"));
        parameters = new HashMap<>();
        parameters.put("test", "Dark vador");
        new File(TARGET_TMP_TEST).mkdirs();
        testBean = new BasicGenerator(() -> Mustache.compiler().compile("<html><body>" +
                "{{test}}" +
                "<h2>TODO LIST</h2>" +
                "<ul>" +
                "{{#list}}<li>{{id}} - {{todo}}</li>{{/list}}" +
                "</ul>" +
                "</body></html>"));
        ArrayList<BeanB> list = new ArrayList<>();
        list.add(new BeanB(1, "Doing first"));
        list.add(new BeanB(2, "Doing Second"));
        a = new BeanA("42", list);
        json = new Gson().toJson(a);
    }

    @Test
    public void should_generate_html() {
        String result = bas.toHtml(parameters);
        assertEquals("<html><body>Dark vador</body></html>", result);
    }

    @Test
    public void should_generate_html_from_bean() {
        String result = testBean.toHtml(a);
        assertEquals("<html><body>42<h2>TODO LIST</h2><ul><li>1.0 - Doing first</li><li>2.0 - Doing Second</li></ul></body></html>", result);
    }

    @Test
    public void should_generate_html_from_json() {
        String result = testBean.toHtml(json);
        assertEquals("<html><body>42<h2>TODO LIST</h2><ul><li>1.0 - Doing first</li><li>2.0 - Doing Second</li></ul></body></html>", result);
    }

    @Test
    public void should_generate_pdf_bytes() {
        byte[] result = bas.toBytes(parameters);
        assertTrue(result.length > 0);
    }

    @Test
    public void should_generate_pdf_bytes_from_bean() {
        byte[] result = testBean.toBytes(a);
        assertTrue(result.length > 0);
    }

    @Test
    public void should_generate_pdf_bytes_from_json() {
        byte[] result = testBean.toBytes(json);
        assertTrue(result.length > 0);
    }


    @Test
    public void should_generate_pdf_file() {
        File file = new File(TARGET_TMP_TEST + "/should_generate_pdf_file.pdf");
        if (file.exists()) {
            file.delete();
        }
        bas.toFile(parameters, file);
        assertTrue(file.exists());
    }

    @Test
    public void should_generate_pdf_file_from_bean() {
        File file = new File(TARGET_TMP_TEST + "/should_generate_pdf_file_from_bean.pdf");
        if (file.exists()) {
            file.delete();
        }

        testBean.toFile(a, file);
        assertTrue(file.exists());
    }

    @Test
    public void should_generate_pdf_file_from_json() {
        File file = new File(TARGET_TMP_TEST + "/should_generate_pdf_file_from_json.pdf");
        if (file.exists()) {
            file.delete();
        }

        testBean.toFile(json, file);
        assertTrue(file.exists());
    }
}
