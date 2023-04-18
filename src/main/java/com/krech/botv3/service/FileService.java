package com.krech.botv3.service;


import com.krech.botv3.domain.WordObject;
import com.krech.botv3.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


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

    public void readWordsFromTelegramFile(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
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
