package net.christophe.genin.spring.boot.pdfgenerator.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PdfGeneratorBuilder {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CacheGeneratorManager cacheGeneratorManager;

    @Value("${net.cg.pdfgenerator.disable.cache:true}")
    private boolean disabledCache;


    public PdfBuilder create() {
        return new PdfBuilder(applicationContext, cacheGeneratorManager, disabledCache);
    }
}
