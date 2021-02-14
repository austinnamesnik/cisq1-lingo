Feature: Showing the first letter of a word
  As a Player,
  I want to see the first letter of a random word,
  In order to start playing the next round.

  Scenario: Starting a new game
    When I click the start button
    Then a new game should start
    And a new letter should be shown.

  Scenario: Starting a new round
    Given the game has already started
    And the previous round was won
    When I see a new letter
    Then I can start guessing

  Scenario Outline: Start a new round
    Given I am playing a game
    And the previous round was won
    And the previous word had <previous length> letters
    When I start the next round
    Then the next word will have <next length> letters

  Examples:
    | previous length | next length |
    | 5               | 6           |
    | 6               | 7           |
    | 7               | 6           |

  #Failure path
  Scenario: Ending a game
    Given I am playing a game
    And I have lost the current round
    Then I cannot start a new round

  Scenario Outline: Guessing a word
    Given a game has been started
    And a letter is shown from the current <word>
    When <guess> a word
    Then I receive <feedback> from the game

  Examples:
    | word | guess | feedback |
    | kitty | kites | CORRECT, CORRECT, CORRECT, ABSENT, ABSENT |
    | kitty | kitten | INVALID, INVALID, INVALID, INVALID, INVALID, INVALID |
    | kitty | kitty | CORRECT, CORRECT, CORRECT, CORRECT, CORRECT |

  Scenario: Winning a round
    Given a word was guessed
    And all letters were guessed correctly
    Then the round will end
    And the player has won the round
    And the score will increase

  Scenario Outline: Increase the score
    Given the <current round> was won
    Then the <score> increases by 5*(5-<current round>)+5

  Examples:
    | current round | score |
    | 1             | 25    |
    | 2             | 45    |
    | 3             | 60    |
    | 4             | 70    |
    | 5             | 75    |

  #Failure path
  Scenario: Player loses
    Given the round is still active
    When I guess a word
    And the guessed word is incorrect
    And the current round is 5
    Then the game is lost
    And the player loses

  #Failure path
  Scenario: Guessing an already guessed word
    Given the round is still active
    When I guess a word
    And the guessed word has already been guessed that round
    Then I can't guess that word
