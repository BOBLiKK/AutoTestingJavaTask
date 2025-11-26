package com.stv.design.designpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/* Created by tatti on 5/25/2024*/
public class MainPage extends Page {
    private final By IMMOTION_HOSTING_LOGO = By.xpath("//ul[@role='menubar']//*[contains(@alt,'Logo')]");
    private final By UP_MENU_SECTION = By.cssSelector("div.upper-menu-section");

    public boolean isMainLogoDisplayed() {
        WebElement registerLink = getDriver().findElement(IMMOTION_HOSTING_LOGO);
        return registerLink.isDisplayed();
    }

    public MainPage clickOnUpMenuSection() {
        WebElement trustButton = getDriver().findElement(UP_MENU_SECTION);
        trustButton.click();
        return this;
    }
}
