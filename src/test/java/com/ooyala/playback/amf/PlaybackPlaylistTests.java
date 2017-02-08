package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlaylistValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPlaylistTests extends PlaybackWebTest {

	private PlaylistValidator playlist;
	private PlayValidator play;
	private PlayAction playAction;
	private EventValidator event;
	private DiscoveryValidator discoveryValidator;

	public PlaybackPlaylistTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "playlist", "amf" }, dataProvider = "testUrls")
	public void testPlaylistTests(String testName, String url) throws OoyalaException {

		boolean result = true;
		try {

			driver.get(url);
			result = result && play.waitForPage();

			injectScript();

			result = result && playAction.startAction();
			
			result = result && event.validate("PreRoll_willPlayAds", 1000);
			
			result = result && playlist.scrollToEitherSide() && playlist.getOrientation("vertical");
			result = result && playlist.getPosition("bottom");
			result = result && playlist.getCaptionPosition("inside");
			
			
			result = result && event.validate("adsPlayed_1", 160000);
			
			if(testName.contains("OOYALA_ADS")){
				result = result && event.validate("ooyalaAds", 1000);
			}
			
			result = result && event.validate("playing_1", 20000);

			if(testName.contains("DISCOVERY")){
				result = result && discoveryValidator.validate("reportDiscoveryClick_1", 60000);
			}

		} catch (Exception e) {
			e.getMessage();
			result = false;
		}
		Assert.assertTrue(result, "Playback Playlist tests failed" + testName);
	}
}
