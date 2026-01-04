Feature: Pricing blocks validation - scenario outline

  Scenario Outline: Verify homepage contains minimum number of pricing blocks with a visible price
    Given user opens InMotion Hosting home page
    When user counts pricing blocks with visible price
    Then at least <minBlocks> pricing blocks should have a visible price

    Examples:
      | minBlocks |
      | 4         |
      | 3         |
