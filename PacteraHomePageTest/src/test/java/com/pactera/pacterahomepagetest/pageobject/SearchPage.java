package com.pactera.pacterahomepagetest.pageobject;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pactera.pacterahomepagetest.util.PacteraHomePageTestException;

public class SearchPage extends Page {
	
	private static Logger log = Logger.getLogger(SearchPage.class.getName());
	
	// "Search > Page"
	@FindBy(how = How.ID, using = "breadcrumb")
	protected WebElement searchDiv;
	
	// The entire search div
	@FindBy(how = How.XPATH, using = "//*[@id='content']")
	protected WebElement searchContestDiv;	
	
	// Search Results str for test automation (under the div)
	@FindBy(how = How.XPATH, using = "//*[@id='content']/p[1]/em")
	protected WebElement emphasizedText;
	
	// Test automation str (under the div)
	@FindBy(how = How.XPATH, using = "//*[@id='content']/p[1]/em/strong")
	protected WebElement keyWordOnPage;
	
	// One of the search result
	@FindBy(how = How.XPATH, using = "//*[@id='content']/p[2]")
	protected WebElement firstContent;

	public SearchPage performSearch(String keyword) throws PacteraHomePageTestException {
		try {
			log.info("Input keyword(" + keyword + ") into search box");
			searchBox.sendKeys(keyword);
			log.info("Start searching ");
			searchBox.submit();
			log.info("Wait for search page to be ready");
			ExpectedCondition<Boolean> searchPageCondition = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					// Make sure that KB3021952 is installed on Windows
					return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
				}
			};
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(searchPageCondition);
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
		log.info("Compare keywords on the page: " + keyword + " and content information");
		if (!((searchContestDiv.findElements(By.className("small-margin")).size() == 7) && searchDiv.getText().startsWith("Home > Search") && emphasizedText.getText().startsWith("Search Results for " + keyword) && keyWordOnPage.getText().startsWith(keyword) && firstContent.getText().startsWith("Pactera"))) {
			throw new PacteraHomePageTestException("Incorrect search result");
		}
		return this;
	}
	
}