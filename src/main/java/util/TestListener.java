package util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.*;

import java.util.concurrent.ConcurrentHashMap;

/**
 * TestListener integrates TestNG with ExtentReports.
 * - Automatically logs test status (PASS, FAIL, SKIP)
 * - Captures screenshots on failures
 * - Ensures thread-safety for parallel test execution
 * - Flushes the report after the suite completes
 */
public class TestListener implements ITestListener, ISuiteListener {

    // Thread-safe map to store ExtentTest instances per thread (for parallel runs)
    private static final ConcurrentHashMap<Long, ExtentTest> tests = new ConcurrentHashMap<>();

    // Shared ExtentReports instance for the entire suite
    private ExtentReports extent;

    /**
     * Called once when the test suite starts.
     * Initializes the ExtentReports instance.
     */
    @Override
    public void onStart(ISuite suite) {
        extent = ReportManager.getReporter(); // get singleton instance from ReportManager
    }

    /**
     * Called once when the test suite finishes.
     * Flushes (writes) all ExtentReports data to the HTML report.
     */
    @Override
    public void onFinish(ISuite suite) {
        if (extent != null) extent.flush();
    }

    /**
     * Called before each test method starts.
     * Creates a new ExtentTest entry for the current method.
     */
    @Override
    public void onTestStart(ITestResult result) {
        String name = result.getMethod().getMethodName(); // method name as test title
        ExtentTest test = extent.createTest(name);         // create a new test in Extent report
        tests.put(Thread.currentThread().getId(), test);   // associate test with thread ID
    }

    /**
     * Called when a test method passes.
     * Logs a PASS status in the Extent report.
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        current().log(Status.PASS, "Passed");
    }

    /**
     * Called when a test method fails.
     * Captures a screenshot, logs the exception, and attaches the screenshot to the report.
     */
    @Override
    public void onTestFailure(ITestResult result) {
        // Capture screenshot and get the file path
        String path = ScreenshotUtil.capturePNG(result.getMethod().getMethodName());

        ExtentTest ct = current();
        ct.log(Status.FAIL, result.getThrowable()); // log failure reason

        // Attach screenshot if available
        if (path != null) {
            ct.addScreenCaptureFromPath(path);
        }
    }

    /**
     * Called when a test is skipped (e.g., dependency failed or was disabled).
     * Logs SKIPPED status in the report.
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        current().log(Status.SKIP, "Skipped");
    }

    /**
     * Not used, but must be implemented (TestNG interface requirement).
     * Triggered for partial test success (rarely used).
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    /**
     * Not used: triggered before the first test method in a test context runs.
     */
    @Override
    public void onStart(ITestContext context) {}

    /**
     * Not used: triggered after all test methods in a test context have run.
     */
    @Override
    public void onFinish(ITestContext context) {}

    /**
     * Helper method to get the ExtentTest instance for the current thread.
     */
    private ExtentTest current() {
        return tests.get(Thread.currentThread().getId());
    }
}
