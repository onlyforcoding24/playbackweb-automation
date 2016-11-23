package com.ooyala.playback.page;


import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.url.PlayerPropertyValue;
import com.ooyala.playback.url.Testdata;

public class AdClickThroughValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger log = Logger.getLogger(AdClickThroughValidator.class);
	
	public AdClickThroughValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("adclicks");
	}

	public void validate(String element, int timeout) throws Exception {
		waitOnElement(element, timeout);
	}

	public void clickThroughAds(Testdata testData, int index) throws Exception{
		
		String value = testData.getTest().get(0).getUrl().get(index).getAdPlugins().getValue();
		
		String baseWindowHdl = driver.getWindowHandle();
		if(value!=null){
			
			if(!getPlatform().equalsIgnoreCase("Android")) {// we skipping this code for IMA and Vast (Android)
				if(value!=PlayerPropertyValue.FREEWHEEL.toString()){
					if(value==PlayerPropertyValue.VAST.toString()){
		                clickOnIndependentElement("adScreenPanel");
					} else{
/*						WebElement adPanel = getWebElementsList("adScreenPanel1").get(0);
		                ((JavascriptExecutor) driver).executeScript("arguments[0].click()", adPanel);*/
						clickOnIndependentElement("adScreenPanel1");
					}
					waitOnElement("adsClicked_1", 10);
					waitOnElement("adsClicked_videoWindow", 10);
					
				}
			}
			if(value!=PlayerPropertyValue.IMA.toString()){
				try {
	                clickOnHiddenElement("learnMore");
	                waitOnElement("adsClicked_learnMoreButton", 5);
	            }catch (Exception e) {
	            	clickOnIndependentElement("learnMore");
	                waitOnElement("adsClicked_learnMoreButton", 20);
	            }
			}
			extentTest.log(PASS, "AdsClicked by clicking on the learn more button");
			sleep(2000);
			java.util.Set<java.lang.String> windowHandles = driver.getWindowHandles();
	        int count = windowHandles.size();
	        log.info("Window handles : "+count);
	        
	        for (String winHandle : driver.getWindowHandles()) {
	              if(!winHandle.equals(baseWindowHdl)) {
	                  driver.switchTo().window(winHandle);
	                  driver.close();
	                  driver.switchTo().window(baseWindowHdl);
	               }
	        }
	        
	        boolean isAd = isAdPlaying();
	        if(isAd) {

	            if(getPlatform().equalsIgnoreCase("Android") ) //TODO : || Description.contains("HLS")
	            {
	                ((JavascriptExecutor) driver).executeScript("pp.play()");
	            }
	            else
	            {
	            	/*WebElement adPanel = getWebElementsList("adPanel").get(0);
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click()", adPanel);*/
	            	clickOnIndependentElement("adPanel");
	            }

	        }
	        
		}else{
			throw new Exception("PlayerPropertyValue should not be null.");
		}
	}
	
	public boolean isAdPlaying(){
        Boolean isAdplaying = (Boolean)(((JavascriptExecutor) driver).executeScript("return pp.isAdPlaying()"));
        return isAdplaying;
    }
}