# Event Model - Contribution of ISLA - CDM for Securities Lending*

_What is being released?_


This release contributes modelling components proposed by ISLA to extend the CDM in term of Securities Lending products. The release introduces a new Payout SecurityFinancePayout, five additional new data types contained within SecurityFinancePayout, and two new enumerations. The existing SecurityPayout data type and the encapsulated data types and related enumerations have been marked as [deprecated] in the model to reflect the move to using the new SecurityFinancePayout.

Changes to other data types and qualification functions have been made where appropriate to accomodate for the new payout.

_Details_

New Data Types

- SecurityFinancePayout
- DividendTerms

New Enumerations

    DurationTypeEnum
    CollateralTypeEnum

Updates to Existing Data Types

    Payout - added new data attribute securityFinancePayout
    EligibleCollateral - data type renamed to EligibleCollateralSchedule, attribute scheduleIdentifier of type Identifier added to allow specification of an identified Collateral Schedule within a Legal Agreement.
    PostingObligationsElection - attribute eligibleCollateral updated to reflect data type name change above.
    CollateralValuationPercentage - new attribute marginPercentage and data condition MarginPercentage added to support alternate approach to defining margin requirements for Securities Finance transactions.

Updates to Qualification Functions

    Twenty nine Product Qualification functions for Credit Default Swaps, Equity Swaps and Interest Rate Swaps have been updated or extended to factor the use of the new SecurityFinancePayout.

Data Types marked as Deprecated

    SecurityPayout
    InitialMargin
    InitialMarginCalculation
    SecurityValuation
    SecurityValuationModel
    BondValuationModel
    BondPriceAndYieldModel
    UnitContractValuationModel

Enumerations marked as Deprecated

    MarginTypeEnum
    RepoDurationEnum

_Review directions_

In the CDM Portal, select the Textual Browser and search for any of the changes specified above.
