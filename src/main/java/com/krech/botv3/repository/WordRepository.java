package com.krech.botv3.repository;

import com.krech.botv3.domain.WordObject;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * repository of words.
 * exist standard methods of CrudRepository and customise request to DB
 */
@Repository
public interface WordRepository extends CrudRepository<WordObject, Integer> {


    /**
     * searching all words with first letter in method param
     * @param c first letter of the desired words
     * @return list of the valid words
     */
    List<WordObject> findByFirstLetter(String c);

    /**
     * search one word by word
     * @param str the word in string
     * @return the existing word in WordObject
     */
    WordObject findByName(String str);


    /**
     * delete the one word by word
     * @param str the word in string
     */
    @Modifying
    @Query(value = "DELETE FROM sys.words w WHERE w.name=:str", nativeQuery = true)
    void deleteByName(@Param("str") String str);


    /**
     * update the one word in DB by id
     * @param name the new word
     * @param firstLetter the first letter of the new word
     * @param id id of the existing word
     */
    @Modifying
    @Query(value = "UPDATE sys.words w SET w.name=:name , w.first_letter=:firstLetter WHERE w.id=:id", nativeQuery = true)
    void update(@Param("name") String name, @Param("firstLetter") String firstLetter, @Param("id") int id);



}
