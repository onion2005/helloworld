package com.pactera.pacterahomepagetest;

import java.io.File;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import com.pactera.pacterahomepagetest.pageobject.HomePage;
import com.pactera.pacterahomepagetest.pageobject.SearchPage;
import com.pactera.pacterahomepagetest.util.PacteraHomePageTestException;
import com.pactera.pacterahomepagetest.util.SeleniumConnection;


public class TestTwoFeatures {
	
	private static Logger log = Logger.getLogger(TestTwoFeatures.class.getName());
	
	private HomePage homePage = null;
	
	private SearchPage searchPage = null;

	@Parameters({"browser_type", "webdriver_ie_driver", "webdriver_chrome_driver", "webdriver_ff_binary", "webdriver_chrome_binary"})
	@BeforeTest
	public void beforeTest(String browser_type, String webdriver_ie_driver, String webdriver_chrome_driver, @Optional("webdriver_ff_binary") String webdriver_ff_binary, @Optional("webdriver_chrome_binary") String webdriver_chrome_binary) throws PacteraHomePageTestException {
		log.info("\r\nTest begins ... \r\n");
		log.info("Check browser type ... ");
		if (browser_type.equals("explorer")) {
			log.info("Browser type : Internet Explorer");
			if (null == webdriver_ie_driver) {
				log.error("Internet Explorer Driver path error: " + webdriver_ie_driver);
				throw new PacteraHomePageTestException("Invalid webdriver_ie_driver path: " + webdriver_ie_driver);
			}
			if (webdriver_ie_driver.isEmpty()) {
				log.error("Internet Explorer Driver path error: " + webdriver_ie_driver);
				throw new PacteraHomePageTestException("Invalid webdriver_ie_driver path: " + webdriver_ie_driver);
			}
			File ieDriverFile = new File(webdriver_ie_driver);
			if (ieDriverFile.exists()) {
				Reporter.log("Create Internet Explorer Driver\r\n", true);
				log.info("Create Internet Explorer Driver");
				SeleniumConnection.createNewInternetExplorerDriver(webdriver_ie_driver);
			} else {
				log.error(webdriver_ie_driver + " does not exist!");
				throw new PacteraHomePageTestException(webdriver_ie_driver + " does not exist!");
			}
		} else if (browser_type.equals("chrome")) {
			log.info("Browser type : Chrome");
			// Driver location must be valid
			if (null == webdriver_chrome_driver) {
				log.error("Chrome Driver path error: " + webdriver_chrome_driver);
				throw new PacteraHomePageTestException("Invalid webdriver_chrome_driver path: " + webdriver_chrome_driver);
			}
			if (webdriver_chrome_driver.isEmpty()) {
				log.error("Chrome Driver path error: " + webdriver_chrome_driver);
				throw new PacteraHomePageTestException("Invalid webdriver_chrome_driver path: " + webdriver_chrome_driver);
			}			
			File chromeDriverFile = new File(webdriver_chrome_driver);
			if (chromeDriverFile.exists()) {
				boolean createChromeDriverWithoutCustomizedPath = true;
				// Binary location is nice to have if you choose customized installation
				if (null != webdriver_chrome_binary) {
					if (!webdriver_chrome_binary.isEmpty()) {
						File chromeBinaryFile = new File(webdriver_chrome_binary);
						if (chromeBinaryFile.exists()) {
							Reporter.log("Create Chrome Driver\r\n", true);
							log.info("Create Chrome Driver");
							SeleniumConnection.createNewChromeDriverWithCustomizedPath(webdriver_chrome_driver, webdriver_chrome_binary);
							createChromeDriverWithoutCustomizedPath = false;
						}
					}
				}
				if (createChromeDriverWithoutCustomizedPath) {
					Reporter.log("Create Chrome Driver\r\n", true);
					log.info("Create Chrome Driver");
					SeleniumConnection.createNewChromeDriver(webdriver_chrome_driver);					
				}
			} else {
				log.error(webdriver_chrome_driver + " does not exist!");
				throw new PacteraHomePageTestException(webdriver_chrome_driver + " does not exist!");
			}
		} else if (browser_type.equals("firefox")) {
			log.info("Browser type : FireFox ");
			boolean createFireFoxDriverWithoutCustomizedPath = true;
			Reporter.log(webdriver_ff_binary, true);
			if (null != webdriver_ff_binary) {
				if (!webdriver_ff_binary.isEmpty()) {
					File ffDriverFile = new File(webdriver_ff_binary);
					if (ffDriverFile.exists()) {
						Reporter.log("Create FireFox Driver with custmized path\r\n", true);
						log.info("Create FireFox Driver");
						SeleniumConnection.createNewFireFoxDriverWithCustomizedPath(webdriver_ff_binary);;						
						createFireFoxDriverWithoutCustomizedPath = false;
					}
				}
			}
			if (createFireFoxDriverWithoutCustomizedPath) {
				Reporter.log("Create FireFox Driver\r\n", true);
				log.info("Create FireFox Driver");
				SeleniumConnection.createNewFireFoxDriver();
			}
		} else {
			log.error("Invalid browser type: " + browser_type);
			Reporter.log("Invalid browser type: " + browser_type + "\r\n", true);
			throw new PacteraHomePageTestException("Invalid browser type: " + browser_type);
		}
		if (null == SeleniumConnection.getCurrentDriver()) {
			Reporter.log("Unable to create selenium driver!\r\n");
			throw new PacteraHomePageTestException("Unable to create selenium driver!");
		}
	}

	@Parameters({ "url" })
	@Test
	public void visitHomePage(String url) {
		Reporter.log("Check URL first. URL: " + url + "\r\n", true);
		if (null == url) {
			Assert.fail("Invalid URL input: " + url);
		}
		if (url.isEmpty()) {
			Assert.fail("Invalid URL input: " + url);
		}
		homePage = new HomePage();
		homePage.setWebDriver(SeleniumConnection.getCurrentDriver());
		Reporter.log("Access home page\r\n", true);
		try {
			Reporter.log("Launch browser and get the URL " + url + "\r\n", true);
			homePage.launch(url);
			Reporter.log("Check the status of the search box\r\n", true);
			searchPage = homePage.readyForSearch();
		} catch (Exception e) {
			Reporter.log("Error: " + e.getMessage() + "\r\n", true);
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "keyword" })
	@Test(dependsOnMethods = { "visitHomePage" })
	public void searchWithKeyword(String keyword) {
		Reporter.log("Check keyword first. keyword: " + keyword + "\r\n", true);
		if (null == keyword) {
			Assert.fail("Invalid keyword input: " + keyword);
		}
		if (keyword.isEmpty()) {
			Assert.fail("Invalid keyword input: " + keyword);
		}
		Reporter.log("Access search page and perform searching\r\n", true);
		searchPage.setWebDriver(SeleniumConnection.getCurrentDriver());
		try {
			searchPage.performSearch(keyword);
		} catch (Exception e) {
			Reporter.log("Error: " + e.getMessage() + "\r\n", true);
			Assert.fail(e.getMessage());
		}
	}

	@AfterTest
	public void afterTest() {
		// Cleaning up resources
		SeleniumConnection.closeSeleniumDriver();
	}
}