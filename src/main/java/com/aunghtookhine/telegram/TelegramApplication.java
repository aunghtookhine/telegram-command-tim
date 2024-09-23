package com.aunghtookhine.telegram;

import com.aunghtookhine.telegram.service.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TelegramApplication {
	public static void main(String[] args) throws TelegramApiException {
//		SpringApplication.run(TelegramApplication.class, args);
//		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//		TelegramBot telegramBot = new TelegramBot();
//		botsApi.registerBot(telegramBot);
		var ctx = SpringApplication.run(TelegramApplication.class, args);

		// Initialize Telegram Bots API
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			// Get the TelegramBot bean from the application context
			TelegramBot telegramBot = ctx.getBean(TelegramBot.class);
			// Register the bot
			botsApi.registerBot(telegramBot);

	}
}
