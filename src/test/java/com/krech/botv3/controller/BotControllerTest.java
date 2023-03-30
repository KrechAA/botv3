package com.krech.botv3.controller;

import com.krech.botv3.config.JwtFilter;
import com.krech.botv3.repository.IndexRepository;
import com.krech.botv3.repository.UserRepository;
import com.krech.botv3.repository.WordRepository;
import com.krech.botv3.service.JwtService;
import com.krech.botv3.service.UserService;
import com.krech.botv3.service.WordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BotController.class)
class BotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    WordService wordService;

    @MockBean
    JwtFilter jwtFilter;


    //TODO как обойтись без мока репозиториев?
    @MockBean
    UserRepository userRepository;

    @MockBean
    WordRepository wordRepository;

    @MockBean
    IndexRepository indexRepository;

    @Test
    void read() throws Exception {
        mockMvc.perform(get("/bot"))
                .andExpect(status().isNoContent())
               /* .andExpect(content().json("[]"))*/;
    }
}