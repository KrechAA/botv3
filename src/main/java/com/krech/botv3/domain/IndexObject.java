package com.krech.botv3.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "indexes")
public class IndexObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;


    /**
     * @index all letters without first
     */
    @Column(name = "other_letter")
    String otherLetter;


    /**
     * @firstLetter only first letter
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


    public String getOtherLetter() {
        return otherLetter;
    }

    public void setOtherLetter(String otherLetter) {
        this.otherLetter = otherLetter;
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
