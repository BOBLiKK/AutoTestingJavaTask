package com.stv.bdd.steps;

import com.stv.bdd.hooks.Hooks;
import com.stv.factory.factorypages.HomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class HomepageSteps {

    private final WebDriver driver = Hooks.getDriver();

    private HomePage homePage;
    private int blocksWithPrice;

    @Given("userOpensInMotionHostingHomePage")
    public void userOpensInMotionHostingHomePage() {
        homePage = new HomePage(driver).open();
    }

    @When("userCountsPricingBlocksWithVisiblePrice")
    public void userCountsPricingBlocksWithVisiblePrice() {
        blocksWithPrice = homePage.countBlocksWithPrice();
    }

    @Then("atLeastPricingBlocksShouldHaveVisiblePrice {int}")
    public void atLeastPricingBlocksShouldHaveVisiblePrice(int minBlocks) {
        Assert.assertTrue(
                blocksWithPrice >= minBlocks,
                "Expected at least " + minBlocks + " pricing blocks with visible $ price. Found: " + blocksWithPrice
        );
    }
}
