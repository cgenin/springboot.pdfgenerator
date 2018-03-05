package net.christophe.genin.spring.boot.pdfgenerator.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class CacheGeneratorManager {

    private static final String CACHE_NAME = CacheManager.class.getName();

    @Autowired
    private CacheManager cacheManager;

    Cache currentCache() {
        return cacheManager.getCache(CACHE_NAME);
    }


    static String createFileSystemId(String path) {
        return "file-" + path;
    }

    static String createClasspathId(String path) {
        return "classpath-" + path;
    }

    public Generator getFileSystem(String path, Supplier<Generator> supplier) {
        return new CacheGenerator(
                () -> cacheManager.getCache(CACHE_NAME)
                        .get(createFileSystemId(path), supplier::get)
        );
    }


    public Generator getClasspath(String path, Supplier<Generator> supplier) {
        return new CacheGenerator(
                () -> cacheManager.getCache(CACHE_NAME)
                        .get(createClasspathId(path), supplier::get)
        );
    }


    public static class CacheGenerator implements Generator {
        Supplier<Generator> supplier;

        private CacheGenerator(Supplier<Generator> supplier) {
            this.supplier = supplier;
        }

        private Generator getGenerator() {
            return supplier.get();
        }

        @Override
        public String toHtml(Map<String, Object> parameters) {
            return getGenerator().toHtml(parameters);
        }

        @Override
        public boolean toFile(Map<String, Object> parameters, File file) {
            return getGenerator().toFile(parameters, file);
        }

        @Override
        public byte[] toBytes(Map<String, Object> parameters) {
            return getGenerator().toBytes(parameters);
        }

        @Override
        public String toHtml(String json) {
            return getGenerator().toHtml(json);
        }

        @Override
        public <T> String toHtml(T bean) {
            return getGenerator().toHtml(bean);
        }

        @Override
        public boolean toFile(String json, File file) {
            return getGenerator().toFile(json, file);
        }

        @Override
        public <T> boolean toFile(T bean, File file) {
            return getGenerator().toFile(bean, file);
        }

        @Override
        public byte[] toBytes(String json) {
            return getGenerator().toBytes(json);

        }

        @Override
        public <T> byte[] toBytes(T bean) {
            return getGenerator().toBytes(bean);
        }
    }
}
