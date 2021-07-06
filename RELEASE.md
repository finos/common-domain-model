# *Legal Agreement Model – Conditions added to Concentration Limit*

_What is being released?_

A condition has been added to CDM to ensure that when a concentration limit is used, a choice of concentration type is made to either use `ConcentrationLimitCriteria` for a specific description of where to apply the concentration limit or `ConcentrationLimitType` for a higher level generic description of where to apply the concentration limit.
In addition a condition is also added to ensure that when the data type `ConcentrationLimit` is used, a concentration value choice must be made to either use `valueCap` or `percentageCap`.

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types and review as per the following instructions:

- Search for the data type `ConcentrationLimit` under the allowed data attributes are 2 additional conditions `ConcentrationLimitValueChoice` and `ConcentrationLimitTypeChoice` Please review these conditions to ensure they determine the correct outcomes required.

# *Legal Agreement Model – Removal of SCSA from Legal Agreement Name enumeration list*

_What is being released?_

Removal of legal agreement type Standard Credit Support Annex from the list of identifiable document names in the `LegalAgreementNameEnum` list. The reason for this being, the document is no longer widely negotiated and research has indicated less than 5 of these exist operationally. Having it referenced in CDM has confused members and feedback has confirmed consensus agreement to remove from the model.

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types and review as per the following instructions:

- Search for the data type enum `LegalAgreemenyNameEnum` and inspect the removal of `StandardCreditSupportAnnex` 
- Related synonyms in `synonym-cdm-fpml` where `StandardCreditSupportAnnex` is referenced have also been removed from CDM.
