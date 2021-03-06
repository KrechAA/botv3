package com.krech.botv3.service;


import com.krech.botv3.domain.WordObject;
import com.krech.botv3.repository.WordRepository;


import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.apache.tomcat.websocket.Constants;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class FileService {


    private final WordRepository wordRepository;

    @Autowired
    public FileService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }


    public void readWordsFromFile(MultipartFile multipartFile) throws IOException {
        InputStream initialStream = multipartFile.getInputStream();
        InputStreamReader isr = new InputStreamReader(initialStream, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        List<WordObject> listOfWO = new ArrayList<>();
        br.lines().forEach(line -> listOfWO.add(wordObjectCreator(line)));
        wordRepository.saveAll(listOfWO);
    }

    public WordObject wordObjectCreator(String str) {
        String fL = String.valueOf(str.charAt(0));
        String oL = str.substring(1);
        String newFl = fL.toUpperCase();
        String newStr = newFl + oL;

        return new WordObject(newStr, newFl);
    }
}
