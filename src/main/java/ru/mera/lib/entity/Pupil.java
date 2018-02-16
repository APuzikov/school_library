package ru.mera.lib.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "pupils")
public class Pupil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "classNumber", nullable = false)
    private int classNumber;

    @Column(name = "className")
    private String className;

    @ManyToMany(mappedBy = "pupils")
    private List<Book> books;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
