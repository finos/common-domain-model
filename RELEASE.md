# Product Model - FpML synonym mappings for credit products

_Background_

This release updates and extends the FpML mapping coverage for the product model.

_What is being released?_

- Mappings added to populate CDM attribute `CreditDefaultPayout -> generalTerms -> basketReferenceInformation -> basketName` with FpML path `underlyer -> basket -> basketName` for Credit products

- Mappings added to populate CDM attribute `CreditDefaultPayout -> generalTerms -> basketReferenceInformation -> basketId` with FpML paths `underlyer -> basket -> basketId` or `underlyer -> basket -> id` for Credit products

- Mappings added to populate CDM attribute `CreditDefaultPayout -> generalTerms -> basketReferenceInformation -> referencePool -> referencePoolItem -> referencePair` with FpML path `underlyer -> referenceEntity` for Credit products

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
