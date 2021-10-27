# *Product Model - FpML Mapping of Notional*

_What is being released?_

This release corrects the representation of `Quantity` in cases where notional is referenced between legs in FpML.

The existing FpML synonym mapping of notionals into `PriceQuantity` is inconsistent across products, which creates challenges for creation of DRR rules.

The following changes standardise location of initial quantity within `PayoutBase` creating normalized location for capture of value and onward referencing:

- `fxLinkedNotionalSchedule -> initialQuantity` has been moved up to `payoutQuantity -> quantitySchedule`.
- Funding leg of an Equity Swap has initial notional populated in `payoutQuantity -> quantitySchedule` with reference to the only `PriceQuantity -> quantity`.
- Population of `PriceQuantity` is unchanged.

_Review Directions_

In the CDM Portal, select the Ingestion Panel and review the following samples:

- fpml-5-10 > products > rates > cdm-xccy-swap-after-usi-uti.xml
- fpml-5-10 > products > rates > cdm-xccy-swap-before-usi-uti.xml
- fpml-5-10 > products > equity > eqs-ex01-single-underlyer-execution-long-form.xml
- fpml-5-10 > products > equity > eqs-ex01-single-underlyer-execution-long-form-other-party.xml
- fpml-5-10 > products > equity > eqs-ex06-single-index-long-form.xml
- fpml-5-10 > products > equity > eqs-ex09-compounding-swap.xml
- fpml-5-10 > products > equity > eqs-ex10-short-form-interestLeg-driving-schedule-dates.xml
- fpml-5-10 > products > equity > eqs-ex11-on-european-single-stock-underlyer-short-form.xml

