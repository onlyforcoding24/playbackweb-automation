package com.ooyala.playback.playerlifecycle;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPlayerDestroyTests extends PlaybackWebTest {

	private static Logger logger = Logger
			.getLogger(PlaybackPlayerDestroyTests.class);

	private PlayValidator play;
	private EventValidator eventValidator;

	public PlaybackPlayerDestroyTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerLifecycle", dataProvider = "testUrls")
	public void testVideoReplay(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			executeScript("pp.destroy();");

			result = result && eventValidator.validate("destroy_1", 50000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		Assert.assertTrue(result, "Player Destroy tests failed");
	}
}
