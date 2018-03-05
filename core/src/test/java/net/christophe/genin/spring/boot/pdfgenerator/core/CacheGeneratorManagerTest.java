package net.christophe.genin.spring.boot.pdfgenerator.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class CacheGeneratorManagerTest {

    @Autowired
    private CacheGeneratorManager cacheGeneratorManager;

    private Generator generator;
    private HashMap<String, Object> parameters;
    private BeanA a;
    private File file;


    @Before
    public void initialize() {
        generator = mock(Generator.class);

        parameters = new HashMap<>();
        parameters.put("test", "Dark vador");

        ArrayList<BeanB> list = new ArrayList<>();
        list.add(new BeanB(1, "Doing first"));
        list.add(new BeanB(2, "Doing Second"));
        a = new BeanA("42", list);

        file = new File("tyty.pdf");

    }

    @Test
    public void should_system_toHtml_from_json() {
        when(generator.toHtml(anyString())).thenReturn("result");

        Generator test = cacheGeneratorManager.getFileSystem("test", () -> generator);
        String result = test.toHtml("{}");

        Cache cache = cacheGeneratorManager.currentCache();
        assertNotNull(cache.get(CacheGeneratorManager.createFileSystemId("test"), Generator.class));
        assertEquals("result", result);
    }


    @Test
    public void should_system_toHtml_from_bean() {
        when(generator.toHtml(any(BeanA.class))).thenReturn("resultBean");

        Generator test = cacheGeneratorManager.getFileSystem("testBean", () -> generator);
        String result = test.toHtml(a);

        Cache cache = cacheGeneratorManager.currentCache();
        assertNotNull(cache.get(CacheGeneratorManager.createFileSystemId("testBean"), Generator.class));
        assertEquals("resultBean", result);
    }

    @Test
    public void should_system_toHtml_from_map() {
        when(generator.toHtml(any(Map.class))).thenReturn("resultMap");

        Generator test = cacheGeneratorManager.getFileSystem("testMap2", () -> generator);
        String result = test.toHtml(parameters);

        Cache cache = cacheGeneratorManager.currentCache();
        assertNotNull(cache.get(CacheGeneratorManager.createFileSystemId("testMap2"), Generator.class));
        assertEquals("resultMap", result);
    }


    @Test
    public void should_system_toBytes_from_json() {
        byte[] result1 = "result".getBytes();
        when(generator.toBytes(anyString())).thenReturn(result1);

        Generator test = cacheGeneratorManager.getFileSystem("test2", () -> generator);
        byte[] result = test.toBytes("{}");

        Cache cache = cacheGeneratorManager.currentCache();
        assertNotNull(cache.get(CacheGeneratorManager.createFileSystemId("test2"), Generator.class));
        assertArrayEquals(result1, result);
    }


    @Test
    public void should_system_toBytes_from_bean() {
        byte[] resultBean = "resultBean".getBytes();
        when(generator.toBytes(any(BeanA.class))).thenReturn(resultBean);

        Generator test = cacheGeneratorManager.getFileSystem("testBean2", () -> generator);
        byte[] result = test.toBytes(a);

        Cache cache = cacheGeneratorManager.currentCache();
        assertNotNull(cache.get(CacheGeneratorManager.createFileSystemId("testBean2"), Generator.class));
        assertArrayEquals(resultBean, result);
    }

    @Test
    public void should_system_toBytes_from_map() {
        byte[] resultMap = "resultMap".getBytes();
        when(generator.toBytes(any(Map.class))).thenReturn(resultMap);

        Generator test = cacheGeneratorManager.getFileSystem("testMap", () -> generator);
        byte[] result = test.toBytes(parameters);

        Cache cache = cacheGeneratorManager.currentCache();
        assertNotNull(cache.get(CacheGeneratorManager.createFileSystemId("testMap"), Generator.class));
        assertArrayEquals(resultMap, result);
    }


    @Test
    public void should_system_file_from_json() {
        when(generator.toFile(anyString(), any(File.class))).thenReturn(true);

        Generator test = cacheGeneratorManager.getFileSystem("test3", () -> generator);
        boolean result = test.toFile("{}", file);

        Cache cache = cacheGeneratorManager.currentCache();
        assertNotNull(cache.get(CacheGeneratorManager.createFileSystemId("test3"), Generator.class));
        assertTrue(result);
    }


    @Test
    public void should_system_file_from_bean() {
        when(generator.toFile(any(BeanA.class), any(File.class))).thenReturn(true);

        Generator test = cacheGeneratorManager.getFileSystem("testBean3", () -> generator);
        boolean result = test.toFile(a, file);

        Cache cache = cacheGeneratorManager.currentCache();
        assertNotNull(cache.get(CacheGeneratorManager.createFileSystemId("testBean3"), Generator.class));
        assertTrue(result);
    }

    @Test
    public void should_system_file_from_map() {
        when(generator.toFile(any(Map.class), any(File.class))).thenReturn(true);

        Generator test = cacheGeneratorManager.getFileSystem("testMap3", () -> generator);
        boolean result = test.toFile(parameters, file);

        Cache cache = cacheGeneratorManager.currentCache();
        assertNotNull(cache.get(CacheGeneratorManager.createFileSystemId("testMap3"), Generator.class));
        assertTrue(result);
    }

    @Test
    public void should_use_cache_With_classpath() {
        when(generator.toFile(any(Map.class), any(File.class))).thenReturn(true);

        Generator test = cacheGeneratorManager.getClasspath("testMap3", () -> generator);
        boolean result = test.toFile(parameters, file);

        Cache cache = cacheGeneratorManager.currentCache();
        assertNotNull(cache.get(CacheGeneratorManager.createClasspathId("testMap3"), Generator.class));
        assertTrue(result);
    }
}
