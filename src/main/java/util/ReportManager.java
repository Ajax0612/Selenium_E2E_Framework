package util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.nio.file.Files;
import java.nio.file.Path;

public final class ReportManager {
    private static ExtentReports extent;
    private ReportManager() {}


    public static synchronized ExtentReports getReporter() {
        if (extent == null) {
            try {
                Path reportsDir = Path.of("target", "reports");
                Files.createDirectories(reportsDir);

                ExtentSparkReporter spark =
                        new ExtentSparkReporter(reportsDir.resolve("ExtentReport.html").toString());

                // Inline configuration (optional)
                spark.config().setTheme(Theme.DARK);
                spark.config().setDocumentTitle("Automation Test Report");
                spark.config().setReportName("UI Test Results");
                spark.config().setEncoding("utf-8");

                extent = new ExtentReports();
                extent.attachReporter(spark);

                // Add system/environment info (optional)
                extent.setSystemInfo("Framework", "Selenium + TestNG");
                extent.setSystemInfo("Browser", System.getProperty("BROWSER", "chrome"));
                extent.setSystemInfo("Headless", System.getProperty("HEADLESS", "true"));
                extent.setSystemInfo("OS", System.getProperty("os.name"));
                extent.setSystemInfo("Tester", "QA Automation");

            } catch (Exception e) {
                throw new RuntimeException("Failed to init ExtentReports", e);
            }
        }
        return extent;
    }
}
