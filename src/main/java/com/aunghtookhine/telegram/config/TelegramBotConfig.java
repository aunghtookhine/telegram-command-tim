package com.aunghtookhine.telegram.config;

import com.aunghtookhine.telegram.bot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfig {
    @Bean
    public TelegramBot telegramBot(@Value("${bot.name}") String botName, @Value("${bot.token}") String botToken){
        TelegramBot telegramBot = new TelegramBot(botToken, botName);

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            System.out.println("Exception during registration telegram api: " + e.getMessage());
        }
        return telegramBot;
    }
}
