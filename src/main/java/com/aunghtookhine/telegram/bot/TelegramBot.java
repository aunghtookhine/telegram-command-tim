package com.aunghtookhine.telegram.bot;

import com.aunghtookhine.telegram.config.AppConfig;
import com.aunghtookhine.telegram.entity.Report;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.File;
import java.util.HashMap;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private final String botName;
    private final AppConfig appConfig;

    public TelegramBot(String botToken, String botName, AppConfig appConfig) {
        super(botToken);
        this.botName = botName;
        this.appConfig = appConfig;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    private final RestTemplate restTemplate = new RestTemplate();
    private final HashMap<Long, Boolean> awaitingDate = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (text.startsWith("/start")) {
                sendMessage(chatId, "Please reply this message with the desired date in YYYY-MM-DD format.");
                awaitingDate.put(chatId, true);
            } else if (awaitingDate.getOrDefault(chatId, false)) {
                if (isValidDateFormat(text)) {
                    sendMessage(chatId, "Generating Report with date: " + text);
                    handleFileGenerateCommand(chatId, text);
                    sendMessage(chatId, "If you want to generate more, please click /start.");
                    awaitingDate.put(chatId, false);
                } else {
                    sendMessage(chatId, "Invalid date format. Please provide with YYYY-MM-DD format.");
                }
            }
        }
    }

    public boolean isValidDateFormat(String date) {
        return date.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
    }

    public void handleFileGenerateCommand(Long chatId, String date){
        File file = new File(appConfig.getResourceDir(), date + ".xlsx");
        if(!file.exists()){
            file = restTemplate.getForObject(appConfig.getBaseUrl()+"/report?date=" + date, File.class);
        }
        sendFile(chatId, file);
    }

    private void sendFile(Long chatId, File file) {
        SendDocument sendDocument = SendDocument.builder()
                .chatId(chatId.toString())
                .document(new InputFile(file))
                .build();

        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending file: {}", e.getMessage());
        }
    }

    public void sendMessage(Long chatId, String msg) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId.toString())
                .text(msg)
                .build();

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message: {}", e.getMessage());
        }
    }
}
