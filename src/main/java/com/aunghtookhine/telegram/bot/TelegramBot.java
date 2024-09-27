package com.aunghtookhine.telegram.bot;

import com.aunghtookhine.telegram.config.AppConfig;
import com.aunghtookhine.telegram.enums.ReportType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.File;
import java.util.*;

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
    private final List<String> menus = new ArrayList<>(Arrays.asList("Daily", "Monthly"));
    private final List<String> dailyMenus = new ArrayList<>(Arrays.asList("Data Pack", "Core System"));
    private final List<String> monthlyMenus = new ArrayList<>(Arrays.asList("MAU", "Gift Code"));
    private String reportType;
    private boolean isDaily = true;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (text.startsWith("/start")){
                showMenuKeyboard(chatId, menus, "What type of report do you want?");
            }else if (text.equals("Daily")){
                showMenuKeyboard(chatId, dailyMenus, "What kind of daily report do you want?");
            }else if (text.equals("Monthly")){
                isDaily = false;
                showMenuKeyboard(chatId, monthlyMenus, "What kind of monthly report do you want?");
            }else if (ReportType.contains(text.toUpperCase().replace(" ", "_"))){
                reportType = text.toUpperCase().replace(" ", "_");
                if (isDaily){
                    sendMessage(chatId, "Please provide a date in YYYY-MM-DD format.");
                }else {
                    sendMessage(chatId, "Please provide a month between 1 and 12.");
                }
            }else {
                handleFileGenerateCommand(chatId, text);
            }
        }
    }

    public boolean isValidDateFormat(String date) {
        return date.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
    }

    public void handleFileGenerateCommand(Long chatId, String date){
        if(isDaily){
            if(!isValidDateFormat(date)){
                sendMessage(chatId, "Please provide the validate one.");
                return;
            }
            File file = new File(appConfig.getResourceDir(), date + ".xlsx");
            if(!file.exists()){
                file = restTemplate.getForObject(appConfig.getBaseUrl()+"/report/daily?date=" + date+"&type="+ reportType, File.class);
            }
            sendFile(chatId, file);
        }else {
            int month = Integer.parseInt(date);
            if (!(month >= 1 && month <= 12)) {
                sendMessage(chatId, "Please provide the validate one.");
                return;
            }
            File file = new File(appConfig.getResourceDir(), month+"-"+reportType + ".xlsx");
            if(!file.exists()){
                file = restTemplate.getForObject(appConfig.getBaseUrl()+"/report/daily?month=" + month+"&type="+ reportType, File.class);
            }
            sendFile(chatId, file);
        }

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

    public void showMenuKeyboard(Long chatId, List<String> menus, String msg) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        for (String menu: menus){
            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setText(menu);
            keyboardRow.add(keyboardButton);
        }
        keyboardRows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(msg)
                .replyMarkup(replyKeyboardMarkup)
                .build();
        try{
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred while showing menu button: {}", e.getMessage());
        }
    }
}

//            if (text.startsWith("/start")) {
//                sendMessage(chatId, "Please provide date?");
//                awaitingDate.put(chatId, true);
//            } else if (awaitingDate.getOrDefault(chatId, false)) {
//                if (isValidDateFormat(text)) {
//                    sendMessage(chatId, "Generating Report with date: " + text);
//                    handleFileGenerateCommand(chatId, text);
//                    sendMessage(chatId, "If you want to generate more, please click /start.");
//                    awaitingDate.put(chatId, false);
//                } else {
//                    sendMessage(chatId, "Invalid date format. Please provide with YYYY-MM-DD format.");
//                }
//            }