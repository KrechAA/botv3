package com.krech.botv3.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * DTO for words
 */

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "words")
public class WordObject {

    public WordObject(String name, String firstLetter) {
        this.name = name;
        this.firstLetter = firstLetter;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "first_letter")
    String firstLetter;


}
