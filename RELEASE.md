# *Legal Agreement Model - ISDA Create Synonym Mappings*

_What is being released?_

This release contains a number of ISDA Create synonym mapping fixes, as detailled below.

* Added type `PledgeeRepresentativeRider` to model the terms of the Rider for the ISDA Euroclear 2019 Collateral Transfer Agreement with respect to the use of a Pledgee Representative, and updated the synonym mappings.
* Added attribute `JurisdictionRelatedTerms->belgianLawSecurityAgreement` to model whether the Belgian Law Security Agreement Addendum is deemed applicable by the parties, and updated the synonym mappings.
* Fixed synonym mappings for:
    * `PostingObligations->partyElection->eligibleCollateral->criteria->treatment->valuationTreatment->fxHaircutPercentage
    * `SubstitutedRegime->additionalRegime`
    * `TerminationCurrencyAmendment->effectiveDate`
    * `MinimumTransferAmountAmendment->effectiveDate->customProvision`
    * `TerminationCurrencyAmendment->effectiveDate->customProvision`
    * `SensitivityMethodology->specifiedMethodology`
    * `LegalAgreementNameEnum->ClauseLibrary`and `LegalAgreementNameEnum->MasterAgreement`
* Added mapping exclusions for fields not intended to be mapped.

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types specified above.

Select the Ingestion view and review the samples in `isda-create > test-pack > production`.
