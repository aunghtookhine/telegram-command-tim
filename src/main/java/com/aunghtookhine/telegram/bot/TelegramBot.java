package com.aunghtookhine.telegram.bot;

import com.aunghtookhine.telegram.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
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
    private final HashMap<Long, String> awaitingDate = new HashMap<>();
    private final List<String> menus = new ArrayList<>(Arrays.asList("Daily", "Monthly"));
    private final List<String> dailyMenus = new ArrayList<>(Arrays.asList("Data Pack", "Core System"));
    private final List<String> monthlyMenus = new ArrayList<>(Arrays.asList("MAU", "Gift Code"));

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if(awaitingDate.containsKey(chatId)){
                handleFileGenerateCommand(chatId, text);
            }else {
                if (text.startsWith("/start")){
                    showMenuKeyboard(chatId, menus, "What type of report do you want?");
                }else if (text.equals("Daily")){
                    showMenuKeyboard(chatId, dailyMenus, "What kind of daily report do you want?");
                }else if (text.equals("Monthly")){
                    showMenuKeyboard(chatId, monthlyMenus, "What kind of monthly report do you want?");
                }else if (dailyMenus.contains(text)) {
                    sendMessage(chatId, "Please provide the date in YYYY-MM-DD format.");
                    awaitingDate.put(chatId, text);
                } else if (monthlyMenus.contains(text)) {
                    sendMessage(chatId, "Please provide month and year in YYYY-MM format.");
                    awaitingDate.put(chatId, text);
                } else {
                    sendMessage(chatId, "Invalid Command. Please click /start to restart.");
                }
            }
        }
    }

    public boolean isValidDateFormat(String input) {
        return input.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
    }

    public boolean isValidMonthFormat(String input){
        return input.matches("^\\d{4}-(0[1-9]|1[0-2])$");
    }

    public void handleFileGenerateCommand(Long chatId, String input){
        String reportType = awaitingDate.get(chatId);
        if(dailyMenus.contains(reportType)) {
            if(isValidDateFormat(input)){
                File file = new File(appConfig.getResourceDir(), String.format("%s-%s.xlsx", input, reportType));
                if (!file.exists()) {
                    file = restTemplate.getForObject(appConfig.getBaseUrl() + "/report/daily?date=" + input + "&type=" + reportType.toUpperCase().replace(" ", "_"), File.class);
                }
                sendMessage(chatId, String.format("Generating report of %s for %s", reportType, input));
                sendFile(chatId, file);
                awaitingDate.remove(chatId);
                sendMessage(chatId, "If you want to generate other report, please click /start");
            }else {
                sendMessage(chatId, "Invalid format. Please provide the date in YYYY-MM-DD format.");
            }
        }else if (monthlyMenus.contains(reportType)){
            if (isValidMonthFormat(input)){
                File file = new File(appConfig.getResourceDir(), String.format("%s-%s.xlsx", input, reportType));
                if (!file.exists()) {
                    file = restTemplate.getForObject(appConfig.getBaseUrl() + "/report/monthly?month=" + input + "&type=" + reportType.toUpperCase().replace(" ", "_"), File.class);
                }
                sendMessage(chatId, String.format("Generating report of %s for %s", reportType, input));
                sendFile(chatId, file);
                awaitingDate.remove(chatId);
                sendMessage(chatId, "If you want to generate other report, please click /start");
            }else {
                sendMessage(chatId, "Invalid format. Please provide month and year in YYYY-MM format.");
            }
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
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId.toString())
                .text(msg)
                .replyMarkup(replyKeyboardRemove)
                .build();

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message: {}", e.getMessage());
        }
    }

    public void showMenuKeyboard(Long chatId, List<String> menus, String msg) {
        ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboardMarkup(menus);
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

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(List<String> menus) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (int i = 0; i < menus.size(); i+=2){
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton(menus.get(i)));
            if(i+1 < menus.size()){
                keyboardRow.add(new KeyboardButton(menus.get(i+1)));
            }
            keyboardRows.add(keyboardRow);
        }
        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }
}