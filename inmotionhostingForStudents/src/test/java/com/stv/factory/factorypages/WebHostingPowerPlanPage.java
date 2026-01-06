package com.stv.factory.factorypages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebHostingPowerPlanPage extends BasePage {

    //Task 7

    // Power plan price text inside card (e.g. $4.79/mo)
    private final By powerPrice = By.xpath(
            "//h3[contains(normalize-space(.),'Power')]" +
                    "/ancestor::*[self::div or self::section][1]" +
                    "//*[contains(.,'$') and contains(.,'/mo')]"
    );

    // Clickable CTA button inside Power card
    private final By powerSelectBtn = By.xpath(
            "//h3[contains(normalize-space(.),'Power')]" +
                    "/ancestor::*[self::div or self::section][1]" +
                    "//a[.//text()[contains(.,'Select') or contains(.,'Get Started') or contains(.,'Choose')]]"
    );

    // Optional cookie banner accept (safe)
    private final By cookieAcceptBtn = By.cssSelector(
            "button[id*='accept'], button[aria-label*='Accept']"
    );

    public WebHostingPowerPlanPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get("https://www.inmotionhosting.com/web-hosting");
    }

    public String getPowerPriceText() {
        WebElement el = waitUntilVisible(powerPrice);
        return safeText(el).replaceAll("\\s+", " ");
    }

    public void clickPowerSelect() {
        acceptCookiesIfPresent();

        WebElement btn = waitUntilPresent(powerSelectBtn);

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
            WebElement btn = driver.findElement(cookieAcceptBtn);
            btn.click();
        } catch (Exception ignored) {
        }
    }

    private WebElement waitUntilVisible(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitUntilPresent(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    private WebElement waitUntilClickable(WebElement el) {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(el));
    }
}
