Feature: Smoke suite - Homepage pricing blocks

  Scenario: Verify pricing blocks with visible price are present on homepage
    Given user opens InMotion Hosting home page
    When user counts pricing blocks with visible price
    Then at least 4 pricing blocks should have a visible price
