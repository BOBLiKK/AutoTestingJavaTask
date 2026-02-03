package com.stv.factory.factorytests;

import com.stv.factory.factorypages.HomePage;
import com.stv.factory.factorypages.WebHostingPage;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class HomepageToProductPriceTest extends BaseFactoryTest {

    // TC3
    @Test
    public void tc3_crossCheckHomepagePriceWithWebHostingPage() {
        HomePage home = new HomePage(driver).open();
        Reporter.log("[TC3] Homepage opened successfully.", true);

        String homePrice = home.getWebHostingStartingPriceFromHome();
        Reporter.log("[TC3] Homepage Web Hosting starting price (Shared Hosting card): " + homePrice, true);

        WebHostingPage webHosting = home.goToWebHostingPage();
        Reporter.log("[TC3] Navigated to Web Hosting page.", true);

        Assert.assertTrue(webHosting.isLoaded(), "Web Hosting page did not load properly.");
        Reporter.log("[TC3] PASS: Web Hosting page loaded.", true);

        String productPrice = webHosting.getFirstPriceOnPage();
        Reporter.log("[TC3] Web Hosting page first detected price: " + productPrice, true);

        Assert.assertTrue(homePrice.startsWith("$"), "Homepage starting price is invalid: " + homePrice);
        Assert.assertTrue(productPrice.startsWith("$"), "Product page price is invalid: " + productPrice);

        String homeDigits = homePrice.replaceAll("[^0-9]", "");
        String productDigits = productPrice.replaceAll("[^0-9]", "");

        Assert.assertTrue(homeDigits.length() > 0 && productDigits.length() > 0,
                "Could not parse numeric parts of prices. home=" + homePrice + ", product=" + productPrice);

        if (!homePrice.equals(productPrice)) {
            boolean explained = webHosting.hasExplanationForPriceDifference();
            Assert.assertTrue(explained,
                    "Prices differ and no explanation found. home=" + homePrice + ", product=" + productPrice);

            Reporter.log("[TC3] PASS: Prices differ but explanation exists on product page.", true);
        } else {
            Reporter.log("[TC3] PASS: Prices match exactly.", true);
        }
    }
}
