# *Legal Agreement Model - Enhancements for Eligible Collateral Concentration Limits*

_Background_

This release follows recent work with ISDA members in the form of workshops to extend the eligible collateral representation in preperation for mapping and adoption.

_What is being released?_

The representation of concentration limits for Eligible Collateral applied to Equity Products has been enhanced with the below:
1.	New attribute added to data type ` ConcentrationLimitCriteria ` named `averageTradingVolume` to enable the user to specify an average trading volume on an exchange in relation to Equity products
2.	New data type added ` AverageTradingVolume` for representing the average trading volume of an Equity product upon an exchange or set of exchanges
3.	Attributes added to data type `AverageTradingVolume` 

  •	`period` for representing the period of the equities average trading volume on the exchange/s
  
  •	`methodology` for indicating the type of equity trading volume’
  
4.	Attribute `methodology` has an associated enumeration modelled named `AverageTradingVolumeMethodologyEnum` which allows the user to specify a single or consolidated average trading volume across exchange/s
5.	An additional enumeration has been added to the `ConcentrationLimitTypeEnum` list named `MarketCapitalisation` to indicate a limit of the issue calculated as a percentage of the market capitalisation of the asset on the market. 
6.	Existing description for `IssueOutstandingAmount` under the `ConcentrationLimitTypeEnum` list had been updated as per member suggestions.
7.	Conditions have been added beneath the data type `EligibleCollateralCriteria` as follows:

  •	`ConcentrationLimitTypeIssueOSAmountDebtOnly` a condition that concentration limit type 'IssueOutstandingAmount' is restricted to be used only if the asset type is described as 'Security' and 'Debt'
  
  •	`ConcentrationLimitTypeMarketCaplitalisationEquityOnly` a condition that concentration limit type `MarketCapitalisation` is restricted to be used only if the asset type is described as `Security` and `Equity`
  
  •	`AverageTradingVolumeEquityOnly` a condition that concentration limit `AverageTradingVolume` is restricted to be used only if the asset type is described as `Security` and `Equity`
  
8.	A condition has been added beneath the data type `ConcentrationLimit` as follows 

  •	`PercentageConcentrationLimit` to enforce that percentage limit exists if the concentration limit type is `MarketCapitalisation`


_Review Directions_
 
In the CDM Portal, select the Textual Browser and inspect the changes mentioned above and across the following data types and enumerations: 

1.	`ConcentrationLimit`
2.	`ConcentrationLimitCriteria`
3.	`AverageTradingVolume`
4.	`ConcentrationLimitTypeEnum`
5.	`AverageTradingVolumeMethodologyEnum`

