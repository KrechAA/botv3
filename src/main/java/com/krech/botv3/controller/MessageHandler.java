package com.krech.botv3.controller;

import com.krech.botv3.service.FileService;
import com.krech.botv3.service.TelegramService;
import com.krech.botv3.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageHandler {

    private final TelegramService telegramService;
    private final WordService wordService;
    private final FileService fileService;


    public BotApiMethod<?> answerMessage(Message message) throws IOException {
        String chatId = message.getChatId().toString();

        String inputText = message.getText();

        if (message.getCaption().equals("/upload")) {
            if (message.getFrom().getId() == 123205669) {
                return getUploadMessage(chatId, message.getDocument().getFileId());
            } else {
                return new SendMessage(chatId, "ты не админ!");
            }
        }
        if (inputText == null && message.getCaption() == null) {
            throw new IllegalArgumentException();
        }

        if (inputText.equals("/start")) {
            return getStartMessage(chatId);
        } else if (inputText.startsWith("/search")) {
            return getSearchMessage(chatId, inputText);
        }

        return new SendMessage(chatId, "я не понимаю, чего ты хочешь");
    }

    private SendMessage getStartMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "привет! чтобы сделать запрос на поиск слова введи <</search>> и доступный тебе набор букв, начиная с первой в слове. ");
        sendMessage.enableMarkdown(true);

        return sendMessage;
    }

    private SendMessage getSearchMessage(String chatId, String inputText) {
        List<String> resultList = wordService.searchWordsForClient(inputText.substring(8));
        String resultStr = String.join(", ", resultList);

        return new SendMessage(chatId, resultStr);
    }

    private SendMessage getUploadMessage(String chatId, String fileId) throws IOException {

        File file = telegramService.getDocumentFile(fileId);

        fileService.readWordsFromTelegramFile(file);
        return new SendMessage(chatId, "словарь загружен");
    }


}
