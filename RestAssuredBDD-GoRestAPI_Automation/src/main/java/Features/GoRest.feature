#Author: Saransh Saxena
#email: saransh.saxena18@gmail.com

@tag
Feature: Go Rest API Automation

	@RunThis
  Scenario Outline: Test Create User API
    Given "Add User" API payload should be valid with "<name>", "<gender>", "<email>", "<status>" 
    When API is hit with "POST" https request
    Then "POST" request response should be valid
    And Verify user existence after "Post" API request
    #Deletion of the created test data is handled in the After hook
    Examples:
    |name							 |gender|email											 |status|
		|Tenali Ramakrishna|male	|tenali.ramakrishna@15ce1237654.com|active|
	
  Scenario Outline: Test Get User API by ID
    Given "Get User" with a valid userId 8067244
    When API is hit with "Get" https request
    Then "Get" request response should be valid with following details "<name>", "<gender>", "<email>", "<status>" 
    Examples:
    |name							 |gender|email						 |status|
		|Tenali Ramakrishna|male	|tenali.ram@15c.com|active|
    
    Scenario: Test Edit User details API
    Given "Edit User" with a valid userId 
    When API is hit with "Put" https request
    Then "PUT" request response should be valid 
    And Verify user existence after "Put" API request
    
    Scenario: Test Delete User API
    Given "Delete User" with a valid userId
    When API is hit with "Delete" https request
    Then "Delete" request response should be valid 
    And Verify user existence after "Delete" API request 