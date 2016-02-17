import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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

            System.setProperty("webdriver.ie.driver", "C:\\Users\\206436732\\IEDriverServer.exe");
            DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
            caps.setCapability("ignoreZoomSetting", true);
            driver = new InternetExplorerDriver(caps);

        } else {

            FirefoxProfile firefoxProfile = new FirefoxProfile();
            firefoxProfile.setPreference("media.volume_scale", "0.0");
            driver = new FirefoxDriver(firefoxProfile);
        }
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
                .append("<h1>MPS3 Ad Testing Results " +"("+prop.getProperty("browser") +  ")</h1>")
                .append("<table border=\"1\">");

//        File testDataSrcResults = new File(prop.getProperty("testDataLocationResults"));
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

        double countPassAd1 = 0;
        double countPassAd2 = 0;
        double countPassAd3 = 0;
        double countPassAd4= 0;
        double countPassAd5 = 0;
        double countPassAd6 = 0;

        double countFailAd1 = 0;
        double countFailAd2 = 0;
        double countFailAd3 = 0;
        double countFailAd4 = 0;
        double countFailAd5 = 0;
        double countFailAd6 = 0;

        //  FileOutputStream fileOut = new FileOutputStream(prop.getProperty("testDataLocationResults"));
        System.out.println("5");

        result.append("<th>").append("</td> Templates</td>")
                .append("<td> ID </td>")
                .append("<td> Flex Ad </td>")
                .append("<td> Top Banner</td>")
                .append("<td> Perfect Market Module</td>")
                .append("<td> Xfinity Upsell</td>")
                .append("<td> Badge A</td>")
                .append("<td> Switch Link </td>")
                .append("<td> Screenshot </td>")
                .append("<td> Seconds </td>")
                .append("</th>");

        System.out.println("Physical Number of Rows:" + (sheet1.getPhysicalNumberOfRows()-1));
        for (int i = 1; i <  /*(sheet1.getPhysicalNumberOfRows()-1)*/ 10 ; i++) {

            ID = sheet1.getRow(i).getCell(1).getStringCellValue();
            long startTime = System.currentTimeMillis();

            if (prop.getProperty("device").equals("desktop")) {
                driver.get(URL + "/id/" + ID);

                if (prop.getProperty("X").equals("xfinity")) {
                    if (i < 2) {
                        Cookie newCookie = new Cookie("active_partner_exp", "xfinity", "/");
                        driver.manage().addCookie(newCookie);
                        driver.navigate().refresh();
                        Thread.sleep(3000);
                    }

                }
            }

            else if (prop.getProperty("device").equals("mobile")) {
                driver.get(URL + "/id/" + ID + "?$DEVICE$=mobile-touch");
             }

            else if (prop.getProperty("device").equals("tablet")) {
                driver.get(URL + "/id/" + ID + "?$DEVICE$=mobile-tablet");
            }

            else if (prop.getProperty("device").equals("android")) {
                driver.get(URL + "/id/" + ID + "?$DEVICE$=mobile-tablet");
            }


            long endTime = System.currentTimeMillis();
            Thread.sleep(5000);

            // Screenshot of File
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
         //  File scrFile1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
          //  File scrFile2 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

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

                adVerify(Ad1.getTopBanner(), newPath, templateName);
                writePass(result,templateName,ID);
                countPassAd1++;

            } catch (Exception e) {
                writeFailed(result,templateName,ID);
                countFailAd1++;

            }

            try {

                adVerify(Ad1.getFlexAd(), newPath, templateName);
                writePass(result,templateName,ID);
                countPassAd2++;


            } catch (Exception e) {
                writeFailed(result,templateName,ID);
                countFailAd2++;
            }

            try {
                //  driver.switchTo().frame(Ad1.getMarketsAdFrame());
                WebDriverWait wait = new WebDriverWait(driver, 20);
                adVerify(wait.until(ExpectedConditions.elementToBeClickable(Ad1.getMarketsAdFrame())), newPath, templateName);
                //   driver.switchTo().defaultContent();
                writePass(result,templateName,ID);
                countPassAd3++;
            } catch (Exception e) {
                writeFailed(result,templateName,ID);
                countFailAd3++;
            }

            try {
                //  driver.switchTo().frame(Ad1.getMarketsAdFrame());
                adVerify(Ad1.getXfinityUpsell(), newPath, templateName);
                //   driver.switchTo().defaultContent();
                writePass(result,templateName,ID);
                countPassAd4++;

            } catch (Exception e) {
                writeFailed(result,templateName,ID);
                countFailAd4++;
            }

            try {
                //  driver.switchTo().frame(Ad1.getMarketsAdFrame());
                adVerify(Ad1.getBadge_A(), newPath, templateName);
                //   driver.switchTo().defaultContent();
                writePass(result,templateName,ID);
                countPassAd5++;

            } catch (Exception e) {
                writeFailed(result,templateName,ID);
                countFailAd5++;
            }

            try {
                //  driver.switchTo().frame(Ad1.getMarketsAdFrame());
                adVerify(Ad1.getSwtichDevices(), newPath, templateName);
                //   driver.switchTo().defaultContent();
                writePass(result,templateName,ID);
                countPassAd6++;

            } catch (Exception e) {
                writeFailed(result,templateName,ID);
                countFailAd6++;
            }





            long totalTime = (endTime - startTime) / 1000;

            System.out.println("Total Page Load Time: " + totalTime + "milliseconds");

                    result.append("<td><a href="+newPath+ "/" +templateName+ "/> Link </a></td>")
                            .append("<td>" + totalTime + "</td>")
                            .append("</tr>");
        }


        result.append("<tr>")
                .append("<td> Total </td>")
                .append("<td> Results </td>")
                .append("<td>" +(int)(countPassAd1/(countFailAd1+countPassAd1) *100) + "% </td>")
                .append("<td>" + (int) (countPassAd2 / (countFailAd2 + countPassAd2) * 100) + "% </td>")
                .append("<td>" + (int)(countPassAd3 / (countFailAd3 + countPassAd3) *100) + "% </td>")
                .append("<td>" + (int) (countPassAd4 / (countFailAd4 + countPassAd4) * 100) + "% </td>")
                .append("<td>" + (int)(countPassAd5 / (countFailAd5 + countPassAd5) *100) + "% </td>")
                .append("<td>" + (int)(countPassAd6 / (countFailAd6 + countPassAd6) *100) + "% </td>")
                .append("</tr>");



        result.append("</table>")
                .append("</html>");



        //fileOut.close();
        // Open the folder of screenshots

    }


    public static void adVerify(WebElement ad1, File newPath, String templateName ) throws IOException {
        if (ad1.isDisplayed()) {
            System.out.println("1");
            Point point = ad1.getLocation();
//Get width and height of the element
            System.out.println("2");

           /* BufferedImage fullImg = ImageIO.read(scrFile1);
            int eleWidth = ad1.getSize().getWidth();
            int eleHeight = ad1.getSize().getHeight();
//Crop the entire page screenshot to get only element screenshot
            System.out.println("3");
            BufferedImage eleScreenshot1 = fullImg.getSubimage(point.getX(), point.getY(), eleWidth,
                    eleHeight);
            ImageIO.write(eleScreenshot1, "png", scrFile1);
//Copy the element screenshot to disk
            System.out.println("4");
            */
            String adName = ad1.getAttribute("id");

         //   FileUtils.copyFile(scrFile1, new File(newPath + "/" + templateName + "/" + templateName+ " " + adName + ".png"));

            System.out.println("Success displayed ad: " + adName);
        }
    }

    public static Properties loadProp() throws IOException {

        File file = new File("C:\\Users\\~206109371\\Documents\\My Downloads\\java1\\src\\screenConfig.prop");
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

        BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(prop.getProperty("results"))));
        bwr.write(result.toString());
        bwr.flush();
        bwr.close();
        //Comments
        File Results = new File(prop.getProperty("results"));
        driver.quit();
        Desktop.getDesktop().open(Results);

    }

}
