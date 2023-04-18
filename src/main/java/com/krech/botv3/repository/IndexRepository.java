package com.krech.botv3.repository;

import com.krech.botv3.domain.IndexObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * repository of indexes.
 * exist standard methods of CrudRepository and customise request to DB
 */
@Repository
public interface IndexRepository extends CrudRepository<IndexObject, Integer> {


    /**
     * searching all indexes by first letter
     * @param str index in string
     * @return list of IndexObject
     */
    List<IndexObject> findByFirstLetter(String str);

    /**
     * search one index by first letter and other letters
     * @param firstLetter first letter in desired index
     * @param otherLetters other letters in desired index
     * @return desired index in IndexObject
     */
    IndexObject findByFirstLetterAndOtherLetters(String firstLetter, String otherLetters);


}
