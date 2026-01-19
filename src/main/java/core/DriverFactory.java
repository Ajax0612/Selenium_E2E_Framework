package core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public final class DriverFactory {

    private DriverFactory() {}

    /** Fallback path (kept for CLI usage): reads BROWSER from system/env. */
    public static WebDriver create() {
        String browser = System.getProperty("BROWSER",
                System.getenv().getOrDefault("BROWSER", "chrome"));
        return create(browser);
    }

    /** Preferred path: pass browser explicitly (thread-safe for parallel runs). */
    public static WebDriver create(String browser) {
        String remoteUrl = System.getProperty("REMOTE_URL", System.getenv("REMOTE_URL"));
        boolean headless = Boolean.parseBoolean(System.getProperty("HEADLESS",
                System.getenv().getOrDefault("HEADLESS", "true")));

        String b = (browser == null ? "chrome" : browser.trim().toLowerCase());

        try {
            switch (b) {
                case "firefox" -> {
                    FirefoxOptions ff = new FirefoxOptions();
                    if (headless) ff.addArguments("-headless");
                    if (isRemote(remoteUrl)) return new RemoteWebDriver(new URL(remoteUrl), ff);
                    WebDriverManager.firefoxdriver().setup();
                    return new FirefoxDriver(ff);
                }
                case "edge" -> {
                    EdgeOptions edge = new EdgeOptions();
                    if (headless) edge.addArguments("--headless=new");
                    if (isRemote(remoteUrl)) return new RemoteWebDriver(new URL(remoteUrl), edge);
                    WebDriverManager.edgedriver().setup();
                    return new EdgeDriver(edge);
                }
                default -> { // chrome
                    ChromeOptions chrome = new ChromeOptions();
                    if (headless) chrome.addArguments("--headless=new");
                    chrome.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
                    if (isRemote(remoteUrl)) return new RemoteWebDriver(new URL(remoteUrl), chrome);
                    WebDriverManager.chromedriver().setup();
                    return new ChromeDriver(chrome);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to start WebDriver for browser: " + b, e);
        }
    }

    private static boolean isRemote(String url) {
        return url != null && !url.isBlank();
    }
}
