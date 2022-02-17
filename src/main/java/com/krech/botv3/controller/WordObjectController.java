package com.krech.botv3.controller;

import com.krech.botv3.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WordObjectController {

    private  final WordService wordService;

@Autowired
    public WordObjectController(WordService wordService) {
        this.wordService = wordService;
    }




}
