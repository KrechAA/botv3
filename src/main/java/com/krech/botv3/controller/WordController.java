package com.krech.botv3.controller;

import com.krech.botv3.domain.WordObject;
import com.krech.botv3.domain.rest.request.WordResponse;
import com.krech.botv3.repository.IndexRepository;
import com.krech.botv3.repository.WordRepository;
import com.krech.botv3.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WordController {


    private final WordService wordService;
    private WordRepository wordRepository;
    private IndexRepository indexRepository;

    @Autowired
    public WordController(WordService wordService, WordRepository wordRepository, IndexRepository indexRepository) {
        this.wordService = wordService;
        this.wordRepository = wordRepository;
        this.indexRepository = indexRepository;
    }


    @PostMapping(value = "/words")
    public ResponseEntity<?> saveOneWord(@RequestBody WordResponse wordResponse) {
        if (wordRepository.findByName(wordResponse.getWord()) != null) {
            throw new IllegalArgumentException("This word already exist");
        }
        return wordService.saveOneWord(wordResponse);
    }


    @DeleteMapping(value = {"/words"})
    public ResponseEntity<?> delete(@RequestParam String name) {

        if (wordRepository.findByName(name) == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            indexRepository.deleteAll();
            wordRepository.deleteByName(name);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


    @PutMapping(value = "/words")
    public ResponseEntity<?> update(@RequestParam String oldword, @RequestBody WordResponse restNewWordObject) {
        WordObject oldWordObject = wordRepository.findByName(oldword);
        if (oldWordObject == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            oldWordObject.setName(restNewWordObject.getWord());
            oldWordObject.setFirstLetter(restNewWordObject.getFirstLetter());
            indexRepository.deleteAll();
            wordRepository.save(oldWordObject);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}





