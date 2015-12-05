package test.java.com.mycompany.app;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by chrx on 11/20/15.
 */
public class Templates {

    static WebDriver driver;
    static String URL ;
    static Properties prop;



    public static void setUp() throws IOException, InterruptedException {

        prop = loadProp();
        URL = prop.getProperty("URL");
        String BROWSER = prop.getProperty("browser");




        if (BROWSER.equals("chrome")) {

            System.setProperty("webdriver.chrome.driver", prop.getProperty("chromeDriverLocation"));
            driver = new ChromeDriver();

        } else if (BROWSER.equals("IE")) {

            driver = new InternetExplorerDriver();

        } else
            driver = new FirefoxDriver();



        driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
        //driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);

        driver.manage().window().maximize();

        if (prop.getProperty("device").equals("mobile")) {
            driver.manage().window().setSize(new Dimension(400, 3000));
        }

        Thread.sleep(2000);
//        driver.get(URL);
        //    URL = prop.getProperty("URL");
        //    driver.navigate().to(URL);
    }


    public static void screenCaptureTest() throws IOException, InterruptedException {

        // This part is to read data from file
        String ID;
        System.out.println("0");

        File testDataSrc = new File(prop.getProperty("testDataLocation"));
        FileInputStream testData = new FileInputStream(testDataSrc);
        XSSFWorkbook wb = new XSSFWorkbook(testData);
        XSSFSheet sheet1 = wb.getSheetAt(0);

        File newPath = new File(prop.getProperty("ScreenshotLocation"));
        String templateName;
        Ads Ad1 = new Ads(driver);


        for(int i=1; i < 67; i++) {

            ID = sheet1.getRow(i).getCell(1).getStringCellValue();

            if (prop.getProperty("device").equals("desktop")) {
                driver.get(URL + "/id/" + ID);
            }
            else{
                driver.get(URL + "/id/" + ID+ "?$DEVICE$=mobile-touch");
            }

            Thread.sleep(2000);

            // Screenshot of File
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Copy folder to new Path

            templateName = sheet1.getRow(i).getCell(0).getStringCellValue();
            if (prop.getProperty("device").equals("desktop")) {
                FileUtils.copyFile(scrFile, new File(newPath + "/" + templateName + ".png"));
            }
            else {
                FileUtils.copyFile(scrFile, new File(newPath + "/" + templateName + "- MOBILE.png"));
            }
            System.out.println(newPath);
            System.out.println("Screenshot of :" + "/" + templateName + " Done.");

            WebElement ads = Ad1.getTopBanner();
            WebElement x300 = Ad1.getFlexAd();

    try {
        if (ads.isDisplayed()) {
            Point point = ads.getLocation();
//Get width and height of the element
            BufferedImage fullImg = ImageIO.read(scrFile);
            int eleWidth = ads.getSize().getWidth();
            int eleHeight = ads.getSize().getHeight();
//Crop the entire page screenshot to get only element screenshot
            BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth,
                    eleHeight);
            ImageIO.write(eleScreenshot, "png", scrFile);
//Copy the element screenshot to disk
            String adName = ads.getAttribute("id");
            FileUtils.copyFile(scrFile, new File(newPath + "/"  +templateName+ " "+ adName + ".png"));
            System.out.println("Success displayed ad: " +adName);
        }
    }catch (Exception e){
        System.out.println("Ad not found");
    }
            try {
                if (x300.isDisplayed()) {
                    Point point = x300.getLocation();
//Get width and height of the element
                    BufferedImage fullImg = ImageIO.read(scrFile);
                    int eleWidth = x300.getSize().getWidth();
                    int eleHeight = x300.getSize().getHeight();
//Crop the entire page screenshot to get only element screenshot
                    BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth,
                            eleHeight);
                    ImageIO.write(eleScreenshot, "png", scrFile);
//Copy the element screenshot to disk
                    String adName = x300.getAttribute("id");
                    FileUtils.copyFile(scrFile, new File(newPath + "/"  +templateName+ " "+ adName + ".png"));
                    System.out.println("Success displayed ad: " +adName);
                }
            }catch (Exception e){
                System.out.println("Ad not found");
            }


        }

        // Open the folder of screenshots
        Desktop.getDesktop().open(newPath);


    }

    public static Properties loadProp() throws IOException {

        File file = new File("C:\\Users\\~206109371\\Documents\\My Downloads\\java1\\src\\screenConfig.prop");
        FileInputStream fileInput = new FileInputStream(file);
        Properties prop = new Properties();
        prop.load(fileInput);
        fileInput.close();

        return prop;
    }
    public static void tearDown(){

        //Comments
        driver.quit();
    }

}