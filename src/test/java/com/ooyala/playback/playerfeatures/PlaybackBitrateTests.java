package com.ooyala.playback.playerfeatures;

import static java.lang.Thread.sleep;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.Bitratevalidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/17/16.
 */
public class PlaybackBitrateTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackBitrateTests.class);
	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private EventValidator eventValidator;
	private Bitratevalidator bitratevalidator;

	public PlaybackBitrateTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testBitrate(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);
			sleep(4000);

            result = result && pause.validate("paused_1", 60000);

            result = result && bitratevalidator.validate("", 60000);

			sleep(1000);

            result = result && seek.validate("seeked_1", 60000);

            result = result && eventValidator.validate("videoPlayed_1", 60000);
			logger.info("Verified that video is played");

		} catch (Exception e) {
			e.printStackTrace();
            result = false;

		}
		Assert.assertTrue(result, "Playback bitrate/Quality tests failed");

	}

}