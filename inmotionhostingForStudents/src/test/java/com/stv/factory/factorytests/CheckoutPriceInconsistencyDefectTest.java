package com.stv.factory.factorytests;

import com.stv.factory.factorypages.CheckoutPage;
import com.stv.factory.factorypages.WebHostingPowerPlanPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutPriceInconsistencyDefectTest extends BaseFactoryTest {

    /**
     * Covers Task 2 defect: IMH-CHECKOUT-001
     * "Checkout Displays $4.99/mo for Power Plan Although This Price Does Not Exist on the Product Page"
     */
    @Test
    public void tc_cover_IMH_CHECKOUT_001_priceMismatchBetweenProductAndCheckout() {

        WebHostingPowerPlanPage product = new WebHostingPowerPlanPage(driver);
        product.open();

        String powerPrice = product.getPowerPriceText();

        // Product page expected: $4.79/mo (as per Task 2)
        Assert.assertTrue(
                powerPrice.contains("$4.79") && powerPrice.contains("/mo"),
                "Power plan price on product page is not '$4.79/mo'. Actual: " + powerPrice
        );

        product.clickPowerSelect();

        CheckoutPage checkout = new CheckoutPage(driver);

        // Defect: checkout introduces $4.99/mo for 2y/3y terms
        // While defect exists, this check should FAIL if we expect no such price.
        Assert.assertFalse(
                checkout.billingDropdownContains("$4.99/mo"),
                "Defect detected: Checkout billing term dropdown contains '$4.99/mo'."
        );
    }
}
