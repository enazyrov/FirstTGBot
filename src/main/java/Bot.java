import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) {

        //инициализируем API
        ApiContextInitializer.init();
        //создаем объект типа API
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        //регистрируем бота
        try{
            telegramBotsApi.registerBot(new Bot());
        }
        catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        //включаем возможность разметки
        sendMessage.enableMarkdown(true);

        //чтобы бот понимал, в какой чат отвечать, для этого устанавливаем id
        sendMessage.setChatId(message.getChatId().toString() );
        //определяем id сообщения на который будем отвечать
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        sendMessage.enableHtml(true);
        try{
            setButton(sendMessage);
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    //something

    //имплементированные методы:
    //метод для приема сообщений (он используется для приема обновлений через long poll)
    public void onUpdateReceived(Update update) {
        //создаем модель, которая будет хранить данные
        Model model = new Model();

        Message message = update.getMessage();
        if(message != null && message.hasText()){
            switch(message.getText()) {
                case "/help":
                    sendMsg(message, "Чем я могу помочь?");
                    break;

                case "/setting":
                    sendMsg(message, "Что будем настраивать?");
                    break;
                default:
                    try{
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e){
                        sendMsg(message, "Такой город не найден");
                    }
            }
        }
    }

    public void setButton(SendMessage sendMessage) {
        //инициализация клавиатуры
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        //установка разметки
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        //автоматическое изменение масштаба
        replyKeyboardMarkup.setResizeKeyboard(true);
        //скрывать нам клавиатуру после использования или нет
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }


    //для получения имя бота
    public String getBotUsername() { return "EmilsBot"; }

    
}
