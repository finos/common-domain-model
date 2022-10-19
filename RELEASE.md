# _Product Model - Representation of Termination Provisions_

_Background_

The CDM already provides termination provisions such as optional or mandatory termination, extendible and cancellable provisions. Some security finance transactions can have specific features such as open, extendible or evergreen that also affect the way they are terminated, but those features were not all represented using the existing CDM termination provisions and were instead nested within the payout details.

_What is being released?_

This release positions a new dedicated termination provision component, applicable across asset classes, inside a product's economic terms. This component assembles the existing termination provisions (early termination, extendible and cancellable) plus the evergreen provisions into a single data type.

In turn, evergreen provisions and the associated duration components are being removed from the security finance payout. 

_Data types_

- Modified `EvergreenProvision` type:

  - Removed all its existing attributes
  - Added `singlePartyOption`, `noticePeriod`, `noticeDeadlinePeriod`, `noticeDeadlineDateTime`, `extensionFrequency` and `finalPeriodFeeAdjustment` attributes

- Modified `ExtendibleProvision` type:

  - Added `singlePartyOption`, `noticeDeadlinePeriod` and `noticeDeadlineDateTime` attributes (same as in `EvergreenProvision`)
  - Added `extensionTerm` and `extensionPeriod` attributes
  - Marked `followUpConfirmation` attribute as optional

- Added new `TerminationProvision` type:

  - Included `cancellableProvision`, `earlyTerminationProvision` and `extendibleProvision` attributes in that data type
  - Added `evergreenProvision` attribute in that data type

- Modified `EconomicTerms` type:

  - Added `terminationProvision` attribute
  - Removed `cancellableProvision`, `earlyTerminationProvision` and `extendibleProvision` attributes (now encapsulated into `terminationProvision`)
  - Added `SecurityFinancePayoutDividendTermsValidation` condition (to enforce that a transaction with dividend terms specified must be a term trade)
  - Added `ExtendibleProvisionExerciseDetails` condition (to enforce that the appropriate exercise type is associated with each termination provision)

- Removed `Duration` type (just marked as deprecated, for backward compatibility reasons), which previously contained `evergreenProvision`
- Modified `SecurityFinancePayout` type:

  - Marked `duration` attribute as deprecated (its underlying `evergreenProvision` attribute is now encapsulated in `terminationProvision`)
  - Removed `DividendTermsValidation` condition (condition now positioned in `EconomicTerms`)

- Modified `TradableProduct` type, so that paths used in conditions use the new `terminationProvision` attribute

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above. 

In the CDM Portal, select the Ingestion Panel and review the following examples:

- products > equity > `eqs-ex09-compounding-swap`
- products > rates > `ird-ex16-mand-term-swap`
- products > repo > `repo-ex02-repo-open-fixed-rate`
