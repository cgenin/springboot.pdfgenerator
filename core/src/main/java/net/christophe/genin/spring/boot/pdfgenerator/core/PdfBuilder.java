package net.christophe.genin.spring.boot.pdfgenerator.core;

import com.samskivert.mustache.Mustache;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.christophe.genin.spring.boot.pdfgenerator.core.PdfBuilder.TypeTemplate.NO;

/**
 * Builder for creating PDF.
 * <p>This Object can specify the template content of pdf file in xhtml format</p>
 */
public class PdfBuilder {


    private final CacheGeneratorManager cacheGeneratorManager;
    private final boolean disabledCache;
    private String classPathPath;
    private String filePath;
    private Function<Mustache.Compiler, Mustache.Compiler> optionsFunc = t -> t;
    private final ApplicationContext applicationContext;
    private TypeTemplate type = NO;
    private byte[] template;
    private boolean cache = false;

    PdfBuilder(ApplicationContext applicationContext, CacheGeneratorManager cacheGeneratorManager, boolean disabledCache) {
        this.applicationContext = applicationContext;
        this.cacheGeneratorManager = cacheGeneratorManager;
        this.disabledCache = disabledCache;
    }

    /**
     * Method for specifying {@link com.samskivert.mustache.Mustache.Compiler} Options. see JMustache docs.
     *
     * @param optionsFunc Method to add options.
     * @return the instance
     */
    public PdfBuilder withCompilerOptions(Function<Mustache.Compiler, Mustache.Compiler> optionsFunc) {
        Objects.requireNonNull(optionsFunc);
        this.optionsFunc = optionsFunc;
        return this;
    }

    /**
     * Specifying an xhtml template in the classpath.
     *
     * @param path the path
     * @return instance.
     */
    public PdfBuilder withClasspathResource(String path) {
        Objects.requireNonNull(path);
        this.type = TypeTemplate.CLASSPATH;
        this.classPathPath = path;
        return this;
    }

    /**
     * Specifying an xhtml template in the file system.
     *
     * @param path the path of the file
     * @return instance.
     */
    public PdfBuilder withFileSystem(String path) {
        Objects.requireNonNull(path);
        this.type = TypeTemplate.SYSTEM;
        this.filePath = path;
        return this;
    }

    /**
     * Specifying the content of the template.
     *
     * @param template the template
     * @return instance.
     */
    public PdfBuilder withXhtml(String template) {
        Objects.requireNonNull(template);
        try {
            return withXhtml(template.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("error in getting bytes", e);
        }
    }

    /**
     * Specifying the content of the template.
     *
     * @param template the template
     * @return instance.
     */
    public PdfBuilder withXhtml(byte[] template) {
        Objects.requireNonNull(template);
        this.type = TypeTemplate.RAW;
        this.template = template;
        return this;
    }

    /**
     * Active or disabled the template cache.
     *
     * @param cache true for activating the cache.
     * @return instance.
     */
    public PdfBuilder cache(boolean cache) {
        this.cache = cache;
        return this;
    }

    /**
     * Create an {@link Generator} instance class.
     *
     * @return the generator.
     */
    public Generator build() {
        boolean activeCache = cache && !disabledCache;
        switch (type) {
            case SYSTEM:
                if (activeCache) {
                    return cacheGeneratorManager.getFileSystem(filePath, supplierSystem());
                } else {
                    return supplierSystem().get();
                }
            case CLASSPATH:
                if (activeCache) {
                    return cacheGeneratorManager.getClasspath(classPathPath, supplierClasspath());
                } else {
                    return supplierClasspath().get();
                }

            case RAW:
                return new BasicGenerator(() -> {
                    final InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(template));
                    Mustache.Compiler compiler = Mustache.compiler();
                    return this.optionsFunc.apply(compiler).compile(reader);
                });
            default:
                throw new IllegalStateException("You must select the template before to try to build the generator");
        }
    }

    private String readFromClasspath() {
        try {
            Resource resource = applicationContext.getResource("classpath:" + classPathPath);
            URI uri = resource.getURI();
            Path path = Paths.get(uri);
            return Files.readAllLines(path)
                    .stream()
                    .collect(Collectors.joining(" "));
        } catch (IOException e) {
            throw new IllegalStateException("Error in resource classpath ", e);
        }
    }

    private String readFile() {
        try {
            return Files.readAllLines(Paths.get(filePath), Charset.forName("UTF-8"))
                    .stream()
                    .collect(Collectors.joining(" "));
        } catch (Exception e) {
            throw new IllegalStateException("Error in reading file system ", e);
        }
    }

    private Supplier<Generator> supplierClasspath() {
        return () -> new BasicGenerator(() -> {
            String text = readFromClasspath();
            Mustache.Compiler compiler = Mustache.compiler();
            return this.optionsFunc.apply(compiler).compile(text);

        });
    }

    private Supplier<Generator> supplierSystem() {
        return () -> new BasicGenerator(() -> {
            String text = readFile();
            Mustache.Compiler compiler = Mustache.compiler();
            return this.optionsFunc.apply(compiler).compile(text);
        });
    }

    /**
     * Inner state of the builder.
     */
    public enum TypeTemplate {
        RAW, CLASSPATH, SYSTEM, NO
    }
}
