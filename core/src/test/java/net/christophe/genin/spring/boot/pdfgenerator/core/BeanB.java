package net.christophe.genin.spring.boot.pdfgenerator.core;

public class BeanB {

    private Integer id;

    private String todo;

    public BeanB(Integer id, String todo) {
        this.id = id;
        this.todo = todo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }
}
