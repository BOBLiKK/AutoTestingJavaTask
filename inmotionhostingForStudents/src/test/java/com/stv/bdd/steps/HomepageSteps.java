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

    @Given("user opens InMotion Hosting home page")
    public void user_opens_inmotion_hosting_home_page() {
        homePage = new HomePage(driver).open();
    }

    @When("user counts pricing blocks with visible price")
    public void user_counts_pricing_blocks_with_visible_price() {
        blocksWithPrice = homePage.countBlocksWithPrice();
    }

    @Then("at least {int} pricing blocks should have a visible price")
    public void at_least_pricing_blocks_should_have_a_visible_price(int minBlocks) {
        Assert.assertTrue(
                blocksWithPrice >= minBlocks,
                "Expected at least " + minBlocks + " pricing blocks with visible $ price. Found: " + blocksWithPrice
        );
    }
}
