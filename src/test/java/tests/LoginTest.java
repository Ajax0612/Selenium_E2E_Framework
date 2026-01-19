package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import config.Config;
import util.SafeLog;

public class LoginTest extends BaseTest {
    @Test
    public void canLoginInParallel() {
        var page = new LoginPage(driver())
                .open(Config.baseUrl())
                .typeUsername(Config.username())
                .typePassword(Config.password());
        page.submit();
        Assert.assertTrue(page.isLoggedIn(), "Expected to be logged in");
        System.out.println("Logged in as " + SafeLog.mask(Config.username()));
    }
}
