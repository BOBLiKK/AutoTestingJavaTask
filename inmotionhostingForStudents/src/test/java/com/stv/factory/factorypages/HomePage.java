package com.stv.factory.factorypages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomePage extends BasePage {

    private static final String BASE_URL = "https://www.inmotionhosting.com/";
    private static final String WEB_HOSTING_PATH = "/web-hosting";
    private static final Pattern PRICE_PATTERN = Pattern.compile("\\$\\s*\\d+(?:\\.\\d+)?");

    @FindBy(css = "button#onetrust-accept-btn-handler")
    private WebElement acceptCookiesBtn;

    @FindBys({
            @FindBy(css = "section"),
            @FindBy(css = "[class*='plan'], [class*='pricing'], [class*='card']")
    })
    private List<WebElement> possiblePricingBlocks;

    @FindBy(css = "a[href*='/web-hosting'], a[href='/web-hosting']")
    private List<WebElement> webHostingLinks;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        driver.get(BASE_URL);
        driver.manage().window().maximize();
        acceptCookiesIfPresent();
        return this;
    }

    public void acceptCookiesIfPresent() {
        try {
            if (acceptCookiesBtn != null && acceptCookiesBtn.isDisplayed()) {
                click(acceptCookiesBtn);
            }
        } catch (Exception ignored) {
        }
    }

    public int countBlocksWithPrice() {
        int count = 0;
        for (WebElement block : possiblePricingBlocks) {
            String t = safeText(block);
            if (t.contains("$") && extractFirstPrice(t) != null) {
                count++;
            }
        }
        return count;
    }

    public String getWebHostingStartingPriceFromHome() {
        for (WebElement block : possiblePricingBlocks) {
            String t = safeText(block).toLowerCase();
            if ((t.contains("web hosting") || t.contains("shared")) && t.contains("$")) {
                String p = extractFirstPrice(safeText(block));
                if (p != null) return p;
            }
        }

        for (WebElement block : possiblePricingBlocks) {
            String p = extractFirstPrice(safeText(block));
            if (p != null) return p;
        }

        throw new AssertionError("Could not extract any starting price from homepage blocks.");
    }

    public WebHostingPage goToWebHostingPage() {
        acceptCookiesIfPresent();

        if (webHostingLinks != null && !webHostingLinks.isEmpty()) {
            for (WebElement link : webHostingLinks) {
                try {
                    if (link.isDisplayed()) {
                        click(link);
                        return new WebHostingPage(driver);
                    }
                } catch (Exception ignored) {}
            }
        }

        driver.navigate().to(BASE_URL + WEB_HOSTING_PATH);
        return new WebHostingPage(driver);
    }

    private String extractFirstPrice(String text) {
        if (text == null) return null;
        Matcher m = PRICE_PATTERN.matcher(text);
        return m.find() ? m.group().replaceAll("\\s+", "") : null;
    }
}
