package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AuthHelper {

    public static void injectToken(WebDriver driver) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("config.properties tidak ditemukan!", e);
        }

        String accessToken = props.getProperty("access.token");
        String googleUser = props.getProperty("google.user");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "localStorage.setItem('accessToken', arguments[0]);" +
                        "localStorage.setItem('googleUser', arguments[1]);",
                accessToken, googleUser
        );
    }
}