import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by chrx on 11/20/15.
 */
public class screenShotTest {

    static WebDriver driver;
    static String URL ;
    static Properties prop;



    @BeforeClass
    public static void setUp() throws IOException {

        prop = loadProp();
        URL = prop.getProperty("URL");
        String BROWSER = prop.getProperty("browser");

        if (prop.getProperty("device").equals("mobile")) {
            String device = "?$DEVICE$=mobile-touch";
            URL += device;
        }

        if (BROWSER.equals("chrome")) {

            System.setProperty("webdriver.chrome.driver", prop.getProperty("chromeDriverLocation"));
            driver = new ChromeDriver();

        } else if (BROWSER.equals("IE")) {

            driver = new InternetExplorerDriver();

        } else
            driver = new FirefoxDriver();



        driver.manage().timeouts().implicitlyWait(20000, TimeUnit.MILLISECONDS);
        //driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
       driver.manage().window().maximize();
        // driver.manage().window().setSize(new Dimension(400, 3000));
//        driver.get(URL);
       //    URL = prop.getProperty("URL");
    //    driver.navigate().to(URL);
    }

        @Test
        public static void screenCaptureTest() throws IOException {

            // This part is to read data from file
            String ID;
            System.out.println("0");

            File testDataSrc = new File(prop.getProperty("testDataLocation"));
            FileInputStream testData = new FileInputStream(testDataSrc);
            XSSFWorkbook wb = new XSSFWorkbook(testData);
            XSSFSheet sheet1 = wb.getSheetAt(0);

            File newPath = new File(prop.getProperty("ScreenshotLocation"));
            String templateName;

            for(int i=1; i < 7; i++) {

                ID = sheet1.getRow(i).getCell(1).getStringCellValue();
                driver.get(URL +"/id/"+ ID);

                // Screenshot of File
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

                // Copy folder to new Path

                templateName = sheet1.getRow(i).getCell(0).getStringCellValue();
                FileUtils.copyFile(scrFile, new File(newPath + "/" +templateName +".png"));
                System.out.println(newPath);
                System.out.println("Screenshot of :" + "/" + templateName +" Done." );

            }

            // Open the folder of screenshots
            Desktop.getDesktop().open(newPath);


        }

    public static Properties loadProp() throws IOException {

        File file = new File("/home/chrx/IdeaProjects/screenShotTemplates/src/screenConfig.prop");
        FileInputStream fileInput = new FileInputStream(file);
        Properties prop = new Properties();
        prop.load(fileInput);
        fileInput.close();

        return prop;
    }
    @AfterClass
    public static void tearDown(){

        //Comments
        driver.quit();
    }

}
