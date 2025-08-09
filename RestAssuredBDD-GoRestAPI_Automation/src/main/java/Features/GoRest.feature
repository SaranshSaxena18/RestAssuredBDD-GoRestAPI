#Author: saransh.saxena18@gmail.com

@tag
Feature: Go Rest API Automation

  @tag1
  Scenario Outline: Test Create User API
    Given "Add User" API payload should be valid with "<name>", "<gender>", "<email>", "<status>" 
    When API is hit with "POST" https request
    Then "status" "ok" response should be recieved
    
    Examples:
    |name							 |gender|email											 |status|
		|Tenali Ramakrishna|male	|tenali.ramakrishna@15ce1.com|active|
  #@tag2
  #Scenario Outline: Title of your scenario outline
    #Given I want to write a step with <name>
    #When I check for the <value> in step
    #Then I verify the <status> in step
#
    #Examples: 
      #| name  | value | status  |
      #| name1 |     5 | success |
      #| name2 |     7 | Fail    |
