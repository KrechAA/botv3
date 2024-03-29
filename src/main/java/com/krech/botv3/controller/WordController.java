package com.krech.botv3.controller;

import com.krech.botv3.domain.WordObject;
import com.krech.botv3.domain.rest.request.WordRequest;
import com.krech.botv3.domain.rest.response.WordResponse;
import com.krech.botv3.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * controller for administrating dictionary
 */
@RestController
public class WordController {

    private final WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }


    /**
     * add one word in DB
     * @param wordRequest
     * @return
     */
    @PostMapping(value = "/words")
    public ResponseEntity<WordResponse> saveOneWord(@RequestBody WordRequest wordRequest) {
        WordObject wordObject = wordService.saveOneWord(wordRequest);
        WordResponse wordResponse = new WordResponse(wordObject.getId(), wordObject.getName(), wordObject.getFirstLetter());
        return new ResponseEntity<>(wordResponse, HttpStatus.CREATED);
    }

    /**
     * delete one word from DB
     * @param name
     * @return
     */
    @DeleteMapping(value = "/words")
    public ResponseEntity<WordResponse> delete(@RequestParam String name) {
        wordService.deleteOneWord(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * update one word in DB
     * @param oldword existing word in string
     * @param wordRequest update word request
     * @return updated new word response
     */
    @PutMapping(value = "/words")
    public ResponseEntity<WordResponse> update(@RequestParam String oldword, @RequestBody WordRequest wordRequest) {
       WordObject wordObject = wordService.updateOneWord(oldword, wordRequest);
       WordResponse wordResponse = new WordResponse(wordObject.getId(), wordObject.getName(), wordObject.getFirstLetter());
       return new ResponseEntity<>(wordResponse, HttpStatus.OK);
    }
}





