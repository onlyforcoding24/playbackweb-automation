package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdSkipButtonValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackSkipAdsTests extends PlaybackWebTest {

	public PlaybackSkipAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private AdSkipButtonValidator skipButtonValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPrerollOverlay(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("willPlaySingleAd_1", 150000);

			result = result && skipButtonValidator.validate("", 120000);

			result = result && event.validate("singleAdPlayed_1", 150000);
			result = result && event.validate("playing_1", 150000);

			result = result && seekValidator.validate("seeked_1", 150000);

			result = result && event.validate("played_1", 150000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Test failed");

	}

}
