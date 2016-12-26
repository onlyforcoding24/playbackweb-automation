package com.ooyala.playback.FCC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
/**
 * Created by snehal on 26/12/16.
 */

public class PlaybackFCCDefaultSettingTests extends PlaybackWebTest {

    private static Logger logger = Logger
            .getLogger(PlaybackFCCDefaultSettingTests.class);

    private PlayValidator play;
    private PauseValidator pause;
    private EventValidator eventValidator;
    private CCValidator cc;
    private FCCValidator fcc;
    private FullScreenValidator fullscreen;

    public PlaybackFCCDefaultSettingTests() throws OoyalaException {
        super();
    }

    @Test(groups = "FCC", dataProvider = "testUrls")
    public void testFCCDefaultSetting(String testName, String url) throws OoyalaException {

        boolean result = true;

        try{
            driver.get(url);

            result = result && fcc.clearCache();
            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1",30000);

            result = result && eventValidator.loadingSpinner();

            Thread.sleep(2000);

            result = result && pause.validate("paused_1",30000);

            result = result && fcc.closedCaptionMicroPanel();
            result = result && fcc.beforeRefreshCCSetting();
            Thread.sleep(2000);

            driver.navigate().refresh();

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1",30000);

            result = result && eventValidator.loadingSpinner();

            Thread.sleep(2000);

            result = result && pause.validate("paused_1",30000);

            result = result && fcc.closedCaptionMicroPanel();
            result = result && fcc.afterRefreshCCSettings();

        }catch(Exception e){
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(result, "Playback FCC CC Default Setting tests failed");
    }
}
