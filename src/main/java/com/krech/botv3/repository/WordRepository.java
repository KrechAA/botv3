package com.krech.botv3.repository;

import com.krech.botv3.domain.WordObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WordRepository extends CrudRepository <WordObject, Integer> {

 //   void addOneWord(String word);

   // List<WordObject> findByFirstLetter(String c);

   // @Query("select * from words where firtsletter = :c")
    List<WordObject> findByFirstLetter(String c);
}
