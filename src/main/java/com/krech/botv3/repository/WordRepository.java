package com.krech.botv3.repository;

import com.krech.botv3.domain.WordObject;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface WordRepository extends CrudRepository<WordObject, Integer> {


    List<WordObject> findByFirstLetter(String c);

    WordObject findByName(String str);


    @Modifying
    @Query(value = "DELETE FROM sys.words_in_dictionary w WHERE w.name=:str", nativeQuery = true)
    void deleteByName(@Param("str") String str);


    @Modifying
    @Query(value = "UPDATE sys.words_in_dictionary w SET w.name=:name , w.first_letter=:firstLetter WHERE w.id=:id", nativeQuery = true)
    void update(@Param("name") String name, @Param("firstLetter") String firstLetter, @Param("id") int id);



}
