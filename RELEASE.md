# *Legal Agreement Model - ISDA Create Synonym Mappings*

_What is being released?_

This release contains a number of ISDA Create synonym mapping fixes, as detailed below.

* Fixed synonym mappings for:
  * `CustodianEvent->endDate`
  * `CollateralTransferAgreementElections->terminationCurrencyAmendment`
  * `CreditSupportAgreementElections->terminationCurrencyAmendment`
  * `AmendmentEffectiveDate->specificDate`
  * `AmendmentEffectiveDate->customProvision`
* Added mapping exclusions for fields not intended to be mapped.

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types specified above.

Select the Ingestion view and review the samples in `isda-create > test-pack > production`.
