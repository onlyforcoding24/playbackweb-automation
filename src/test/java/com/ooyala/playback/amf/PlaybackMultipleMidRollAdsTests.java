package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.Url;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMultipleMidRollAdsTests extends PlaybackWebTest {

	public PlaybackMultipleMidRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private PoddedAdValidator poddedAdValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyMultipleMidroll(String testName, String url)
			throws OoyalaException {

		boolean result = false;

		try {

			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();
			
			playValidator.validate("playing_1", 90);
			
			Map<String,String> map = parseURL(url);
			
			if(map!=null && map.get("ad_plugin")!=null && !map.get("ad_plugin").contains("pulse"))
				seekAction.seekPlayback(); 

	        event.validate("videoPlayed_1", 200);
	        
	        poddedAdValidator.validate("countPoddedAds", 60); // TODO : need to check diff between willPlayAds_ and willPlaySingleAds_

	        if(map!=null && map.get("ad_plugin")!=null && !map.get("ad_plugin").contains("pulse"))
	            event.validate("seeked_1", 200); 

	        event.validate("played_1", 200);
	        extentTest.log(PASS, "Verified Multiple MidRoll Ads");
			

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");

	}
}