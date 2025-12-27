package com.stv.factory.factorypages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    protected void waitVisible(WebElement el) {
        wait.until(ExpectedConditions.visibilityOf(el));
    }

    protected void waitClickable(WebElement el) {
        wait.until(ExpectedConditions.elementToBeClickable(el));
    }

    protected void click(WebElement el) {
        waitClickable(el);
        el.click();
    }

    protected void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    protected String safeText(WebElement el) {
        try {
            return el.getText() == null ? "" : el.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
