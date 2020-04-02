# *CDM Model: Legal Documents Modelling*

_What is being released_

First phase of refactoring to support new legal documents modelling approach.

- Change to `LegalAgreement` to look for `ContractualTerms`.
- Addition of `ContractualTerms` to define an `Agreement` and `RelatedAgreements`
- Addition of `RelatedAgreements` for specification of related agreements terms
- Rename `Documentation->RelatedAgreement` in `model-derivatives-shared` and update reference in `model-cdm-product`.
- Addition of types to support Credit Support, Collateral Transfer and Security Agreements.
- Modelling of clauses in Security Agreement and addition of synonyms.

_Review Directions_

In the Textual Browser, review `LegalAgreement` and related types. 
