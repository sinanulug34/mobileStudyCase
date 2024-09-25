Feature: Nesine Case Study Android Mobile App

  @login
  Scenario: Success Login
    Given Start Nesine android application
    * Trigger click event for element "loginButton"
    * Find elements by key and type username "usernameTextBox"
    * Find elements by key and type password "passwordTextBox"
    * Trigger click event for element "askLoginRadioButton"
    * Trigger click event for element "loginToAppButton"
    * Trigger click event for element "memberButton"
    * Swipe to end of the page
    * Sleep for 2 seconds
    * Trigger click event for element "logoutButton"
    * Check visibility of element "loginButton" on the page

  @invalidlogin
  Scenario: Assert invalid password info message
    * Start Nesine android application
    * Trigger click event for element "loginButton"
    * Find element by key "usernameTextBox" and type the value "35641777370"
    * Find element by key "passwordTextBox" and type the value "mat.1199"
    * Trigger click event for element "askLoginRadioButton"
    * Trigger click event for element "loginToAppButton"
    * Validate that text "Üye numaranız, TC kimlik numaranız veya şifreniz hatalıdır! Lütfen bilgilerinizi kontrol ediniz." is visible to the user

    @promotion
    Scenario: Define Promotion code
      * Start Nesine android application
      * Trigger click event for element "loginButton"
      * Find element by key "usernameTextBox" and type the value "35641777370"
      * Find element by key "passwordTextBox" and type the value "mat.1177"
      * Trigger click event for element "askLoginRadioButton"
      * Trigger click event for element "loginToAppButton"
      * Trigger click event for element "memberButton"
      * Trigger click event for element "promotions"
      * Trigger click event for element "inputPromotionCodeTextBox"
      * Find element by key "inputPromotionCodeTextBox" and type the promotion code value


  @customernumber
  Scenario: Compare member number
    * Start Nesine android application
    * Trigger click event for element "loginButton"
    * Find element by key "usernameTextBox" and type the value "35641777370"
    * Find element by key "passwordTextBox" and type the value "mat.1177"
    * Trigger click event for element "askLoginRadioButton"
    * Trigger click event for element "loginToAppButton"
    * Verify equality of text values for elements "memberNumber" and "memberNumberOnProfilePage"






