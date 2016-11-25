package com.ooyala.playback;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;

import com.ooyala.facile.listners.IMethodListener;
import com.ooyala.facile.test.FacileTest;
import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.httpserver.SimpleHttpServer;
import com.ooyala.playback.page.PlayBackPage;
import com.ooyala.playback.report.ExtentManager;
import com.ooyala.playback.url.Testdata;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.ooyala.qe.common.util.PropertyReader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@Listeners(IMethodListener.class)
public abstract class PlaybackWebTest extends FacileTest {

	public static Logger logger = Logger.getLogger(PlaybackWebTest.class);
	protected String browser;
	protected ChromeDriverService service;
	protected PropertyReader propertyReader;
	protected PlayBackFactory pageFactory;
	// protected static NodeList nodeList;
	protected ExtentReports extentReport;
	protected ExtentTest extentTest;
	protected Testdata testData;
	protected String[] jsUrl;

	public PlaybackWebTest() throws OoyalaException {

		try {
			propertyReader = PropertyReader.getInstance("config.properties");
		} catch (Exception e) {
			throw new OoyalaException("could not read properties file");
		}

		extentReport = ExtentManager.getReporter();
	}

	@BeforeMethod(alwaysRun = true)
	public void handleTestMethodName(Method method, Object[] testData) {
		String testCaseName = getTestCaseName(method, testData);
		extentTest = extentReport.startTest(testCaseName);

		try {
			Field[] fs = this.getClass().getDeclaredFields();
			fs[0].setAccessible(true);
			for (Field property : fs) {
				if (property.getType().getSuperclass()
						.isAssignableFrom(PlayBackPage.class)) {
					property.setAccessible(true);
					property.set(this,
							pageFactory.getObject(property.getType()));
					Method[] allMethods = property.get(this).getClass()
							.getMethods();
					for (Method function : allMethods) {
						if (function.getName()
								.equalsIgnoreCase("setExtentTest"))
							function.invoke(property.get(this), extentTest);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getJSFile(String jsFile) throws Exception {
		logger.info("************Getting the JS file*************");
		String[] jsFiles;
		if (jsFile.contains(",")) {
			jsFiles = jsFile.split(",");
		} else {
			jsFiles = new String[1];
			jsFiles[0] = jsFile;
		}
		// String jsHost = readPropertyOrEnv("jshostIpAddress","10.11.66.55");
		if (jsFiles != null && jsFiles.length > 0) {
			jsUrl = new String[jsFiles.length];
			for (int i = 0; i < jsFiles.length; i++) {
				InetAddress inetAdd = InetAddress.getLocalHost();
				jsUrl[i] = "http://" + inetAdd.getHostAddress()
						+ ":8000/js?fileName=" + jsFiles[i];
			}
		}

	}

	public String getTestCaseName(Method method, Object[] testData) {
		String testCase = "";
		if (testData != null && testData.length > 0) {
			for (Object testParameter : testData) {
				if (testParameter instanceof String) {
					String testCaseParams = (String) testParameter;
					testCase = testCaseParams;
					break;
				}
			}
			testCase = String.format("%s(%s)", method.getName(), testCase);
		} else
			testCase = method.getName();

		return testCase;
	}

	@BeforeClass(alwaysRun = true)
	@Parameters({ "testData", "jsFile" })
	public void setUp(String xmlFile, String jsFile) throws Exception {
		logger.info("************Inside setup*************");
		logger.info("browser is " + browser);

		browser = System.getProperty("browser");
		if (browser == null || browser.equals(""))
			browser = "firefox";

		driver = getDriver(browser);

		logger.info("Driver initialized successfully");
		//driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		// driver.manage().timeouts().implicitlyWait(240, TimeUnit.MINUTES);
		pageFactory = PlayBackFactory.getInstance(driver);
		parseXmlFileData(xmlFile);
		getJSFile(jsFile);
		SimpleHttpServer.startServer();

	}

	@AfterMethod(alwaysRun = true)
	protected void afterMethod(ITestResult result) {

		takeScreenshot(result.getTestName());
		if (result.getStatus() == ITestResult.FAILURE) {
			extentTest.log(
					LogStatus.INFO,
					"Snapshot is "
							+ extentTest.addScreenCapture("images/"
									+ result.getTestName()));
			extentTest.log(LogStatus.FAIL, result.getThrowable());
		} else if (result.getStatus() == ITestResult.SKIP) {
			extentTest.log(LogStatus.SKIP, result.getTestName()
					+ " Test skipped " + result.getThrowable());
		} else {
			extentTest.log(LogStatus.PASS, result.getTestName()
					+ " Test passed");
		}
		extentReport.endTest(extentTest);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		extentReport.flush();
		logger.info("************Inside tearDown*************");
		if (driver != null) {
			driver.quit();
			driver = null;
		} else {
			logger.info("Driver is already null");
		}
		logger.info("Assigning the neopagefactory instance to null");
		PlayBackFactory.destroyInstance();
		SimpleHttpServer.stopServer();
	}

	public void waitForSecond(int sec) {
		try {
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void parseXmlFileData(String xmlFile) {

		try {
			File file = new File("src/test/resources/" + xmlFile);
			JAXBContext jaxbContext = JAXBContext.newInstance(Testdata.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			testData = (Testdata) jaxbUnmarshaller.unmarshal(file);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
	}

	public void injectScript() throws Exception {
		if (jsUrl != null && jsUrl.length > 0) {
			for (String url : jsUrl) {
				try {
					logger.info("JS - " + url);
					injectScript(url);
				} catch (Exception e) {
					// e.printStackTrace();
					logger.error(e.getMessage());
					logger.info("Retrying...");
					injectScript(url);
				}
			}
		}
	}

	private void injectScript(String scriptURL) throws Exception {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object object = js.executeScript("function injectScript(url) {\n"
				+ "   var script = document.createElement ('script');\n"
				+ "   script.src = url;\n"
				+ "   var head = document.getElementsByTagName( 'head')[0];\n"
				+ "   head.appendChild(script);\n" + "}\n" + "\n"
				+ "var scriptURL = arguments[0];\n"
				+ "injectScript(scriptURL);", scriptURL);

		if (scriptURL.contains("common"))
			object = js.executeScript("subscribeToCommonEvents();");
		else
			object = js.executeScript("subscribeToEvents();");
		extentTest.log(LogStatus.PASS, "Javascript injection is successful");
	}

	public long loadingSpinner() {
		long startTime = 0L;
		long endTime = 0L;
		int time = 0;
		long flag = 0L;

		while (true) {

			startTime = System.currentTimeMillis();
			// Giving hardcoded end time as 2 minutes i.e it will check loading
			// spinner upto 2 min otherwise will break
			if (time <= 120) {
				try {
					driver.findElement(By.className("oo-spinner"))
							.isDisplayed();
					Thread.sleep(1000);
					time++;
				} catch (Exception e) {
					endTime = System.currentTimeMillis();
					break;
				}
			} else {
				logger.info("Loading spinner is not vanishing i.e it occured more that 2 minutes");
				flag = 1;
				break;
			}

		}
		return flag;

	}

	public String getPlatform() {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String platformName = cap.getPlatform().toString();
		return platformName;
	}

	public String getBrowser() {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String browser = cap.getBrowserName().toString();
		return browser;
	}

	public static String readPropertyOrEnv(String key, String defaultValue) {
		String v = System.getProperty(key);
		if (v == null)
			v = System.getenv(key);
		if (v == null)
			v = defaultValue;
		return v;
	}

	public String takeScreenshot(String fileName) {
		File destDir = new File("images/");
		if (!destDir.exists())
			destDir.mkdir();

		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File("images/" + fileName));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Not able to take the screenshot");
		}
		return "images/" + fileName;
	}

	@DataProvider(name = "testUrls")
	public Object[][] getTestData() {

		List<String> urls = UrlGenerator.parseXmlDataProvider(getClass()
				.getSimpleName(), testData);
		String testName = getClass().getSimpleName();
		Object[][] output = new Object[urls.size()][2];
		for (int i = 0; i < urls.size(); i++) {
			output[i][0] = testName;
			output[i][1] = urls.get(i);
		}

		return output;

	}

	public Map<String,String> parseURL(String urlString) throws Exception{
		if(urlString!=null && !urlString.isEmpty()){
			URL url = new URL(urlString);
			Map<String, String> query_pairs = new HashMap<String, String>();
		    String query = url.getQuery();
		    String[] pairs = query.split("&");
		    for (String pair : pairs) {
		        int index = pair.indexOf("=");
		        query_pairs.put(URLDecoder.decode(pair.substring(0, index), "UTF-8"), URLDecoder.decode(pair.substring(index + 1), "UTF-8"));
		    }
		    return query_pairs;
		}
		
		return null;
	}
}
