package com.kcar;


import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import org.json.simple.*;

public class Greeting {
    public static void main(String[] args) {
        Greeting greeting = new Greeting();
        System.out.println(greeting.getGreeting());
        JOptionPane.showMessageDialog(null, greeting.getGreeting());
    }

    public String getUserName() {
        String username = System.getenv("USER");
        return username;
    }

    public String getIPAddress() {
        Process p = null;
        StringBuilder sb = new StringBuilder();
        try {
            p = Runtime.getRuntime().exec("curl ipinfo.io/ip");
            p.waitFor();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        String result = sb.toString();
        return result;
    }

    public String getCity() {
        String locationInfo = getLocationInfo();
        String city = extractValueFromIPResultString(locationInfo, "city");
        return city;
    }

    public String getZipCode() {
        String locationInfo = getLocationInfo();
        String zipCode = extractValueFromIPResultString(locationInfo, "zip");
        return zipCode;
    }

    public String getCountryCode() {
        String locationInfo = getLocationInfo();
        String countryCode = extractValueFromIPResultString(locationInfo, "country_code");
        return countryCode;
    }

    public String getLocationInfo() {
        String locationInfo = "No data";
        String ip = getIPAddress();
        URL url = null;
        try {
            url = new URL("http://api.ipstack.com/" + ip + "?access_key=7117e1b37230afca66f91136433f5ddf");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream is = connection.getInputStream();

            int status = connection.getResponseCode();
            if (status != 200) {
                return locationInfo;
            }
            // Return string looks like:
            // {"ip":"99.33.189.164","type":"ipv4","continent_code":"NA","continent_name":"North America","country_code":"US","country_name":"United States","region_code":"TX","region_name":"Texas","city":"Houston","zip":"77077","latitude":29.751,"longitude":-95.613,"location":{"geoname_id":4699066,"capital":"Washington D.C.","languages":[{"code":"en","name":"English","native":"English"}],"country_flag":"http:\/\/assets.ipstack.com\/flags\/us.svg","country_flag_emoji":"\ud83c\uddfa\ud83c\uddf8","country_flag_emoji_unicode":"U+1F1FA U+1F1F8","calling_code":"1","is_eu":false}}

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            for (String line; (line = reader.readLine()) != null; ) {
                locationInfo += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationInfo;

    }

    private String extractValueFromIPResultString(String line, String label) {
        int start = line.indexOf(":\"", line.indexOf(label)) + 2;
        int end = line.indexOf("\",", start);
        String labelString = line.substring(start, end);
        return labelString;
    }

    private double extractNumberFromIPResultString(String line, String label) {
        int start = line.indexOf(":", line.indexOf(label)) + 1;
        int end = line.indexOf(",", start);
        String labelString = line.substring(start, end);
        return Double.parseDouble(labelString);
    }

    public String getWeather(String zip, String countryCode) {
        String weather = "";
        URL url = null;
        try {
            url = new URL("http://api.openweathermap.org/data/2.5/weather?zip=" + zip + "," + countryCode + "&appid=4213c4981142f54de6ba24ad483de1ea");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream is = connection.getInputStream();

            int status = connection.getResponseCode();
            if (status != 200) {
                return weather;
            }
            // Return string looks like:
            // {"coord":{"lon":-95.6,"lat":29.71},"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01n"}],"base":"stations","main":{"temp":301.15,"pressure":1015,"humidity":74,"temp_min":300.15,"temp_max":302.15},"visibility":16093,"wind":{"speed":4.1,"deg":170},"clouds":{"all":0},"dt":1528859700,"sys":{"type":1,"id":2640,"message":0.0053,"country":"US","sunrise":1528888888,"sunset":1528939423},"id":420034519,"name":"Houston","cod":200}

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }

            weather = extractValueFromIPResultString(result.toString(), "description")
                    +  " with a temperature of ";
            double tempC = extractNumberFromIPResultString(result.toString(), "temp") - 273;
            double tempF = (tempC * 9) / 5 + 32;
            weather += String.format("%4.1f°F",tempF);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weather;
    }

    public String getGreeting() {
        // get user name
        String userName = getUserName();
        // get location
        String location = getCity();
        // get weather at location
        String weather = getWeather(getZipCode(), getCountryCode());

        

        return "Hello " + userName + ", the weather in " + location + " is " + weather;
    }
}
