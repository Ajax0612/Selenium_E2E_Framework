package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;

public class DashboardTest extends BaseTest {
    @Test
    public void dashboardLoads() {
        DashboardPage dashboard = new DashboardPage(driver());
        Assert.assertTrue(dashboard.loaded(), "Dashboard should be visible");
    }
}
