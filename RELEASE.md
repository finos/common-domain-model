# *Event Model - Map FpML Record-Keeping Novation Event*

_What is being released?_

This release adds FpML Record-Keeping synonyms for the notional amount, and parties of the before and after states representing a Novation event.

_Review Directions_

In the CDM Portal, select the Ingestion Panel and review the following samples:

- fpml-5-10 > record-keeping > record-ex113-novation-from_transferor.xml
- fpml-5-10 > processes > msg-ex52-execution-advice-trade-partial-novation-C02-00.xml
- fpml-5-10 > processes > msg-ex53-execution-advice-trade-partial-novation-correction-C02-10.xml

# *Event Model - Map FpML Record-Keeping Clearing Event*

_What is being released?_

This release adds FpML Record-Keeping synonyms for clearing events, including timestamps and the workflow event status.

_Review Directions_

In the CDM Portal, select the Ingestion Panel and review the following samples:

- fpml-5-10 > record-keeping > record-ex143-new-trade-CFTC-clearing.xml
- fpml-5-10 > record-keeping > record-ex149-new-beta-trade-CFTC-SEC-and-canada.xml

# *Product Model - Additional Day Count Fraction*

_What is being released?_

This release adds the day count fraction `ACT/ACT.ISMA` to the model denoting a Fixed/Floating Amount that will be calculated in accordance with Rule 251 of the statutes, by-laws, rules and recommendations of the International Securities Market Association.

_Review Directions_

In the CDM Portal, select the Textual Browser and navigate to the above enumeration.


# *Product Model - Early Termination Provisions*

_What is being released?_

This release adjusts data types `OptionalEarlyTermination` and `MandatoryEarlyTermination` to contain the required settlement terms data type and adds synonym mappings for Optional and Mandatory Early Termination events in the CDM.

_Details_

- `OptionalEarlyTermination->cashSettlement` updated to be of data type `SettlementTerms` in order to include `settlementDate`
- `MandatoryEarlyTermination->cashSettlement` updated to be of data type `SettlementTerms` in order to include `settlementDate`
- Synonym mappings added to support ingestion of FpML 5.10
- The following trades contained in the `fpml-5-10 > incomplete-products > interest-rate-derivatives` folder have been moved to `fpml-5-10 > products > rates`
  - ird-ex11-euro-swaption-partial-auto-ex
  - ird-ex13-euro-swaption-cash-with-cfs
  - ird-ex14-berm-swaption
  - ird-ex15-amer-swaption
  - ird-ex16-mand-term-swap
  - ird-ex17-opt-euro-term-swap
  - ird-ex18-opt-berm-term-swap
  - ird-ex19-opt-amer-term-swap
  - ird-ex20-euro-cancel-swap
  - ird-ex21-euro-extend-swap
  - ird-ex26-fxnotional-swap-with-cfs
  - ird-ex28-bullet-payments
  - ird-ex31-non-deliverable-settlement-swap
  - ird-ex34-MXN-swap
  - ird-ex35-inverse-floater-inverse-vs-floating
  - ird-ex36-amer-swaption-pred-clearing

_Review Directions_

In the CDM Portal, select the Textual Browser and navigate to the above data type.
In the CDM Portal, select the Ingestion panel and review sample trades specified above, specifically `ird-ex16-mand-term-swap` and `ird-ex17-opt-euro-term-swap`.

# *Product Model - FINOS*
