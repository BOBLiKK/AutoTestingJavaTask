package com.stv.factory.factorypages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    protected void waitVisible(WebElement el) {
        wait.until(ExpectedConditions.visibilityOf(el));
    }

    protected WebElement waitVisible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected void waitClickable(WebElement el) {
        wait.until(ExpectedConditions.elementToBeClickable(el));
    }

    protected void click(WebElement el) {
        waitClickable(el);
        el.click();
    }

    protected void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
    }

    protected String safeText(WebElement el) {
        try {
            String t = el.getText();
            return t == null ? "" : t.trim();
        } catch (Exception e) {
            return "";
        }
    }

    protected String safeAttr(WebElement el, String attr) {
        try {
            String v = el.getAttribute(attr);
            return v == null ? "" : v.trim();
        } catch (Exception e) {
            return "";
        }
    }
}
