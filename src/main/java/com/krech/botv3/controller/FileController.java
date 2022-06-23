package com.krech.botv3.controller;


import com.krech.botv3.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class FileController {


    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


    @PostMapping(value = "/files")
    public ResponseEntity<?> handleFileUpload(@RequestParam ("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return new ResponseEntity<>("file is empty", HttpStatus.BAD_REQUEST);
        }
        fileService.readWordsFromFile(multipartFile);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}





