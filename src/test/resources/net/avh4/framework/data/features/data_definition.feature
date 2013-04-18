Feature: Data definition

Scenario: list of data objects
  Given a data object definition
  When I instantiate a list of data objects
  Then the data is persisted