package com.aunghtookhine.telegram.bot;

import jakarta.annotation.Resource;
import org.apache.xmlbeans.ResourceLoader;
import org.junit.platform.engine.support.descriptor.ClasspathResourceSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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

public class TelegramBot extends TelegramLongPollingBot {
    private final String botName;

    public TelegramBot(String botToken, String botName) {
        super(botToken);
        this.botName = botName;
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
            if (text.equals("/start")) {
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
        File file = new File("src/main/resources", date + ".xlsx");
        if(!file.exists()){
            ResponseEntity<File> response = restTemplate.getForEntity("http://localhost:8080/report?date=" + date, File.class);
            file = response.getBody();
        }
        sendFile(chatId, file);
    }

    private void sendFile(Long chatId, File file) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId.toString());
        sendDocument.setDocument(new InputFile(file));

        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());;
        }
    }

    public void sendMessage(Long chatId, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(msg);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}
