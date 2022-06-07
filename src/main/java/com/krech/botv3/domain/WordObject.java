package com.krech.botv3.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
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

}
