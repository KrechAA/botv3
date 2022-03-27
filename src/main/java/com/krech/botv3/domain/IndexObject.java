package com.krech.botv3.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "indexes_aka_queries")
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getOtherLetters() {
        return otherLetters;
    }

    public void setOtherLetters(String otherLetter) {
        this.otherLetters = otherLetter;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public Set<WordObject> getSetOfWords() {
        return words;
    }

    public void setWords(Set<WordObject> words) {
        this.words = words;
    }
}
