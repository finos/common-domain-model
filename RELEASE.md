# *DSL Syntax - Remove deprecated DRR report syntax*

_What is being released?_

This release removes the deprecated DSL syntax to specify DRR reports using the `with fields` keywords.

An example of the previous syntax is as follows:

```
report CFTC Part43 in T+1
 	when ReportableEvent
 	using standard ISO_20022 with fields
 		ReportingTimestamp
 		ReportingCounterpartyID
 		TypeOfIdOfTheOtherCounterparty
 		IdOfTheOtherCounterparty
 		CountryOfTheOtherCounterparty
 		CorporateSectorOfTheReportingCounterparty
 		NatureOfTheReportingCounterparty
 		BrokerID
 		TypeOfIdOfTheBeneficiary
 		BeneficiaryID
 		TradingCapacity
 		CounterpartySide
```
    
Which has now been replaced by the following syntax:

```
report CFTC Part43 in T+1
    when ReportableEvent
  	using standard ISO_20022
  	with type CFTCPart43TransactionReport
```

Where the type is then defined seeperately in the same report definition.

Review Directions

There is no impact as the usages have already been replaced in DRR by the `with type` keywords.
