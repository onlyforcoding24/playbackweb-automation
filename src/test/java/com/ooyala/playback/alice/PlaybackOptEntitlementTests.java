package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.SaasPortValidator;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by snehal on 23/11/16.
 */
public class PlaybackOptEntitlementTests extends PlaybackWebTest {

    private EventValidator eventValidator;
    private PlayValidator play;
    private SeekValidator seek;
    private SaasPortValidator sasport;

    public PlaybackOptEntitlementTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testOptEntitlementAlice(String testName, String url) throws OoyalaException {
        boolean result = false;
        try {
            sasport.validate("CREATE_ENTITLEMENT",300);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }
            driver.get(url);
            play.waitForPage();
            Thread.sleep(10000);

            injectScript();
            play.validate("playing_1", 60);

            logger.info("Verified that video is getting played");

            seek.validate("seeked_1", 60);

            logger.info("Verified that video is seeked");

            eventValidator.validate("played_1",60);

            logger.info("Verified that video is played");

            sasport.validate("DISPLAY_BTN",10);

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result, "OPT ENtitlement tests failed");
    }
}
