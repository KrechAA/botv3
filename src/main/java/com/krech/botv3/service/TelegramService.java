package com.krech.botv3.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.Objects;


@Service
@Getter
public class TelegramService {
    private final String URL;
    private final String botToken;

    private final RestTemplate restTemplate;

    public TelegramService(@Value("${telegram.api-url}") String URL, @Value("${telegram.bot-token}") String botToken) {
        this.URL = URL;
        this.botToken = botToken;
        this.restTemplate = new RestTemplate();
    }

    public File getDocumentFile(String fileId) {

        return restTemplate.execute(
                Objects.requireNonNull(getDocumentTelegramFileUrl(fileId)),
                HttpMethod.GET,
                null,
                clientHttpResponse -> {
                    File ret = File.createTempFile("download", "tmp");
                    StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
                    return ret;
                });
    }


    private String getDocumentTelegramFileUrl(String fileId) {
        ResponseEntity<ApiResponse<org.telegram.telegrambots.meta.api.objects.File>> response = restTemplate.exchange(
                MessageFormat.format("{0}bot{1}/getFile?file_id={2}", URL, botToken, fileId),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return Objects.requireNonNull(response.getBody()).getResult().getFileUrl(this.botToken);

    }

}

