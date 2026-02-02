package com.stv.factory.factorytests;

import com.stv.factory.factorypages.CheckoutPage;
import com.stv.factory.factorypages.DomainPage;
import com.stv.factory.factorypages.SharedHostingPage;
import com.stv.factory.factorypages.WebHostingPowerPlanPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CheckoutPriceInconsistencyDefectTest extends BaseFactoryTest {

    @Test
    public void tc_IMH_CHECKOUT_001_bugFoundTrue() {

        // 1) Navigate to https://www.inmotionhosting.com/web-hosting
        WebHostingPowerPlanPage webHosting = new WebHostingPowerPlanPage(driver);
        webHosting.open();

        // 2) Power shows $4.79/mo
        String powerPrice = webHosting.getPowerPriceText();
        System.out.println("[WEB-HOSTING] Power price = " + powerPrice);

        Assert.assertTrue(
                powerPrice.contains("$4.79") && powerPrice.contains("/mo"),
                "Expected '$4.79/mo' on web-hosting page. Actual: " + powerPrice
        );

        // 3) click Select -> goes to /shared-hosting
        webHosting.clickPowerSelect();
        System.out.println("[NAV] After first Select URL = " + driver.getCurrentUrl());

        // 4) on shared-hosting: scroll and click Select for Power
        SharedHostingPage sharedHosting = new SharedHostingPage(driver);
        System.out.println("[SHARED] url=" + driver.getCurrentUrl());
        sharedHosting.selectPowerPlan();
        System.out.println("[NAV] After shared-hosting Power Select URL = " + driver.getCurrentUrl());
        System.out.println("[SHARED] after click url=" + driver.getCurrentUrl());

        // 5) domain: Choose My Domain Later
        DomainPage domain = new DomainPage(driver);
        domain.chooseMyDomainLater();
        System.out.println("[NAV] After Choose my domain later URL = " + driver.getCurrentUrl());
        System.out.println("[NAV] After Choose My Domain Later URL = " + driver.getCurrentUrl());

        // 6) checkout: dropdown has 2y/3y @ $4.99/mo (bug)
        CheckoutPage checkout = new CheckoutPage(driver);

        List<String> options = checkout.getBillingOptionsText();
        System.out.println("[CHECKOUT] Billing options: " + String.join(" | ", options));

        boolean bugFound = checkout.bugPriceFoundFor2yOr3y();
        System.out.println("[DEFECT IMH-CHECKOUT-001] bugFound = " + bugFound);

        // PASS if bug is found
        Assert.assertTrue(
                bugFound,
                "Bug evidence not found. Expected '2 Years/3 Years' with '$4.99'. Options: " + String.join(" | ", options)
        );
    }
}
