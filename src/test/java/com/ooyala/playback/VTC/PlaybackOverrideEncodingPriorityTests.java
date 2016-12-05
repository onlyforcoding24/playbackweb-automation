package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EncodingValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import static java.net.URLDecoder.decode;

/**
 * Created by jitendra on 28/11/16.
 */
public class PlaybackOverrideEncodingPriorityTests extends PlaybackWebTest {

    private PlayValidator play;
    private PlayAction playAction;
    private SeekValidator seek;
    private EventValidator event;
    private EncodingValidator encode;

    public PlaybackOverrideEncodingPriorityTests() throws OoyalaException{
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testOverrideEncodingPriorities(String testName , String url){

        logger.info("Test url for "+testName+" is : \n"+url);

        boolean result = true;

        try {

            driver.get(url);

            result = result && play.waitForPage();

            Thread.sleep(10000);

            injectScript();

            encode.setTestUrl(url);

            result = result && encode.validate("validate_default_encoding",20000);

            playAction.startAction();

            loadingSpinner();

            result = result && event.validate("adsPlayed_1",20000);

            result = result && seek.validate("seeked_1",20);

            loadingSpinner();

            result = result && event.validate("videoPlayed_1",20000);

            String param = "{\"freewheel-ads-manager\":{\"fw_video_asset_id\":\"Q5MXg2bzq0UAXXMjLIFWio_6U0Jcfk6v\",\"html5_ad_server\":\"http://g1.v.fwmrm.net\",\"html5_player_profile\":\"90750:ooyala_html5\",\"fw_mrm_network_id\":\"380912\",\"showInAdControlBar\":true},\"initialTime\":0,\"autoplay\":false,\"encodingPriority\":[\"hls\",\"webm\",\"mp4\",\"dash\"]}";

            encode.setTestUrl(encode.getNewUrl(param,browser));

            Thread.sleep(10000);

            injectScript();

            result = result && encode.validate("Override",60000);

            playAction.startAction();

            loadingSpinner();

            result = result && event.validate("adsPlayed_1",20000);

            result = result && seek.validate("seeked_1",60000);

            result = result && event.validate("videoPlayed_1",60000);

        } catch (Exception e){
            e.printStackTrace();
        }

        Assert.assertTrue(result,"OverrideEncoding Priority test failed");
    }
}