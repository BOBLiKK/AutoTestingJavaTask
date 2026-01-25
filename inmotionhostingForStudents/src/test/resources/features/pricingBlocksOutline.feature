Feature: Pricing blocks validation - scenario outline

  Scenario Outline: verifyHomepageContainsMinimumNumberOfPricingBlocks
    Given userOpensInMotionHostingHomePage
    When userCountsPricingBlocksWithVisiblePrice
    Then atLeastPricingBlocksShouldHaveVisiblePrice <minBlocks>

    Examples:
      | minBlocks |
      | 4         |
      | 3         |
