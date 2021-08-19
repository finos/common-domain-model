# *DSL Syntax - Enhancements for repeatable regulatory rules*

_What is being released?_

This release introduces the new DSL keyword `repeatable` to model reporting rules that repeat the same reporting logic on a specific set of data.  One use case is the CFTC Part 45 regulation. Fields 33-35 require to report the full details of a notional quantity schedule: for each quantity schedule step the notional amount, the effective date and the end date must be reported.

Usage of the `repeatable` keyword will replicate the following logical template: 
- `extract repeatable` < ExpressionWithMultipleCardinality> ( < ReportingRule1 >, < ReportingRule2 > ... < ReportingRuleN > )

The logic prescribes that a set of N reporting rules is applied sequentially but as a collective to each item of  the list of data records named ExpressionWithMultipleCardinality. 

In the example below, the `repeatable` keyword in the reporting rule `NotionalAmountScheduleLeg1` signifies that the bracketed set of reporting rules should be applied as a collective to each item of the extracted list of quantity notional schedule steps. The rule `NotionalAmountScheduleLeg1` will therefore result into a repeating set of reported fields with corresponding values.
```
reporting rule NotionalAmountScheduleLeg1 <"Notional Amount Schedule">
	[regulatoryReference CFTC Part45 appendix "1" item "33-35" field "Notional Amount Schedule"
		provision "Fields 33-35 are repeatable and shall be populated in the case of derivatives involving notional amount schedules"]
    TradeForEvent then
        InterestRateLeg1 then
            extract repeatable InterestRatePayout -> payoutQuantity -> quantitySchedule -> stepSchedule -> step then
            (
                NotionalAmountScheduleLeg1Amount,
                NotionalAmountScheduleLeg1EndDate,		
                NotionalAmountScheduleLeg1EffectiveDate
            )

reporting rule NotionalAmountScheduleLeg1Amount <"Notional amount in effect on associated effective date of leg 1">
	[regulatoryReference CFTC Part45 appendix "1" item "33" field "Notional amount in effect on associated effective date of leg 1"]
		CDENotionalAmountScheduleAmount
		as "33/35-$ 33 Notional amount leg 1"

reporting rule NotionalAmountScheduleLeg1EffectiveDate <"Effective date of the notional amount of leg 1">
	[regulatoryReference CFTC Part45 appendix "1" item "34" field "Effective date of the notional amount of leg 1"]
		CDENotionalAmountScheduleEffectiveDate
		as "33/35-$ 34 Effective date leg 1"

reporting rule NotionalAmountScheduleLeg1EndDate <"End date of the notional amount of leg 1">
	[regulatoryReference CFTC Part45 appendix "1" item "35" field "End date of the notional amount of leg 1"]
		CDENotionalAmountScheduleEndDate
		as "33/35-$ 35 End date leg 1"
```

_Review Directions_

In the CDM Portal, select the User Documentation tile and navigate to the Rosetta DSL > Rosetta Modelling Components > Reporting Component  section, or review the documentation section directly:

- [DSL Documentation - Repeatable Rules](https://docs.rosetta-technology.io/dsl/documentation.html#repeatable-rules)
