package com.pactera.pacterahomepagetest.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.pactera.pacterahomepagetest.util.SeleniumConnection;


public class Page {
	
	protected WebDriver driver = null;
	
	@FindBy(how = How.XPATH, using = "//input[@id='s']")
	protected WebElement searchBox;
	
	@FindBy(how = How.XPATH, using = "//li[@id='menu-item-4']/a")
	protected WebElement logoOfPactera;

	@FindBy(how = How.ID, using = "footer-menu")
	protected WebElement footerMenu;
	
	protected Page() {
		PageFactory.initElements(SeleniumConnection.getCurrentDriver(), this);
	}
	
	public void setWebDriver(WebDriver driver) {
		this.driver = driver;
	}
	
}
