# Product Model - Inclusion of time in the event instruction and economic terms
_Background_

The current data model for trades only includes date references within the economicTerms section for the contract's start and end dates. Additionally, the EventInstruction structure does not currently capture precise intra-day event times. This approach lacks granularity for intra-day transactions, where the exact time of initiation and termination is critical for accurate trade representation and downstream processing. The inclusion of time would allow precise specification of the contract's start and end times and should support a time zone and related time components to ensure accurate interpretation across regions and other referenced times.

_What is being released?_

Contribution of new types to define the time as a direct or relative object, based on the TimeZone type and potentially having some offsets and adjustments
The DirectOrRelativeTime applied to the effectiveDate and terminationDate as part of the economicTerms in a new elements: effectiveTime and terminationTime
The time relative to the event date in the EventInstruction

_Review Directions_

Changes can be reviewed in PR: [#4437](https://github.com/finos/common-domain-model/pull/4437)

# _Asset Model - Adding Redemption Attribute to Debt Type_
_Background_

There are several values in the the `DebtClassEnum` relating to the redemption of the debt which could be more granular and composable. There are 4 attributes representing unique combinations for `IssuerConvertible`, `HolderConvertible`, `IssuerExchangeable`, `HolderExchangeable`. However, these could be represented using separate enums and conditions within DebtType. This would also remove the additional Convertible attribute.

_What is being released?_

Created a DebtRedemption type 
- Added `redemptionType` attribute with type `RedemptionTypeEnum` 
- Added `putCall` attribute with type `PutCallEnum` 
- Added `party` attribute with type `RedemptionPartyEnum` 

Created 2 new enums 
- `RedemptionTypeEnum` with values Convertible, Exchangeable, ContingentConvertible, Sinkable, Extraordinary
- `RedemptionPartyEnum` with values Holder and Issuer

_Review Directions_

The changes can be reviewed in PR: [#4447](https://github.com/finos/common-domain-model/pull/4447)
