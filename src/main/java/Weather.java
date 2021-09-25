import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.Spliterator;

public class Weather {

    public  static String getWeather(String message, Model model) throws IOException {
        //для извлечения содержимого
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" +message+"&units=metric&appid=574a9ac626537eb4c3566f911c1063be");
        //читаем сожрежимое того, что нам пришло
        Scanner in = new Scanner((InputStream) url.getContent());

        //если с потока можем что-то считать, прибавляем в строку
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }

        JSONObject object =new JSONObject(result);
        model.setName(object.getString("name"));

        //получаем подобъект main из нашего json файла
        JSONObject main = object.getJSONObject("main");
        //это строкой мы получили поле температуры
        model.setTemp(main.getDouble("temp"));
        //это строкой мы получили поле влажности
        model.setHumidity(main.getDouble("humidity"));

        //т.к. данные в weather хранятся в массиве, то надо создать JSONArray
        JSONArray getArray = object.getJSONArray("weather");
        //идем по массиву
        for (int i=0; i < getArray.length(); i++) {
           JSONObject obj = getArray.getJSONObject(i);
           model.setIcon((String) obj.get("icon"));
           model.setMain((String) obj.get("main"));
        }

        return "City: " + model. getName() + "\n" +
                "Temperature: " + model.getTemp() + "C" + "\n" +
                "Humudity: " + model.getHumidity() + "%" + "\n" +
                "Main: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/wn/" + model.getIcon() + ".png";
    }
}
