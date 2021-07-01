# *Legal Agreement Model – Removal of SCSA from Legal Agreement Name enumeration list*

_What is being released?_

Removal of legal agreement type Standard Credit Support Annex from the list of identifiable document names in the `LegalAgreementNameEnum` list. The reason for this being, the document is no longer widely negotiated and research has indicated less than 5 of these exist operationally. Having it referenced in CDM has confused members and feedback has confirmed consensus agreement to remove from the model.


_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types and review as per the following instructions:

- Search for the data type enum `LegalAgreemenyNameEnum` and inspect the removal of `StandardCreditSupportAnnex` 
- Related synonyms in `synonym-cdm-fpml` where `StandardCreditSupportAnnex` is referenced have also been removed from CDM.
