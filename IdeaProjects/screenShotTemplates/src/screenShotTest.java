import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by chrx on 11/20/15.
 */
public class screenShotTest {

    static WebDriver driver;

    @BeforeClass
    public static void setUp() throws IOException {

        File file = new File("/home/chrx/IdeaProjects/screenShotTemplates/src/screenConfig.prop");
        FileInputStream fileInput = new FileInputStream(file);

        Properties prop = new Properties();
        prop.load(fileInput);
        fileInput.close();

        String BROWSER = prop.getProperty("browser");
        String URL = prop.getProperty("URL");

        if (prop.getProperty("device").equals("mobile")) {
            String device = "?$DEVICE$=mobile-touch";
            URL += device;
        }

        if (BROWSER.equals("chrome")) {

            System.setProperty("webdriver.chrome.driver", "/home/chrx/chromedriver");
            driver = new ChromeDriver();

        } else if (BROWSER.equals("IE")) {

            driver = new InternetExplorerDriver();

        } else
            driver = new FirefoxDriver();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().window().setSize(new Dimension(400, 3000));
        driver.navigate().to(URL);
    }

        @Test
        public void screenCaptureTest() throws IOException {



            driver.get("");

            // Screenshot of File
            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            File newPath = new File("/home/chrx/test1/");

            // Copy folder to new Path
            FileUtils.copyFile(scrFile, new File("/home/chrx/test1/screenshot.png"));

           // Open the folder of screenshots
            Desktop.getDesktop().open(newPath);

        }

    @AfterClass
    public static void tearDown(){

        //Comments
        driver.quit();
    }

}
