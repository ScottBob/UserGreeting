package com.kcar;

import javax.swing.*;
import java.util.Map;

public class ShowGreeting {
    public static void main(String[] args) {
        Greeting greeting = new Greeting();
        StringBuilder message = new StringBuilder();
        message.append(greeting.getGreeting());
        DataAccess da = new DataAccess();
        Map<String, String> data = da.getData();
        for (String name : data.keySet()) {
            message.append(System.lineSeparator()).
                    append("Name: ").append(name)
            .append(", ").append("Value: ").append(data.get(name));
        }
        JOptionPane.showMessageDialog(null, message.toString());
    }
}
