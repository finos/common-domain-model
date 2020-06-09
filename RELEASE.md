# *CDM Model: Legal Document Modelling*

_What is being released_

Resolve complex `ISDA Create` mapping issues:

- `CalculationDateLocation` - resolve incorrect association of mapped items.
- `Custodian` - build mapper to associate Custodian Name and Account details for Stock vs Cash.
- `Threshold` / `MinimumTransferAmount` - update existing mapper to correctly deal with zero amount.
- `CustodyArrangements` - resolve incorrect association of mapped items.

_Review Direction_

In the Ingestion Panel, try samples in folder `isda-create`.


# *CDM Model: Eligible Collateral Modelling*

_What is being released_

Eligible collateral model changes to support concentration limits and wrong way risk.

_Review Direction_

In the Textual Browser, review types `CollateralTreatment` and `ConcentrationLimitCriteria`.
