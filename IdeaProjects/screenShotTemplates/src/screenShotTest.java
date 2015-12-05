package test.java.com.mycompany.app;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by chrx on 11/20/15.
 */
public class screenShotTest {

    static WebDriver driver;
    static String URL ;
    static Properties prop;
    static StringBuilder result;





    @BeforeClass
    public static void setUp() throws IOException, InterruptedException {

        prop = loadProp();
        URL = prop.getProperty("URL");
        String BROWSER = prop.getProperty("browser");


        if(BROWSER.equals("phantom")){
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                    prop.getProperty("phantomDriverLocation"));
            driver = new PhantomJSDriver(caps);
        }

        else if (BROWSER.equals("chrome")) {

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

    @Test
    public static void screenCaptureTest() throws IOException, InterruptedException {

        // This part is to read data from file
        String ID;
        System.out.println("0");

        File testDataSrc = new File(prop.getProperty("testDataLocation"));
        FileInputStream testData = new FileInputStream(testDataSrc);
        XSSFWorkbook wb = new XSSFWorkbook(testData);
        XSSFSheet sheet1 = wb.getSheetAt(0);
        result = new StringBuilder();
        result.append("<html>")
                .append("<head><title> MPS3 Ad Testing Results </title></head>")
                .append("<h1>MPS3 Ad Testing Results </h1>")
                .append("<table border=\"1\">");

        File testDataSrcResults = new File(prop.getProperty("testDataLocationResults"));
        //FileInputStream testDataResults = new FileInputStream(testDataSrcResults);
        // FileOutputStream testDataResults = new FileOutputStream(testDataSrcResults);
       // XSSFWorkbook wbResults = null;

        /*try {
            wbResults = new XSSFWorkbook(testDataSrcResults);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet1Results = wbResults.getSheetAt(0);
*/


        File newPath = new File(prop.getProperty("ScreenshotLocation"));

        String templateName;
        Ads Ad1 = new Ads(driver);

        //  FileOutputStream fileOut = new FileOutputStream(prop.getProperty("testDataLocationResults"));
        System.out.println("5");

        result.append("<th>").append("</td> Templates</td>")
                .append("<td> ID </td>")
                .append("<td> Flex Ad </td>")
                .append("<td> Top Banner</td>")
                .append("<td> Perfect Market Module</td>")
                .append("<td> Screenshot </td>")
                .append("</th>");

        System.out.println("Physical Number of Rows:" + (sheet1.getPhysicalNumberOfRows()-1));
        for (int i = 1; i < 30; i++) {

            ID = sheet1.getRow(i).getCell(1).getStringCellValue();

            if (prop.getProperty("device").equals("desktop")) {
                driver.get(URL + "/id/" + ID);
            } else {
                driver.get(URL + "/id/" + ID + "?$DEVICE$=mobile-touch");
            }

            Thread.sleep(5000);

            // Screenshot of File
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File scrFile1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File scrFile2 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Copy folder to new Path

            templateName = sheet1.getRow(i).getCell(0).getStringCellValue();
            templateName = templateName.replace(' ', '-');
            if (prop.getProperty("device").equals("desktop")) {
                FileUtils.copyFile(scrFile, new File(newPath + "/" + templateName + "/" + templateName + ".png"));
            } else {
                FileUtils.copyFile(scrFile, new File(newPath + "/" + templateName + "- MOBILE.png"));
            }
            System.out.println(newPath);
            System.out.println("Screenshot of :" + "/" + templateName + " Done.");

            result.append("<tr>")
                    .append("<td><a href="+ URL + "/id/" + ID+ ">" + templateName +" </a></td>")
                    .append("<td>"+ ID+" </td>");
            try {

                adVerify(Ad1.getTopBanner(), scrFile, newPath, templateName);
                writePass(result,templateName,ID);

            } catch (Exception e) {
                writeFailed(result,templateName,ID);
            }

            try {

                adVerify(Ad1.getFlexAd(), scrFile1, newPath, templateName);
                writePass(result,templateName,ID);

            } catch (Exception e) {
                writeFailed(result,templateName,ID);
            }

            try {
              //  driver.switchTo().frame(Ad1.getMarketsAdFrame());
                adVerify(Ad1.getMarketsAdFrame(), scrFile2, newPath, templateName);
             //   driver.switchTo().defaultContent();
                writePass(result,templateName,ID);
            } catch (Exception e) {
                writeFailed(result,templateName,ID);
            }

            result.append("<td><a href="+newPath+ "/" +templateName+ "/> Link </a></td>")
                    .append("</tr>");
            }

            result.append("</table>")
                    .append("</html>");



            //fileOut.close();
            // Open the folder of screenshots
            Desktop.getDesktop().open(newPath);
        }


    public static void adVerify(WebElement ad1,File scrFile1, File newPath, String templateName ) throws IOException {
        if (ad1.isDisplayed()) {
            System.out.println("1");
            Point point = ad1.getLocation();
//Get width and height of the element
            System.out.println("2");
            BufferedImage fullImg = ImageIO.read(scrFile1);
            int eleWidth = ad1.getSize().getWidth();
            int eleHeight = ad1.getSize().getHeight();
//Crop the entire page screenshot to get only element screenshot
            System.out.println("3");
            BufferedImage eleScreenshot1 = fullImg.getSubimage(point.getX(), point.getY(), eleWidth,
                    eleHeight);
            ImageIO.write(eleScreenshot1, "png", scrFile1);
//Copy the element screenshot to disk
            System.out.println("4");
            String adName = ad1.getAttribute("id");
            FileUtils.copyFile(scrFile1, new File(newPath + "/" + templateName + "/" + templateName+ " " + adName + ".png"));
            System.out.println("Success displayed ad: " + adName);
        }
    }

    public static Properties loadProp() throws IOException {

        File file = new File("/home/fortegs/IdeaProjects/java1/src/screenConfig.prop");
        FileInputStream fileInput = new FileInputStream(file);
        Properties prop = new Properties();
        prop.load(fileInput);
        fileInput.close();

        return prop;
    }

    public static void writePass( StringBuilder result, String templateName, String ID){

            result
                .append("<td style=\"background-color:#00FF00;\"> Pass </td>");
        System.out.println("Ad Found: Success");
    }

    public static void writeFailed( StringBuilder result, String templateName, String ID){

        result.append("<td style=\"background-color:#FF0000;\"> Fail </td>");
        System.out.println("Ad not found");
    }


    @AfterClass
    public static void tearDown() throws IOException {

        BufferedWriter bwr = new BufferedWriter(new FileWriter(new File("/home/fortegs/Results/results.html")));
        bwr.write(result.toString());
        bwr.flush();
        bwr.close();
        //Comments
        driver.quit();
    }


   

}