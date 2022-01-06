# *Event Model – Margin Call Process, Exposure, Collateral Positions and Balance Representation*

_What is being released_

New data types and attributed are proposed to represent margin calls for collateral related processes. These common data elements are intended for the various asset classes where collateral margin call practices exist such as (OTC Derivates, OTC Cleared, Repo, Securities Lending and ETD). The changes include the following:

1.	`MarginCallBase` – Specifies the generic requirements for all message types
2.	`Exposure` -  Represents positions and aggregate exposure/ valuations required in margin call calculations 
3.	`MarginCallExposure` -  Represents exposure for margin call reference and a breakdown of exposure values if required
4.	`CollateralBalance` – Represents an aggregated collateral balance in a base currency with is settlement status and any haircut factors
5.	`CollateralPortfolio` – Allows for specification of portfolio identity, individual components of a collateral positions and a collateral balance 
6.	`CollateralPosition` – Extends `position` for listing individual collateral plus their settlement status and any specific treatment/ haircuts.
7.	`MarginCallIssuance`  - Represents common attributes for margin call issuance communications 
8.	`MarginCallInstructionType` - Represents values to specify the call notification type and specific action types
9.	`MarginCallResponseAction` - Represents specific actions in response messages such as what collateral is being delivered and whether it is a deliver or return of collateral 
10.	`MarginCallResponse` - Represents common attributes for margin call response communications. Includes agreement of amount, actions for delivery and if the response is (agreed in full, partially or disputed)


_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the data types mentioned above, their attributes and descriptions. They  are all in the event-common-type name space.

In the name space event-common-type, review the following conditions:

1.  condition `RegIMRoleIMOnly` Found in relation to data type `MarginCallBase`  - Specifies a condition to ensure that RegIMRole (Pledgor or Secured Party) is only applicable if the Reg margin type is defined as RegIM (Regulatory Initial Margin)
2. condition `OverallExposureSumOfSimmAndScheduleIM` Found in relation to data type `MarginCallExposure` - Represents a condition to ensure that if Simm IM exposure and Schedule/Grid IM exposure are specified the sum value must equate to overall exposure amount
3.	condition `CollateralPositionStatusSettledOrIntransitOnly` Found in relation to data type `CollateralPosition` - Represents a condition to ensure that if a status is defined for a collateral position you must only indicate 'Settled Amount' or 'In Transit' amount from the available enumerations.
4.	condition `CallTypeExpectedVisibility` Found in relation to data type `MarginCallInstructionType` -Represents a condition to ensure that a visibility indicator is specifies then the call type must be an expected call.

In the name space event-common-enum review each of the following enumerations, their values and descriptions:

1.	`CallTypeEnum` - Represents the enumeration values that indicate the intended status of message type, such as expected call, notification of a call or a margin call.
2.	`MarginCallActionEnum` - Represents the enumeration values to identify the collateral action instruction.
3.	`CollateralStatusEnum` - Represents the enumeration list to identify the settlement status of the collateral.
4.	`MarginCallResponseTypeEnum` - Represents the enumeration values to define the response type to a margin call.
5.	`RegMarginTypeEnum` - Represents the enumeration values to specify the margin type in relation to bilateral or regulatory obligation.
6.	`RegIMRoleEnum` - Represents the enumeration values to specify the role of the party in relation to a regulatory initial margin call.
7.	`HaircutIndicatorEnum` - Represents the enumeration indicators to specify if an asset or group of assets valuation is based on any valuation treatment haircut.

Within the name space (event-position-type), the following changes have been made: 

1.	Data type `Position` has had the following attributes removed 
  o	`product`
  o	`quantity`
  o	`postionStatus`
These have been replaced with a new attribute
  o	`positionComponent` which uses `PriceQuantity` for representation of positions with many price quantities 
2.	The attribute `tradeReference` has been renamed to reference `TradeState` instead of  `Trade`
3.	Minor spacing issues have been rectified in the descriptions for the following data types:
  o	`Position` 
  o	`PortoflioState`


# *Termination Visualisations*

_What is being released?_

- STORY-458: A CDM user can visualise how Full and Partial Termination Events work when multiple Trade Lots exist
