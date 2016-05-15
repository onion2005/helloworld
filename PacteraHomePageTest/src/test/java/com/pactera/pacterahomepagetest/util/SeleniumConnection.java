package com.pactera.pacterahomepagetest.util;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class SeleniumConnection {

	// Static member, which is used to hold the object of selenium driver
	protected static WebDriver driver = null;

	// Clean-up
	public static void closeSeleniumDriver() {
		if (driver != null) {
			driver.close();
			driver.quit();
			driver = null;
		}
	}

	// Need to check null
	public static WebDriver getCurrentDriver() {
		return driver;
	}

	// Helper function
	public void setImplicitlyWaitDuration(long num, TimeUnit unit) {
		if (driver != null) {
			driver.manage().timeouts().implicitlyWait(num, unit);
		}
	}
	
	// Create a brand-new firefox driver
		public static void createNewFireFoxDriver() {
			if (driver != null) {
				driver.close();
				driver.quit();
				driver = null;
			}
			driver = new FirefoxDriver();
		}

	// Create a brand-new firefox driver with customized binary path
	public static void createNewFireFoxDriverWithCustomizedPath(String webdriver_firefox_binary) {
		if (driver != null) {
			driver.close();
			driver.quit();
			driver = null;
		}
		System.setProperty("webdriver.firefox.bin", webdriver_firefox_binary);
		driver = new FirefoxDriver();
	}
	
	// Create a brand-new chrome driver with customized binary path
	public static void createNewChromeDriverWithCustomizedPath(String webdriver_chrome_driver, String webdriver_chrome_binary) {
		if (driver != null) {
			driver.close();
			driver.quit();
			driver = null;
		}
		System.setProperty("webdriver.chrome.driver", webdriver_chrome_driver);
		System.setProperty("webdriver.chrome.bin", webdriver_chrome_binary);
		driver = new ChromeDriver();
	}

	// Create a brand-new chrome driver
	public static void createNewChromeDriver(String webdriver_chrome_driver) {
		if (driver != null) {
			driver.close();
			driver.quit();
			driver = null;
		}
		System.setProperty("webdriver.chrome.driver", webdriver_chrome_driver);
		driver = new ChromeDriver();
	}

	// Create a brand-new Internet Explorer driver
	public static void createNewInternetExplorerDriver(
			String webdriver_ie_driver) {
		if (driver != null) {
			driver.close();
			driver.quit();
			driver = null;
		}
		System.setProperty("webdriver.ie.driver", webdriver_ie_driver);
		driver = new InternetExplorerDriver();
	}
}