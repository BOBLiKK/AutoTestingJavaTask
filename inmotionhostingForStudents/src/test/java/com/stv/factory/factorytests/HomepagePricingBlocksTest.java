package com.stv.factory.factorytests;

import com.stv.factory.factorypages.HomePage;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class HomepagePricingBlocksTest extends BaseFactoryTest {

    // TC1
    @Test
    public void tc1_verifyPresenceOfPricingBlocksOnHomepage() {
        HomePage home = new HomePage(driver).open();

        int cardsWithPrice = home.countPricingCardsWithPriceInCompareSection();
        Reporter.log("[TC1] Compare Plans cards with visible starting price found: " + cardsWithPrice, true);

        Assert.assertTrue(cardsWithPrice >= 4,
                "Expected at least 4 Compare Plans cards with visible 'Starting at $' price. Found: " + cardsWithPrice);

        Reporter.log("[TC1] PASS: found >= 4 Compare Plans cards with price.", true);
    }
}
