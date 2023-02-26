package com.krech.botv3.controller;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.io.IOException;

@Getter
@Setter
public class QBot  extends SpringWebhookBot {
    private String botPath;
    private String botUsername;
    private String botToken;

    private MessageHandler messageHandler;


    public QBot(SetWebhook setWebhook, MessageHandler messageHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "шо за хрень");
        } catch (Exception e) {
            e.printStackTrace();
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "чтото сломалось");
        }
    }

    @Override
    public String getBotPath() {
        return null;
    }

    private BotApiMethod<?> handleUpdate(Update update) throws IOException {

        Message message = update.getMessage();
        if (message != null) {
            return messageHandler.answerMessage(update.getMessage());
        }

        return null;
    }

}
