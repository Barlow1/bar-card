package com.nexuscmarketing.bar_card.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigUtil {

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public static String getProperty(String key) throws IOException, NullPointerException {
        Properties properties = new Properties();
        InputStream configReader = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties");
        properties.load(configReader);
        return properties.getProperty(key);
    }
}
