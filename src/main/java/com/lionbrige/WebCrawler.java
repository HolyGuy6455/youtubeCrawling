package com.lionbrige;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * WebCrawler
 */
public class WebCrawler {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor jsExecutor;
    private final static WebCrawler singletonInstance;
    static {
        singletonInstance = new WebCrawler();
    }

    private WebCrawler(){
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        // Logger.log(System.getProperty("user.dir"));
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
        jsExecutor = (JavascriptExecutor) driver;
    }

    public static WebCrawler getInstance(){
        return singletonInstance;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public JavascriptExecutor getJsExecutor() {
        return jsExecutor;
    }

    public void close(){
        driver.close();
    }
}