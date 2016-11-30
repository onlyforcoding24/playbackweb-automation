package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMidRollAdsTests extends PlaybackWebTest {

	public PlaybackMidRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekAction seekAction;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyMidRoll(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

            result = result && playValidator.validate("playing_1", 60000);
			extentTest.log(PASS, "Video started playing");
			Thread.sleep(2000);

			seekAction.seekSpecific(15);

			loadingSpinner();
			event.validate("videoPlaying_1", 90000);
            result = result && event.validate("MidRoll_willPlaySingleAd_1", 120000);
			extentTest.log(PASS, "Midroll Ad started to play");
            result = result && event.validate("singleAdPlayed_1", 160000);
			extentTest.log(PASS, "Midroll Ad ended");
			
			event.validateForSpecificPlugins("singleAdPlayed_2", 160000, "pulse");

			seekAction.seekSpecific(10);

			event.validate("videoPlayed_1", 160000);
            result = result &&event.validate("played_1", 160000);
			extentTest.log(PASS, "Video Played");
			extentTest.log(PASS, "Verified MidrollAdsTest");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}
}
