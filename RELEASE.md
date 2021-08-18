# *Digital Regulatory Reporting - DSL Upgrade for Repeatable Rules*

_What is being released?_

This release contains new DSL keyword `repeatable` to support the reporting of repeatable sets of data as required by most regulations. 

For example, in the CFTC Part 45 regulations, fields 33-35 require the reporting of a notional quantity schedule. For each quantity schedule step, the notional amount, effective date and end date must be reported.

In the code snippet below, the `repeatable` keyword specifies that the rule will be reported as a repeating set of data. The reporting rules specified in the brackets specify the fields to report for each repeating data set.
- `extract repeatable` < ExpressionWithMultipleCardinality> ( < ReportingRule1 >, < ReportingRule2 > ... < ReportingRuleN > )

In the example below, the `repeatable` keyword in reporting rule `NotionalAmountScheduleLeg1` specifies that the extracted list of quantity notional schedule steps should be reported as a repeating set of data. The rules specified within the brackets define the fields that should be reported for each repeating step.
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
