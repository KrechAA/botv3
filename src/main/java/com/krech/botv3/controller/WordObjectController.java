package com.krech.botv3.controller;


import com.krech.botv3.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
public class WordObjectController {

    private final WordService wordService;

    @Autowired
    public WordObjectController(WordService wordService) {
        this.wordService = wordService;
    }


    @GetMapping(value = "/request/{letters}")
    public ResponseEntity<List<String>> read(@PathVariable(name = "letters") String letters) {
        final List<String> words = wordService.searchWordsForClient(letters);

        return words != null
                ? new ResponseEntity<>(words, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
