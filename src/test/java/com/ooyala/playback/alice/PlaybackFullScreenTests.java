package com.ooyala.playback.alice;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackFullScreenTests extends PlaybackWebTest {

	private PlayValidator play;
	private SeekValidator seek;
	private EventValidator eventValidator;
	private FullScreenValidator fullScreenValidator;

	public PlaybackFullScreenTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Player", dataProvider = "testUrls")
	public void testPlaybackFullscreen(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

            result = result && play.waitForPage();

			injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && fullScreenValidator.validate("", 60000);

            result = result && seek.validate("seeked_1", 60000);

            result = result && eventValidator.validate("played_1", 60000);

			logger.info("video played");
		} catch (Exception e) {
			e.printStackTrace();
            result = false;
		}
		Assert.assertTrue(result, "Playback FullScreen tests failed");
	}
}