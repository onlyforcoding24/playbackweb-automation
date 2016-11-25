package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.Url;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPostrollDiscoveryTests extends PlaybackWebTest{

	public PlaybackPostrollDiscoveryTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private PauseValidator pauseValidator;
	private DiscoveryValidator discoveryValidator;
	private SeekAction seekAction;
	private UpNextValidator upNextValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPostrollDiscovery(String testName, String url)
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

			playValidator.validate("playing_1", 150);
			
			pauseValidator.validate("paused_1", 60);
	        Thread.sleep(5000);
	        
	        discoveryValidator.validate("reportDiscoveryClick_1", 60);
	        

	        loadingSpinner();
	        
	        playAction.startActionOnScreen();
	        
	        Thread.sleep(3000);
	        seekAction.seek(10, true);

	        loadingSpinner();
	        
	        upNextValidator.validate("", 60);

	        event.validate("willPlaySingleAd_1", 90);
	        extentTest.log(PASS, "Postroll Ad started");
	        
	        Map<String, String> map = parseURL(url) ;

	        if(map!=null && map.get("ad_plugin")!=null && map.get("ad_plugin").contains("pulse")) {
	        	event.validate("singleAdPlayed_2", 90);
	        }
	        else{
	        	event.validate("singleAdPlayed_1", 90);
	        }
	        extentTest.log(PASS, "Postroll Ad completed");
	        extentTest.log(PASS, "Verified PostRoll Ads test");

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");

	}

}