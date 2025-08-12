#Author: Saransh Saxena
#email: saransh.saxena18@gmail.com

@tag
Feature: Go Rest API Automation

  
  Scenario Outline: Test Create User API
    Given "Add User" API payload should be valid with "<name>", "<gender>", "<email>", "<status>" 
    When API is hit with "POST" https request
    Then "POST" request response should be valid
    And created user should exist
    Examples:
    |name							 |gender|email											 |status|
		|Tenali Ramakrishna|male	|tenali.ramakrishna@15ce123.com|active|

	@tag1
  Scenario Outline: Test Get User API by ID
    Given "Get User" with a valid userId 8065105
    When API is hit with "Get" https request
    Then "Get" request response should be valid with following details "<name>", "<gender>", "<email>", "<status>" 
    Examples:
    |name							 |gender|email						 |status|
		|Tenali Ramakrishna|male	|tenali.ram@15c.com|active|
    