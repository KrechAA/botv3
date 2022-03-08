package com.krech.botv3.controller;

import com.krech.botv3.domain.WordObject;
import com.krech.botv3.domain.rest.request.RestWordObject;
import com.krech.botv3.repository.IndexRepository;
import com.krech.botv3.repository.WordRepository;
import com.krech.botv3.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @PostMapping(value = "/word/create")
    public ResponseEntity<?> saveOneWord(@RequestBody RestWordObject restWordObject) {
        WordObject wordObject = new WordObject(restWordObject.getWord(), restWordObject.getFirstLetter());
        indexRepository.deleteAll();
        wordRepository.save(wordObject);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @DeleteMapping(value = {"/word/delete"})
    public ResponseEntity<?> delete(@RequestParam String name) {

        if (wordRepository.findByName(name) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            indexRepository.deleteAll();
            wordRepository.deleteByName(name);
            if (wordRepository.findByName(name) == null) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }


    @PutMapping(value = "/word/update")
    public ResponseEntity<?> update(@RequestParam String oldword, @RequestBody RestWordObject restNewWordObject) {
        WordObject newWordObject = new WordObject(restNewWordObject.getWord(), restNewWordObject.getFirstLetter());
        WordObject oldWordObject = wordRepository.findByName(oldword);
        if (oldWordObject == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            indexRepository.deleteAll();
            newWordObject.setId(oldWordObject.getId());
            wordRepository.update(newWordObject.getName(), newWordObject.getFirstLetter(), newWordObject.getId());
            if (wordRepository.findByName(newWordObject.getName()) == null) {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
    }
}




