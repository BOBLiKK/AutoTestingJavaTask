package com.stv.factory.factorypages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomePage extends BasePage {

    private static final String BASE_URL = "https://www.inmotionhosting.com/";
    private static final String WEB_HOSTING_PATH = "/web-hosting";
    private static final Pattern PRICE_PATTERN = Pattern.compile("\\$\\s*\\d+(?:\\.\\d+)?");

    @FindBy(css = "button#onetrust-accept-btn-handler")
    private WebElement acceptCookiesBtn;

    @FindBy(xpath = "//p[normalize-space()='Compare Our Hosting Plans']")
    private WebElement comparePlansHeader;

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
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(90));
        driver.get(BASE_URL);
        driver.manage().window().maximize();

        System.out.println("[HomePage] Opened URL: " + driver.getCurrentUrl());
        acceptCookiesIfPresent();
        return this;
    }

    public void acceptCookiesIfPresent() {
        try {
            if (acceptCookiesBtn != null && acceptCookiesBtn.isDisplayed()) {
                click(acceptCookiesBtn);
                System.out.println("[HomePage] Cookies accepted.");
            }
        } catch (Exception ignored) {}
    }

    public void scrollToComparePlansSection() {
        acceptCookiesIfPresent();
        waitVisible(comparePlansHeader);
        scrollIntoView(comparePlansHeader);
        System.out.println("[HomePage] Scrolled to 'Compare Our Hosting Plans' section.");
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    private String extractFirstPrice(String text) {
        if (text == null) return null;
        Matcher m = PRICE_PATTERN.matcher(text);
        return m.find() ? m.group().replaceAll("\\s+", "") : null;
    }


    private List<WebElement> findCardsNearCompareSection() {
        scrollToComparePlansSection();

        List<WebElement> candidates = new ArrayList<>();

        try {
            WebElement nextSection = comparePlansHeader.findElement(By.xpath("./ancestor::*[self::section or self::div][1]/following::section[1]"));
            candidates.addAll(nextSection.findElements(By.cssSelector("[class*='plan'], [class*='pricing'], [class*='card']")));
        } catch (Exception ignored) {}

        if (candidates.isEmpty()) {
            candidates = driver.findElements(By.cssSelector("[class*='plan'], [class*='pricing'], [class*='card']"));
        }

        return candidates;
    }

    public Map<String, String> getStartingPricesFromComparePlansCards() {
        List<WebElement> cards = findCardsNearCompareSection();
        System.out.println("[HomePage] Cards candidates found: " + cards.size());

        Map<String, String> result = new LinkedHashMap<>();

        for (WebElement card : cards) {
            String text = safeText(card);
            String lower = normalize(text);

            if (!(lower.contains("starting at") && lower.contains("$"))) {
                continue;
            }

            String price = extractFirstPrice(text);
            if (price == null) continue;

            if (lower.contains("shared hosting")) {
                result.put("Shared Hosting", price);
            } else if (lower.contains("hosting for wordpress") || lower.contains("wordpress")) {
                result.put("WordPress Hosting", price);
            } else if (lower.contains("vps hosting") || lower.contains("vps")) {
                result.put("VPS Hosting", price);
            } else if (lower.contains("dedicated hosting") || lower.contains("dedicated")) {
                result.put("Dedicated Hosting", price);
            }
        }

        System.out.println("[HomePage] Extracted prices from Compare Plans:");
        result.forEach((k, v) -> System.out.println("  - " + k + " => " + v));

        return result;
    }

    public int countPricingCardsWithPriceInCompareSection() {
        Map<String, String> m = getStartingPricesFromComparePlansCards();
        int count = m.size();
        System.out.println("[HomePage] Pricing cards with valid starting price (Compare Plans): " + count);
        return count;
    }

    // TC3
    public String getWebHostingStartingPriceFromHome() {
        try {
            Map<String, String> prices = getStartingPricesFromComparePlansCards();
            if (prices.containsKey("Shared Hosting")) {
                String p = prices.get("Shared Hosting");
                System.out.println("[HomePage] Web Hosting starting price (from Compare Plans / Shared): " + p);
                return p;
            }
        } catch (Exception ignored) {}

        // fallback
        for (WebElement block : possiblePricingBlocks) {
            String t = safeText(block).toLowerCase();
            if ((t.contains("web hosting") || t.contains("shared")) && t.contains("$")) {
                String p = extractFirstPrice(safeText(block));
                if (p != null) {
                    System.out.println("[HomePage] Web Hosting starting price (fallback blocks): " + p);
                    return p;
                }
            }
        }

        throw new AssertionError("Could not extract any starting price from homepage.");
    }

    public WebHostingPage goToWebHostingPage() {
        acceptCookiesIfPresent();

        if (webHostingLinks != null && !webHostingLinks.isEmpty()) {
            for (WebElement link : webHostingLinks) {
                try {
                    if (link.isDisplayed()) {
                        System.out.println("[HomePage] Clicking Web Hosting link: " + safeText(link));
                        click(link);
                        return new WebHostingPage(driver);
                    }
                } catch (Exception ignored) {}
            }
        }

        String direct = BASE_URL + WEB_HOSTING_PATH;
        System.out.println("[HomePage] Navigating directly to: " + direct);
        driver.navigate().to(direct);
        return new WebHostingPage(driver);
    }
}
