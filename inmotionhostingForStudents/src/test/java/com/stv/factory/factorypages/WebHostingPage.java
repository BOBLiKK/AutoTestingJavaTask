package com.stv.factory.factorypages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebHostingPage extends BasePage {

    private static final Pattern PRICE_PATTERN = Pattern.compile("\\$\\s*\\d+(?:\\.\\d+)?");

    @FindBy(css = "h1, h1 span")
    private WebElement h1;

    @FindBy(css = "body")
    private WebElement pageBody;

    private final By explanationBy = By.xpath(
            "//*[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'intro') " +
                    "or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'term') " +
                    "or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'renew') " +
                    "or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'renews') " +
                    "or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'first') " +
                    "or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'month')]"
    );

    public WebHostingPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            waitVisible(h1);
            return safeText(h1).length() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getFirstPriceOnPage() {
        String text = safeText(pageBody);
        Matcher m = PRICE_PATTERN.matcher(text);
        if (m.find()) {
            String p = m.group().replaceAll("\\s+", "");
            System.out.println("[WebHostingPage] First price on page: " + p);
            return p;
        }
        throw new AssertionError("Could not find any $ price on Web Hosting page.");
    }

    public boolean hasExplanationForPriceDifference() {
        try {
            boolean present = !driver.findElements(explanationBy).isEmpty();
            System.out.println("[WebHostingPage] Explanation present=" + present);
            return present;
        } catch (Exception e) {
            return false;
        }
    }
}
