@tag
Feature: Feature to test cart

  @tag1
  Scenario: Add four different products to the cart
    Given open browser
    Given I want to add four random items to my cart
    When I view my cart
    Then I find total four items in my cart
    When I search for lowest price item
    And I am able to remove the lowest price item from my cart
    Then I am able to verify three items in my cart
