package com.krech.botv3.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * DTO for indexes.
 * Index - all unique client search requests for words.
 */
@Getter
@Setter
@Entity
@Table(name = "indexes")
public class IndexObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;


    /**
     *  all letters without first
     */
    @Column(name = "other_letter")
    String otherLetters;


    /**
     *  only first letter
     */
    @Column(name = "first_letter")
    String firstLetter;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "idwords_idindex",
            joinColumns = @JoinColumn(name = "id_index"),
            inverseJoinColumns = @JoinColumn(name = "id_word"))
    Set<WordObject> words;

}
