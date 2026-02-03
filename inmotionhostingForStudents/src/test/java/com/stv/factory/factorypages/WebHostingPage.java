package com.stv.factory.factorypages;

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

    public WebHostingPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            waitVisible(h1);
            String title = safeText(h1);
            boolean ok = title.length() > 0;
            System.out.println("[WebHostingPage] isLoaded=" + ok + ", h1='" + title + "'");
            return ok;
        } catch (Exception e) {
            System.out.println("[WebHostingPage] isLoaded=false, reason=" + e.getClass().getSimpleName());
            return false;
        }
    }

    public String getFirstPriceOnPage() {
        String text = safeText(pageBody);
        Matcher m = PRICE_PATTERN.matcher(text);
        if (m.find()) {
            String price = m.group().replaceAll("\\s+", "");
            System.out.println("[WebHostingPage] First price on page: " + price);
            return price;
        }
        throw new AssertionError("Could not find any $ price on Web Hosting page.");
    }

    public boolean hasExplanationForPriceDifference() {
        String body = safeText(pageBody).toLowerCase();
        boolean has =
                body.contains("per month") ||
                        body.contains("/mo") ||
                        body.contains("billed") ||
                        body.contains("renew") ||
                        body.contains("term") ||
                        body.contains("intro") ||
                        body.contains("promotional") ||
                        body.contains("regular price");
        System.out.println("[WebHostingPage] Explanation present=" + has);
        return has;
    }
}
