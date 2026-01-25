package com.stv.factory.factorypages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebHostingPowerPlanPage extends BasePage {

    //Task 7

    private static final String WEB_HOSTING_URL = "https://www.inmotionhosting.com/web-hosting";

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

    // Power plan price text inside card (e.g. $4.79/mo)
    @FindBy(xpath =
            "//h3[contains(normalize-space(.),'Power')]" +
                    "/ancestor::*[self::div or self::section][1]" +
                    "//*[contains(.,'$') and contains(.,'/mo')]"
    )
    private WebElement powerPrice;

    // Clickable CTA button inside Power card
    @FindBy(xpath =
            "//h3[contains(normalize-space(.),'Power')]" +
                    "/ancestor::*[self::div or self::section][1]" +
                    "//a[.//text()[contains(.,'Select') or contains(.,'Get Started') or contains(.,'Choose')]]"
    )
    private WebElement powerSelectBtn;

    // Optional cookie banner accept (safe)
    @FindBy(css = "button[id*='accept'], button[aria-label*='Accept']")
    private WebElement cookieAcceptBtn;


    public WebHostingPowerPlanPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(WEB_HOSTING_URL);
    }

    public String getPowerPriceText() {
        waitUntilVisible(powerPrice);
        return safeText(powerPrice).replaceAll("\\s+", " ");
    }


    public void clickPowerSelect() {
        acceptCookiesIfPresent();

        WebElement btn = powerSelectBtn;

        // Scroll to button (centered)
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", btn);

        try {
            // Normal Selenium click (preferred)
            waitUntilClickable(btn).click();
        } catch (Exception e) {
            // Fallback: JS click (handles overlays / non-interactable cases)
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", btn);
        }
    }


    private void acceptCookiesIfPresent() {
        try {
            if (cookieAcceptBtn.isDisplayed()) {
                cookieAcceptBtn.click();
            }
        } catch (Exception ignored) {
        }
    }


    private void waitUntilVisible(WebElement element) {
        new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.visibilityOf(element));
    }


    private WebElement waitUntilPresent(By locator) {
        return new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    private WebElement waitUntilClickable(WebElement el) {
        return new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(el));
    }
}
