package com.kcar;

import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

public class GreetingTest {
    // get user name
    @Test
    public void getUserNameFromEnvironmentVariable() {
        Greeting c = new Greeting();
        String userName = c.getUserName();
        Assert.assertEquals("scott", userName);
    }

    // get location
    @Test
    public void getLocationTest() {
        Greeting c = new Greeting();
        String ipaddress = c.getIPAddress();
        Assert.assertNotEquals("127.0.0.1", ipaddress);
        Assert.assertEquals("99.33.189.164", ipaddress);
    }

    @Test
    public void getPhysicalLocationTest() {
        Greeting c = new Greeting();
        String ip = c.getIPAddress();
        String city = c.getCity();
        URL url = null;
        Assert.assertEquals("Houston", city);
        // get weather at location
        // get local temperature
        // Display hello message

    }

    @Test
    public void getWeatherForCity() {
        Greeting c = new Greeting();
        String zip = "77082";
        String weather = c.getWeather(zip, "us");
        Assert.assertEquals("clear sky with a temperature of 81.04°F", weather);
    }

    @Test
    public void getGreetingTest() {
        Greeting greeting = new Greeting();
        Assert.assertEquals("Hello scott, the weather in Houston is shower rain with a temperature of 85.55°F", greeting.getGreeting());
    }
}

