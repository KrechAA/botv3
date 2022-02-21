package com.krech.botv3.repository;

import com.krech.botv3.domain.IndexObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexRepository extends CrudRepository<IndexObject, Integer> {


    List<IndexObject> findByFirstLetter(String str);

    IndexObject findByFirstLetterAndOtherLetter(String firstLetter, String otherLetters);


}
