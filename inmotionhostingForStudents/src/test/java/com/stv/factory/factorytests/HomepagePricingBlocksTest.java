package com.stv.factory.factorytests;

import com.stv.factory.factorypages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomepagePricingBlocksTest extends BaseFactoryTest {

    //TC1
    @Test
    public void tc1_verifyPresenceOfPricingBlocksOnHomepage() {
        HomePage home = new HomePage(driver).open();
        int blocksWithPrice = home.countBlocksWithPrice();
        Assert.assertTrue(blocksWithPrice >= 4,
                "Expected at least 4 pricing blocks with visible $ price. Found: " + blocksWithPrice);
    }
}
