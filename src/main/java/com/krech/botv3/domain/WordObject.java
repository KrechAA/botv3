package com.krech.botv3.domain;

import javax.persistence.*;

@Entity
@Table(name = "words")
public class WordObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "first_letter")
    String firstLetter;


    public WordObject() {
    }

    public WordObject(String name, String firstLetter) {
        this.name = name;
        this.firstLetter = firstLetter;
    }

    public int getId() {
        return id;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public String getName() {
        return name;
    }



    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }
}
