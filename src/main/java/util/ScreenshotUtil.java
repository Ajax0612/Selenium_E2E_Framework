package util;

import core.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {

    private ScreenshotUtil() {}


    public static String capturePNG(String testName) {
        try {
            var driver = DriverManager.getDriver();
            if (!(driver instanceof TakesScreenshot ts)) return null;
            File src = ts.getScreenshotAs(OutputType.FILE);


            Path outDir = Path.of("target", "reports", "screenshots");
            Files.createDirectories(outDir);
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            Path dest = outDir.resolve(testName + "_" + stamp + ".png");
            Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            return dest.toString();
        } catch (Exception e) {
            return null;
        }
    }
}