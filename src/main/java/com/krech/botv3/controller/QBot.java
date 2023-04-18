package com.krech.botv3.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.io.IOException;

/**
 * interceptor messages from telegram client
 */
@Getter
@Setter
public class QBot extends SpringWebhookBot {
    private String botPath;
    private String botUsername;
    private String botToken;

    private MessageHandler messageHandler;

    @Autowired
    public QBot(SetWebhook setWebhook, MessageHandler messageHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
    }

    /**
     * capture message (Update) from telegram
     * @param update from telegram
     * @return message for client
     */
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "ты что-то не то ввел");
        } catch (Exception e) {
            e.printStackTrace();
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "что-то сломалось");
        }
    }

    @Override
    public String getBotPath() {
        return null;
    }

    /**
     * handle capturing message (update) from telegram
     * @param update  from telegram
     * @return message for client
     * @throws IOException
     */
    private BotApiMethod<?> handleUpdate(Update update) throws IOException {

        Message message = update.getMessage();
        if (message != null) {
            return messageHandler.answerMessage(update.getMessage());
        }

        return null;
    }

}
