package com.aunghtookhine.telegram.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${bot.name}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
//        if(update.hasMessage() && update.getMessage().getText().startsWith("/date")){
//            handleCommand(update);
//        }
        if(update.hasMessage()){
            Message message = update.getMessage();
            if(message.hasText()){
                String text = message.getText();
                System.out.println(text);
                if(text.equals("/start")){
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("Hello. Please provide the date (YYYY-MM-DD) for your report.");
                    sendMessage.setParseMode(ParseMode.MARKDOWN);
                    sendMessage.setChatId(message.getChatId());

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                } else if(text.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    System.out.println("Hello");
                    handleGenerateCommand(update, text);
                }else {
                    System.out.println("World");
                }
            }
        }
    }

    public void handleGenerateCommand(Update update, String date) {
        Long chatId = update.getMessage().getChatId();

        ResponseEntity<byte[]> response = restTemplate.getForEntity(
                "http://localhost:8080/report?date=" + date, byte[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            sendFileToTelegram(response.getBody(), chatId, date);
        } else {
            sendErrorMessage(chatId);
        }
    }

    public void sendErrorMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Could not generate report for the selected date.");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendFileToTelegram(byte[] file, Long chatId, String date) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendDocument";
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("chat_id", chatId);

        // Use ByteArrayResource to send the file
        body.add("document", new ByteArrayResource(file) {
            @Override
            public String getFilename() {
                return date + ".xlsx"; // Set a filename for the document
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, requestEntity, String.class);
    }
}
