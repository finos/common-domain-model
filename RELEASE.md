# *Product Model - Performance Payout - Valuation dates*
_Background_

This release completes the representation of the valuation dates for a performance payout with the specification of the initial dates, that was previously missing. 

_What is being released?_

The attribute `valuationDatesInitial` of type `PerformanceValuationDates` has been added to specify the initial valuation dates of the underlyer. The corresponding synonym mapping has also been introduced.

_Review Directions_

In the CDM Portal, select the textual representation of the model and inspect the representation of the valuation dates of a `PerformancePayout`.

# *Product Model - FpML Mappings - Bond Reference for Interest Rate Payout*

_What is being released?_

Synonym mapping has been added to populate the `bondReference` attribute in data type `InterestRatePayout` so that the reference to a bond underlier and the applicability of the Precedent bond condition. The latter denotes that the contract is only valid if the bond is issued and that if there is any dispute over the terms of the fixed stream then the bond terms will be used.

_Review Directions_

In the CDM Portal, select Ingestion and review the following example:

- fpml-5-10 > incomplete-products > inflation-swaps > inflation-swaps-ex02-yoy-bond-reference

# *Product Model - Enumeration Referencing FpML Scheme*

_What is being released?_

InflationRateIndexEnum is now linked to the FpML Coding Scheme through use of the `docReference` functionality.  The contents of the enumeration list will now be automatically kept in line with the latest FpML scheme information.

_Review Directions_

In the CDM Portal, select the Textual Browser and review `InflationRateIndexEnum`.
