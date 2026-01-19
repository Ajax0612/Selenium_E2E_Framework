package pages;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private final By username = By.id("username");
    private final By password = By.id("password");
    private final By submit = By.cssSelector("button[type='submit']");
    private final By banner = By.id("welcomeBanner");


    public LoginPage(WebDriver driver) { super(driver); }


    public LoginPage open(String baseUrl) { driver.get(baseUrl + "/login"); return this; }
    public LoginPage typeUsername(String user) { type(username, user); return this; }
    public LoginPage typePassword(char[] secret) { typeSecure(password, secret); return this; }
    public void submit() { click(submit); }
    public boolean isLoggedIn() { return visible(banner); }
}
