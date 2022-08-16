package fr.epita.quiz.services;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Authentication {
    public static boolean authenticate(String type, String user, String password) throws IOException {
        boolean aunthenticated = false;
        FileReader reader = new FileReader("credentials.properties");
        Properties properties = new Properties();
        properties.load(reader);
        String defaultUser = "";
        String defaultPassword = "";
        if ("admin".equals(type)) {
            defaultUser = properties.getProperty("adminUser");
            defaultPassword = properties.getProperty("adminPassword");
        } else if ("student".equals(type)) {
            defaultUser = properties.getProperty("studentUser");
            defaultPassword = properties.getProperty("studentPassword");
        } else {
            return false;
        }
        if (defaultUser.equals(user) && defaultPassword.equals(password)) {
            aunthenticated = true;
        }
        return aunthenticated;
    }
}
