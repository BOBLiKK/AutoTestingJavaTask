package com.stv.factory.factorytests;

import com.stv.factory.factorypages.HomePage;
import com.stv.factory.factorypages.WebHostingPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomepageToProductPriceTest extends BaseFactoryTest {

    //TC3
    @Test
    public void tc3_crossCheckHomepagePriceWithWebHostingPage() {
        HomePage home = new HomePage(driver).open();

        String homePrice = home.getWebHostingStartingPriceFromHome();
        WebHostingPage webHosting = home.goToWebHostingPage();

        Assert.assertTrue(webHosting.isLoaded(), "Web Hosting page did not load properly.");
        String productPrice = webHosting.getFirstPriceOnPage();

        Assert.assertTrue(productPrice.startsWith("$"),
                "No valid price detected on Web Hosting page.");


        Assert.assertTrue(homePrice.startsWith("$"), "Homepage starting price is invalid: " + homePrice);
        Assert.assertTrue(productPrice.startsWith("$"), "Product page price is invalid: " + productPrice);

        String homeDollars = homePrice.replaceAll("[^0-9]", "");
        String productDollars = productPrice.replaceAll("[^0-9]", "");
        Assert.assertTrue(homeDollars.length() > 0 && productDollars.length() > 0,
                "Could not parse numeric parts of prices. home=" + homePrice + ", product=" + productPrice);
    }
}
