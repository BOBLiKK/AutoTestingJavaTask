package com.stv.bdd.steps;

import com.stv.bdd.hooks.Hooks;
import com.stv.factory.factorypages.HomePage;
import com.stv.factory.factorypages.WebHostingPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Map;

public class HomepageSteps {

    private final WebDriver driver = Hooks.getDriver();

    private HomePage homePage;

    // TC1 data
    private Map<String, String> compareSectionPrices;
    private int cardsWithPrice;

    // TC3 data
    private String homePrice;
    private String productPrice;
    private WebHostingPage webHostingPage;

    @Given("userOpensInMotionHostingHomePage")
    public void userOpensInMotionHostingHomePage() {
        System.out.println("\n=== GIVEN: userOpensInMotionHostingHomePage ===");
        homePage = new HomePage(driver).open();
    }


    @When("userCountsPricingBlocksWithVisiblePrice")
    public void userCountsPricingBlocksWithVisiblePrice() {
        System.out.println("=== WHEN: userCountsPricingBlocksWithVisiblePrice ===");
        cardsWithPrice = homePage.countPricingCardsWithPriceInCompareSection();
        System.out.println("[BDD] Counted cardsWithPrice=" + cardsWithPrice);
    }

    @When("userReadsStartingPricesFromComparePlansCards")
    public void userReadsStartingPricesFromComparePlansCards() {
        System.out.println("=== WHEN: userReadsStartingPricesFromComparePlansCards ===");
        compareSectionPrices = homePage.getStartingPricesFromComparePlansCards();
        cardsWithPrice = compareSectionPrices.size();
        System.out.println("[BDD] Collected compareSectionPrices size=" + cardsWithPrice);
    }

    @Then("atLeastPricingBlocksShouldHaveVisiblePrice {int}")
    public void atLeastPricingBlocksShouldHaveVisiblePrice(int minBlocks) {
        System.out.println("=== THEN: atLeastPricingBlocksShouldHaveVisiblePrice " + minBlocks + " ===");
        Assert.assertTrue(
                cardsWithPrice >= minBlocks,
                "Expected at least " + minBlocks + " pricing cards with visible starting price in Compare Plans section. Found: " + cardsWithPrice
        );
        System.out.println("[BDD] PASS: cardsWithPrice >= " + minBlocks);
    }

    @Then("comparePlansSectionShouldContainStartingPricesForAllMainHostingTypes")
    public void comparePlansSectionShouldContainStartingPricesForAllMainHostingTypes() {
        System.out.println("=== THEN: comparePlansSectionShouldContainStartingPricesForAllMainHostingTypes ===");

        if (compareSectionPrices == null) {
            compareSectionPrices = homePage.getStartingPricesFromComparePlansCards();
        }

        String[] expected = {"Shared Hosting", "WordPress Hosting", "VPS Hosting", "Dedicated Hosting"};

        for (String key : expected) {
            Assert.assertTrue(compareSectionPrices.containsKey(key),
                    "Missing card price for: " + key + ". Found keys: " + compareSectionPrices.keySet());

            String price = compareSectionPrices.get(key);
            Assert.assertTrue(price != null && price.startsWith("$"),
                    "Invalid price for " + key + ": " + price);

            System.out.println("[BDD] PASS: " + key + " has starting price " + price);
        }

        System.out.println("[BDD] PASS: All 4 hosting types have visible starting prices.");
    }

    // ===== TC3 =====

    @When("userOpensProductPageFor {string}")
    public void userOpensProductPageFor(String productName) {
        System.out.println("=== WHEN: userOpensProductPageFor '" + productName + "' ===");

        if (!"Web Hosting".equalsIgnoreCase(productName)) {
            throw new IllegalArgumentException("Only 'Web Hosting' is supported. Got: " + productName);
        }

        homePrice = homePage.getWebHostingStartingPriceFromHome();
        webHostingPage = homePage.goToWebHostingPage();
        productPrice = webHostingPage.getFirstPriceOnPage();

        System.out.println("[BDD] Captured prices: home=" + homePrice + ", product=" + productPrice);
    }

    @Then("productPageShouldBeLoadedFor {string}")
    public void productPageShouldBeLoadedFor(String productName) {
        System.out.println("=== THEN: productPageShouldBeLoadedFor '" + productName + "' ===");
        Assert.assertTrue(webHostingPage.isLoaded(), "Product page did not load properly for: " + productName);
        System.out.println("[BDD] PASS: Product page loaded.");
    }

    @Then("homepageAndProductPricesShouldBeValidAndParsable")
    public void homepageAndProductPricesShouldBeValidAndParsable() {
        System.out.println("=== THEN: homepageAndProductPricesShouldBeValidAndParsable ===");

        Assert.assertTrue(homePrice != null && homePrice.startsWith("$"), "Homepage price invalid: " + homePrice);
        Assert.assertTrue(productPrice != null && productPrice.startsWith("$"), "Product price invalid: " + productPrice);

        String homeDigits = homePrice.replaceAll("[^0-9]", "");
        String productDigits = productPrice.replaceAll("[^0-9]", "");

        Assert.assertTrue(homeDigits.length() > 0, "Could not parse digits from homepage price: " + homePrice);
        Assert.assertTrue(productDigits.length() > 0, "Could not parse digits from product price: " + productPrice);

        System.out.println("[BDD] PASS: Prices valid & parsable. homeDigits=" + homeDigits + ", productDigits=" + productDigits);
    }

    @Then("homepageAndProductPriceShouldMatchOrHaveExplanation")
    public void homepageAndProductPriceShouldMatchOrHaveExplanation() {
        System.out.println("=== THEN: homepageAndProductPriceShouldMatchOrHaveExplanation ===");

        if (homePrice.equals(productPrice)) {
            System.out.println("[BDD] PASS: Prices match exactly. home=" + homePrice + ", product=" + productPrice);
            return;
        }

        boolean explained = webHostingPage.hasExplanationForPriceDifference();
        Assert.assertTrue(explained,
                "Prices differ and no explanation found. home=" + homePrice + ", product=" + productPrice);

        System.out.println("[BDD] PASS: Prices differ but explanation exists on product page. home=" + homePrice + ", product=" + productPrice);
    }
}
