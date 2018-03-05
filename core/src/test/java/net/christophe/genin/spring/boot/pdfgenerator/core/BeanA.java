package net.christophe.genin.spring.boot.pdfgenerator.core;

import java.util.List;

public class BeanA {

    private String test;

    private List<BeanB> list;

    public BeanA(String test, List<BeanB> list) {
        this.test = test;
        this.list = list;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public List<BeanB> getList() {
        return list;
    }

    public void setList(List<BeanB> list) {
        this.list = list;
    }
}
