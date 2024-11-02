Feature: Add Place API Validation

  @add_place @regression
  Scenario Outline: Successfully adding a new place using the addPlaceAPI
    Given the payload for AddPlaceAPI is prepared with "<name>" "<language>" and "<address>"
    When the user sends a request to "addPlaceAPI" with "POST" http method
    Then the API call is successful with status code 200
    And the "status" in the response is "OK"
    And the "scope" in the response is "APP"
    And verify that the place_id maps to "<name>" using "getPlaceAPI"

    Examples:
      | name       | language | address  |
    #|Syed Yakhub |    Urdu    | Near Noorani Masjid |
      | Syed Usman | English  | Batawadi |
    #|Syed Siddiq |   Kannada  |    Banglore         |

  @delete_place @regression
  Scenario: Successfully deleting the added place using the deletePlaceAPI
    Given building the delete place payload using place_id
    When the user sends a request to "deletePlaceAPI" with "POST" http method
    Then the API call is successful with status code 200
    And the "status" in the response is "OK"

