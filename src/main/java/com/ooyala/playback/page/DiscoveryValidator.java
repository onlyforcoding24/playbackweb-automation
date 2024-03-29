package com.ooyala.playback.page;

import static java.lang.Thread.sleep;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

public class DiscoveryValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger logger = Logger.getLogger(DiscoveryValidator.class);

	public DiscoveryValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("discovery");
		addElementToPageElements("play");
		addElementToPageElements("pause");

	}
	
	public boolean validateDiscoveryToaster() throws Exception{

		if (isElementPresent("PAUSE_BUTTON")) {
			clickOnIndependentElement("PAUSE_BUTTON");
		}
		return waitOnElement("DISCOVERY_TOASTER", 10000);
		
	}
	
	public boolean validateLeftRightButton() throws Exception{

		List<WebElement> count = getWebElementsList("DISCOVERY_IMG_WRAPPER");

		logger.info("Count Value :" + count.size());
		logger.info("Number of Discovery Videos " + count.size());

		boolean flagTrue = false;
		try {
			flagTrue = isElementVisible("RIGHT_BTN");
			logger.info("Is right button showing on Discovery Screen  "
					+ flagTrue);
		} catch (Exception e) {
			logger.info("Max videos are showing on Discovery screen");
			return false;
		}
		if (count.size() > 3 && flagTrue) {
			if (!clickOnIndependentElement("RIGHT_BTN"))
				return false;
			sleep(2000);
			if (!clickOnIndependentElement("LEFT_BTN"))
				return false;
			extentTest.log(LogStatus.PASS,
					"verified discovery left right button");
		}
		return true;
	}
	
	public boolean validateImageStyle(){
		if(!clickOnIndependentElement("IMAGE_STYLE")) return false;
        if(!waitOnElement(By.id("reportDiscoveryClick_1"), 60000)) return false;
		return true;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		
		if(!loadingSpinner()){
			extentTest.log(LogStatus.FAIL, "Loading spinner is persistent!");
			return false;
		}
		
		Thread.sleep(5000);

		if (validateDiscoveryToaster() && validateLeftRightButton()
				&& validateImageStyle()) {
			return waitOnElement(By.id("reportDiscoveryImpression_1"), 60000)
					&& waitOnElement(By.id("setEmbedCode_1"), 60000)
					&& waitOnElement(By.id("playbackReady_1"), 60000)
					&& waitOnElement(By.id("videoPreload_1"), 60000);
		}

		return false;
	}

	public boolean verifyDiscoveryEnabled(String Onevent,boolean isPresent) {
		boolean discoverytray = isElementPresent("DISCOVERY_STYLE");
		boolean discoveryscreen = isElementPresent("CONTENT_SCREEN");
		logger.info("discovery screen is enabled " + Onevent + ": "
				+ discoveryscreen);
		logger.info("discovery Toaster is Shown " + Onevent + ": "
				+ discoverytray);
        if (isPresent)
		    return discoverytray && discoveryscreen;
        else
            return !discoverytray && !discoveryscreen;

	}

	public boolean clickOnDiscoveryCloseButton() {
		return waitOnElement("DISCOVERY_CLOSE_BTN", 20000)
				&& clickOnIndependentElement("DISCOVERY_CLOSE_BTN");
	}
	
	public boolean clickOnDiscoveryButton() throws Exception {
		if(!loadingSpinner()){
			extentTest.log(LogStatus.FAIL, "Loading spinner is persistent!");
			return false;
		}
		return clickOnIndependentElement("DISCOVERY_BTN") && validateDiscoveryToaster() && validateLeftRightButton();
	}
}
