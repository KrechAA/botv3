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
        List<String> list;

        InputStream initialStream = multipartFile.getInputStream();
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);

        File targetFile = new File("src/main/resources/targetFile.tmp");
        try (OutputStream outStream = new FileOutputStream(targetFile)) {
            outStream.write(buffer);
        }
        Path path = Path.of("src/main/resources/targetFile.tmp");
        list = Files.readAllLines(path);

        List<WordObject> listOfWO = new ArrayList<>();
        for (String str : list) {
            String fL = String.valueOf(str.charAt(0));
            String oL = str.substring(1);
            String newFl = fL.toUpperCase();
            String newStr = newFl + oL;

            WordObject w = new WordObject(newStr, newFl);
            listOfWO.add(w);
        }
        wordRepository.saveAll(listOfWO);
    }
}
