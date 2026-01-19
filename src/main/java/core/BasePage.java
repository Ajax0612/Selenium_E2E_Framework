package core;



import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.WaitUtils;

import java.time.Duration;


public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;


    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    protected WebElement find(By locator) { return driver.findElement(locator); }


    protected void click(By locator) { wait.until(ExpectedConditions.elementToBeClickable(locator)).click(); }


    protected void type(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(text);
    }


    protected void typeSecure(By locator, char[] secret) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(new String(secret));
        java.util.Arrays.fill(secret, '\0');
    }


    protected boolean visible(By locator) {
        try { wait.until(ExpectedConditions.visibilityOfElementLocated(locator)); return true; }
        catch (TimeoutException e) { return false; }
    }


    protected String text(By locator) { return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText(); }


    protected void waitForUrlContains(String fragment) { wait.until(ExpectedConditions.urlContains(fragment)); }


    protected void jsClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }


    protected void shortPauseMs(long ms) { WaitUtils.sleep(ms); }
}