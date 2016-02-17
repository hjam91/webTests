import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by 206436732 on 12/1/2015.
 */
public class Ads {

    WebDriver driver;

    @FindBy(id = "dart_wrapper_Top_Banner")
    WebElement leaderBoardAD;

  /*  @FindBy(id = "")
    WebElement flex;*/

    @FindBy(id = "dart_wrapper_Flex_Ad_First")
    WebElement x300;

    @FindBy(id = "prtnr_mod_upsell")
    WebElement xfinityUpsell;

    @FindBy(id="pmtracker")
    WebElement perfectMarketAd;

    @FindBy(id="pmad_right-2-frame")
    WebElement MarketsAdFrame;

    @FindBy(id="ad-com-rightrail-bottom")
    WebElement topBannerInter;

    @FindBy(id="dart_wrapper_Badge_A")
    WebElement badge_A;

    @FindBy(partialLinkText = "Switch to mobile view")
    WebElement swtichDevices;

    Ads(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    public WebElement getSwtichDevices(){ return swtichDevices;}

    public WebElement getBadge_A(){ return badge_A;}

    public WebElement getMarketsAdFrame(){ return MarketsAdFrame;}

    public WebElement getFlexAd(){
        return x300;
    }

    public WebElement getPerfectMarketAd() { return perfectMarketAd; }

    public WebElement getTopBanner(){
        return leaderBoardAD;
    }

    public WebElement getTopBannerInter(){
        return topBannerInter;
    }

    public WebElement getXfinityUpsell(){
        return xfinityUpsell;
    }



}
