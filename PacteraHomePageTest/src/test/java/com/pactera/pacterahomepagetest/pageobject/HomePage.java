package com.pactera.pacterahomepagetest.pageobject;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pactera.pacterahomepagetest.util.PacteraHomePageTestException;

public class HomePage extends Page {
	
	private static Logger log = Logger.getLogger(HomePage.class.getName());

	// The id of the body will be different for different pages
	@FindBy(how = How.ID, using = "home")
	protected WebElement bodyOfHomePage;

	public HomePage launch(String url) throws PacteraHomePageTestException {
		// Launch the browser first
		try {
			log.info("Get the URL " + url);
			driver.get(url);
			// Call javascript function to check status
			log.info("Wait for page to be ready");
			ExpectedCondition<Boolean> homePageLaunchCondition = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					// Make sure that KB3021952 is installed on Windows
					return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
				}
			};
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(homePageLaunchCondition);
		} catch (Exception e) {
			boolean throwException = true;
			// For demo purpose, should be removed during normal testing
			if (e instanceof TimeoutException) {
				if (footerMenu.isDisplayed()) {
					log.info("The current page is loading slowly due to javascript issues ... bypass it and go to next step");
					throwException = false;
				}
			}
			if (throwException) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new PacteraHomePageTestException(e.getMessage());
			}
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// Check some elements
		log.info("Check <body>");
		if (!bodyOfHomePage.isDisplayed()) {
			throw new PacteraHomePageTestException("Incorrect homepage <body>");			
		}
		log.info("Check logo");
		if (!logoOfPactera.isDisplayed()) {
			throw new PacteraHomePageTestException("Missing company logo");		
		}
		log.info("Check footer");
		if (!footerMenu.isDisplayed()) {
			throw new PacteraHomePageTestException("Missing homepage footer menu");
		}
		return this;
	}
	
	public SearchPage readyForSearch() throws PacteraHomePageTestException {
		log.info("Check search box");
		if (!searchBox.isDisplayed()) {
			throw new PacteraHomePageTestException("Missing search box");
		}
		return new SearchPage();
	}

}