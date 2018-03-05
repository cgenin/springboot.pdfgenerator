package net.christophe.genin.spring.boot.pdfgenerator.core;

import com.google.gson.Gson;
import com.samskivert.mustache.Template;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class BasicGenerator implements Generator {

    private static Gson GSON = new Gson();

    private final Supplier<Template> compiler;

    BasicGenerator(Supplier<Template> compiler) {
        this.compiler = compiler;
    }


    private ITextRenderer renderer(String html) {
        final ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        return renderer;
    }

    @Override
    public String toHtml(Map<String, Object> parameters) {
        Objects.requireNonNull(parameters);
        return compiler
                .get()
                .execute(parameters);
    }

    @Override
    public boolean toFile(Map<String, Object> parameters, File file) {
        Objects.requireNonNull(parameters);
        final String html = this.toHtml(parameters);
        try {
            ITextRenderer renderer = renderer(html);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            renderer.createPDF(fileOutputStream, true, 1);
            return true;
        } catch (Exception ex) {
            throw new IllegalStateException("toFile - error", ex);
        }
    }

    @Override
    public byte[] toBytes(Map<String, Object> parameters) {
        Objects.requireNonNull(parameters);
        final String html = this.toHtml(parameters);
        try {
            ITextRenderer renderer = renderer(html);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            renderer.createPDF(byteArrayOutputStream, true, 1);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException("toBytes - error", ex);
        }
    }


    private <T> Map<String, Object> beanToMap(T bean) {
        Objects.requireNonNull(bean);
        String json = GSON.toJson(bean);
        return stringToMap(json);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> stringToMap(String json) {
        return GSON.fromJson(json, Map.class);
    }

    @Override
    public String toHtml(String json) {
        Map<String, Object> parameters = stringToMap(json);
        return toHtml(parameters);
    }

    @Override
    public <T> String toHtml(T bean) {
        Map<String, Object> parameters = beanToMap(bean);
        return toHtml(parameters);
    }

    @Override
    public boolean toFile(String json, File file) {
        Map<String, Object> parameters = stringToMap(json);
        return toFile(parameters, file);
    }

    @Override
    public <T> boolean toFile(T bean, File file) {
        Map<String, Object> parameters = beanToMap(bean);
        return toFile(parameters, file);
    }

    @Override
    public byte[] toBytes(String json) {
        Map<String, Object> parameters = stringToMap(json);
        return toBytes(parameters);
    }

    @Override
    public <T> byte[] toBytes(T bean) {
        Map<String, Object> parameters = beanToMap(bean);
        return toBytes(parameters);
    }
}
