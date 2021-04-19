# *Model Change - Contribution of ISLA - CDM for Securities Lending - SecurityFinancePayout*

_What is being released?_

This release contributes product modelling work completed by ISLA to extend the CDM to support Securities Lending products.  The release introduces a new Payout `SecurityFinancePayout`, five additional new data types contained within `SecurityFinancePayout`, and two new enumerations.  The existing `SecurityPayout` data type and the encapsulated data types and enumerations have been marked as `[deprecated]` in the model to reflect the move to using a Payout developed and approved by ISLA.

Changes to other data types and qualification functions have been made where the ISLA model has impacted existing CDM model content.

_Details_

New Data Types
- `SecurityFinancePayout`
- `DividendTerms`
- `CollateralProvisions`
- `SecurityFinanceLeg`
- `Duration`
- `EvergreenProvision`

New Enumerations
- `DurationTypeEnum`
- `CollateralTypeEnum`

Updates to Existing Data Types
- `Payout` - added new data attribute `securityFinancePayout`
- `EligibleCollateral` - data type renamed to `EligibleCollateralSchedule`, attribute `scheduleIdentifier` of type `Identifier` added to allow specification of an identified Collateral Schedule within a Legal Agreement.
- `PostingObligationsElection` - attribute `eligibleCollateral` updated to reflect data type name change above.
- `CollateralValuationPercentage` - new attribute `marginPercentage` and data condition `MarginPercentage` added to support alternate approach to defining margin requirements for Securities Finance transactions.

Updates to Qualification Functions
- Twenty nine Product Qualification functions for Credit Default Swaps, Equity Swaps and Interest Rate Swaps have been updated to extend the list of Payout types that must be absent to include `SecurityFinancePayout`.

Data Types marked as Deprecated
- `SecurityPayout`
- `InitialMargin`
- `InitialMarginCalculation`
- `SecurityValuation`
- `SecurityValuationModel`
- `BondValuationModel`
- `BondPriceAndYieldModel`
- `UnitContractValuationModel`

Enumerations marked as Deprecated
- `MarginTypeEnum`
- `RepoDurationEnum`

_Review directions_

In the CDM Portal, select the Textual Browser and search for any of the changes specified above. 
