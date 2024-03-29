package com.ooyala.playback.amf.vast;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.FullScreenAction;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackAdWrapperAndLiveRailTests extends PlaybackWebTest {

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private FullScreenAction fullScreenAction;
	private VolumeValidator volumeValidator;
	private PauseAction pauseAction;

	public PlaybackAdWrapperAndLiveRailTests() throws OoyalaException {
		super();
	}

	@Test(groups = {"amf"}, dataProvider = "testUrls", enabled=false)
	public void verifyPlaybackAdWrapper(String testName, UrlObject url)
			throws Exception {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();
			injectScript();

			result = result && playValidator.validate("playing_1", 10000);

			result = result && pauseAction.startAction();
			result = result && fullScreenAction.startAction();

			result = result && volumeValidator.validate("", 10000);
			result = result && seekValidator.validate("seeked_1", 5000);
			result = result && event.validate("played_1", 5000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback Ad Wrapper tests failed");

	}
}
