package pages;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {
    private final By header = By.cssSelector("h1.page-title");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }
    public boolean loaded() {
        return visible(header);
    }
}