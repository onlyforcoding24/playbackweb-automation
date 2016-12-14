package com.ooyala.playback.report;

import java.util.HashMap;
import java.util.Map;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class ExtentManager {

	private static Map<String, ExtentTest> extentTestMap;
	private static ExtentReports extentReports;

	static {
		extentTestMap = new HashMap<String, ExtentTest>();
		if (extentReports == null) {
			extentReports = new ExtentReports("./ExtentReport.html", true);

			extentReports.addSystemInfo("Host Name", "Jenkins-Dallas-Slave")
					.addSystemInfo("Environment", "QA");
			extentReports.addSystemInfo("browser", System.getProperty("browser"));

		}
	}

	public synchronized static ExtentReports getReporter() {
		return extentReports;
	}

	public synchronized static ExtentReports sharedInstance() {
		return extentReports;
	}

	public static synchronized void endTest(ExtentTest test) {
		extentReports.endTest(test);

	}

	public static synchronized ExtentTest startTest(String testName) {

		ExtentTest test = extentTestMap.get(testName);
		if (test == null) {
			test = extentReports.startTest(testName);
			extentTestMap.put(testName, test);
		}

		return test;
	}

	public static synchronized void flush() {
		extentReports.flush();
	}
}
