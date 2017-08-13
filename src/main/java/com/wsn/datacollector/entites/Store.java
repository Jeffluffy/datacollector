package com.wsn.datacollector.entites;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by lufen on 2017/8/12.
 */
@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue()
    private Long id ;

    private String name;


    @OneToOne
    private Book book ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
