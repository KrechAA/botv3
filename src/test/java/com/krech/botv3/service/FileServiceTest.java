package com.krech.botv3.service;

import com.krech.botv3.domain.User;
import com.krech.botv3.domain.WordObject;
import com.krech.botv3.repository.IndexRepository;
import com.krech.botv3.repository.WordRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    WordRepository wordRepository;


    FileService fileService;

    @BeforeEach
    public void init() {
        fileService = new FileService(wordRepository);
    }


    @Test
    void readWordsFromFileTest() throws IOException {
        File file  = new File("src/main/resources/forTest.txt");
        FileInputStream input = new FileInputStream(file);

        MultipartFile multipartFile = new MockMultipartFile("forTest.txt", input);

        fileService.readWordsFromFile(multipartFile);

        ArgumentCaptor<List<WordObject>> captor = ArgumentCaptor.forClass(ArrayList.class);
        verify(wordRepository, times(1)).saveAll(captor.capture());
        assertEquals(3, captor.getValue().size());
        assertEquals("Феникс", captor.getValue().get(0).getName());
        assertEquals("Фениксятина", captor.getValue().get(1).getName());
        assertEquals("Фениксоид", captor.getValue().get(2).getName());
    }

    @Test
    void wordObjectCreatorTest() {
        String str = "Абыр";
        WordObject expected = new WordObject("Абыр", "А");

        WordObject result = fileService.wordObjectCreator(str);

        assertEquals(expected.getFirstLetter(), result.getFirstLetter());
        assertEquals(expected.getName(), result.getName());
    }

}