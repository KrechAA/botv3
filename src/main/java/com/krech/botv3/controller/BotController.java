package com.krech.botv3.controller;


import com.krech.botv3.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
public class BotController {

    private final WordService wordService;

    @Autowired
    public BotController(WordService wordService) {
        this.wordService = wordService;
    }


    @GetMapping(value = "/bot/search")
    public ResponseEntity<List<String>> read(@RequestParam String letters) {
        final List<String> words = wordService.searchWordsForClient(letters);

        return words != null
                ? new ResponseEntity<>(words, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
