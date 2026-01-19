package tests;

import config.Config;
import core.DriverFactory;
import core.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public abstract class BaseTest {

    /**
     * Accepts optional TestNG params:
     *  - browser: chrome | firefox | edge
     *  - startAt: override the initial URL
     * If 'browser' is provided by XML, we set System property so your DriverFactory.create()
     * (which reads BROWSER from System/env) uses it for this run.
     */
    @Parameters({ "browser", "startAt" })
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser,
                      @Optional("") String startAt) {

        WebDriver driver = DriverFactory.create(browser); // pass per-thread browser
        DriverManager.setDriver(driver);
        driver.manage().window().maximize();

        String url = (startAt != null && !startAt.isBlank()) ? startAt : Config.baseUrl();
        driver.get(url);
    }


    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DriverManager.unload();              // important for parallel runs
            }
        }
    }

    protected WebDriver driver() {
        return DriverManager.getDriver();
    }
}
