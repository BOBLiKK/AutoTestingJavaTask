package com.stv.factory.factorypages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
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

    @FindBy(xpath = "//p[contains(@class,'h2') and normalize-space()='Compare Our Hosting Plans']")
    private WebElement comparePlansHeader;

    @FindBy(css = "a[href*='/web-hosting'], a[href='/web-hosting']")
    private List<WebElement> webHostingLinks;

    private final By cardCandidatesBy = By.cssSelector("[class*='plan'], [class*='pricing'], [class*='card']");

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

    private String norm(String s) {
        return s == null ? "" : s.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    private String extractFirstPrice(String text) {
        if (text == null) return null;
        Matcher m = PRICE_PATTERN.matcher(text);
        return m.find() ? m.group().replaceAll("\\s+", "") : null;
    }


    public Map<String, String> getStartingPricesFromComparePlansCards() {
        scrollToComparePlansSection();

        List<WebElement> candidates = new ArrayList<>();

        try {
            WebElement container = comparePlansHeader.findElement(
                    By.xpath("./ancestor::*[self::section or self::div][1]")
            );

            candidates.addAll(container.findElements(cardCandidatesBy));

            if (candidates.isEmpty()) {
                WebElement nextSection = container.findElement(By.xpath("following::section[1]"));
                candidates.addAll(nextSection.findElements(cardCandidatesBy));
            }
        } catch (Exception ignored) {}

        if (candidates.isEmpty()) {
            candidates = driver.findElements(cardCandidatesBy);
        }

        System.out.println("[HomePage] Card candidates found: " + candidates.size());

        Map<String, String> result = new LinkedHashMap<>();

        for (WebElement card : candidates) {
            String text = safeText(card);
            String lower = norm(text);

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


    public String getWebHostingStartingPriceFromHome() {
        Map<String, String> prices = getStartingPricesFromComparePlansCards();
        if (prices.containsKey("Shared Hosting")) {
            String p = prices.get("Shared Hosting");
            System.out.println("[HomePage] Web Hosting starting price (Shared Hosting card): " + p);
            return p;
        }

        throw new AssertionError("Could not extract Web Hosting (Shared Hosting) starting price from Compare Plans section.");
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
