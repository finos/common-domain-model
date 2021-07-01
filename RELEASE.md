# *Legal Agreement Model – Agreement Type/Identification  and addition of Margin Type*

_What is being released?_

Change of the name of data type `LegalAgreementType` to `LegalAgreementIdentification` as more relevant to its purpose. This is also referenced in several places throughout the CDM, therefore changes have been made where used. In addition to this change another data label has been added within this data type called `marginType`. This is to allow users to identify the margin type of an agreement, and an enumeration list has been added to support this to specify the margin type such as initial or variation margin. Conditions are added to enforce using the margin type for relevant agreement types only and specific vintage years.
 
_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types and review as per the following instructions:

- Search for the data type `LegalAgreementIdentification` and inspect the change from `LegalAgreementType`. The change is also reflected where `agreementType` is referenced under `LegalAgreementBase`. The word `Type` is replaced with `Identification` throughout the CDM where referenced for these data labels.  
- Changes have also been made to the description of data label `LegalAgreementNameEnum` to remove reference to variation margin.
- In `LegalAgreementIdentification`, review a new data attribute called `marginType` and its associated enumeration list called `AgreementMarginTypeEnum` which contains the required margin types. A condition has been added to the CDM so that if `marginType` is used, it would only be for specific documents identified by data type `LegalAgreementNameEnum` such as the CSA CSD and CTA . Another condition is added so that certain margin types from the `AgreementMarginTypeEnum` can only be used for specified published vintage years on documents. For example `LegacyMargin`  only if vintage is < 2016, or `VariationMargin` and `InitialMargin` if vintage is >= 2016 
- In `synonym-cdm-fpml` where `LegalAgreementType` is referenced, the CDM side of the synonym has been amended to the new label `LegalAgeeementIdentification`.
