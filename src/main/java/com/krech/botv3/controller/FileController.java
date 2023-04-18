package com.krech.botv3.controller;


import com.krech.botv3.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * controller for file administrating
 */
@RestController
public class FileController {


    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * upload dictionary from file
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/files")
    public ResponseEntity<?> handleFileUpload(@RequestParam ("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return new ResponseEntity<>("file is empty", HttpStatus.BAD_REQUEST);
        }
        fileService.readWordsFromFile(multipartFile);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}





