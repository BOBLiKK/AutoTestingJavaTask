Feature: Price cross-check between homepage and product pages

  Scenario Outline: verifyHomepageStartingPriceMatchesProductPage
    Given userOpensInMotionHostingHomePage
    When userOpensProductPageFor "<hostingType>"
    Then productPageShouldBeLoadedFor "<hostingType>"
    And homepageAndProductPricesShouldBeValidAndParsable
    And homepageAndProductPriceShouldMatchOrHaveExplanation

    Examples:
      | hostingType  |
      | Web Hosting  |
