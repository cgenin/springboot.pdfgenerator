package net.christophe.genin.spring.boot.pdfgenerator.core;


import java.io.File;
import java.util.Map;

public interface Generator {
    String toHtml(Map<String, Object> parameters);

    String toHtml(String json);

    <T> String toHtml(T bean);

    boolean toFile(Map<String, Object> parameters, File file);

    boolean toFile(String json, File file);

    <T> boolean toFile(T bean, File file);

    byte[] toBytes(Map<String, Object> parameters);

    byte[] toBytes(String json);

    <T> byte[] toBytes(T bean);
}
