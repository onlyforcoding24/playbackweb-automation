package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static java.lang.Thread.sleep;

/**
 * Created by soundarya on 11/17/16.
 */
public class Bitratevalidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(Bitratevalidator.class);

	public Bitratevalidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("play");
		addElementToPageElements("pause");
		addElementToPageElements("bitrate");
	}

	public boolean validate(String element, int timeout) throws Exception {

		try {
			if (!isElementPresent("BITRATE"))
				return false;
		} catch (Exception e) {
			if (!clickOnIndependentElement("MORE_OPTION_ITEM"))
				return false;
			sleep(1000);
			if (!isElementPresent("BITRATE"))
				return false;
		}

		if (!clickOnIndependentElement("BITRATE"))
			return false;

		try{
			if(isElementPresent("BITRATE_CLOSE_BTN")) {
				clickOnIndependentElement("BITRATE_CLOSE_BTN");
			}
		} catch (Exception e){
			logger.info("Bitrate close button is not present");
		}

		int length = Integer.parseInt(((JavascriptExecutor) driver)
				.executeScript("return pp.getBitratesAvailable().length")
				.toString());
		if (length > 1) {
			boolean flag = true;
			for (int i = 0; i < length - 1; i++) {
				((JavascriptExecutor) driver)
						.executeScript(" return pp.pause()");

				String bitrate = ((JavascriptExecutor) driver).executeScript(
						"return pp.getBitratesAvailable()[" + i
								+ "]['bitrate']").toString();
				((JavascriptExecutor) driver)
						.executeScript("return pp.setTargetBitrate('" + i
								+ "')");
				String bufferedLengthOfVideo = ((JavascriptExecutor) driver)
						.executeScript(" return pp.getBufferLength().toFixed()").toString();
				int bufferedLength = Integer.parseInt(bufferedLengthOfVideo);
				logger.info("Buffered length is "+bufferedLength);
				String totalDurationOfVideo = ((JavascriptExecutor) driver)
						.executeScript(" return pp.getDuration().toFixed()").toString();
				int totalDuration = Integer.parseInt(totalDurationOfVideo);
				logger.info("Total time duration is "+totalDuration);

				((JavascriptExecutor) driver)
							.executeScript("pp.seek(pp.getBufferLength()-15)");

				((JavascriptExecutor) driver)
							.executeScript("pp.seek(10)");

				Thread.sleep(5000);

				loadingSpinner();

				((JavascriptExecutor) driver)
						.executeScript(" return pp.play()");
				flag = flag && waitOnElement(
						By.id("bitrateChanged_" + (bitrate)), 25000);
			}
			return flag;
		} else {
			String currentBitrate = ((JavascriptExecutor) driver)
					.executeScript("return pp.getCurrentBitrate()[\"bitrate\"]")
					.toString();
			((JavascriptExecutor) driver).executeScript("return pp.play()");
			// Assert.assertNotNull(currentBitrate);
			if (currentBitrate == null) {
				return false;
			}
			return true;
		}

	}
}
