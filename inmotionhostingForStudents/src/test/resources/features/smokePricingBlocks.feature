Feature: Smoke suite - Homepage pricing blocks

  Scenario: verifyPricingBlocksWithVisiblePriceOnHomepage
    Given userOpensInMotionHostingHomePage
    When userCountsPricingBlocksWithVisiblePrice
    Then atLeastPricingBlocksShouldHaveVisiblePrice 4
