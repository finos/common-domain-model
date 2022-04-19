# *Legal Agreement Model - Enhancements for the categorisation of legal agreement*

_Background_

The categorisation of legal agreements has been enhanced and a few data validation rule have been adjusted based on feedback from users.


_What is being Released_


1.	Change of data type name `LegalAgreementType` to `LegalAgreementIdentification`. This change uses a more relevant type name for identifying the legal agreement and avoids the unnecessary use of suffix 'type' in the label. 

2.  Conditions have been updated in the model due to the renaming of `LegalAgreementType` to `legalAgreementIdentification`.

2.	Data type `AgreementName` has an additional condition added for `creditSupportAgreement` This condition ensures a credit support agreement type is specified if the legal agreement is a credit support agreement.

3.	Changes have been made to the logic in haircut percentages and Asset type conditions. In Data type `CollateralValuationTreatment` the related conditions for `HaircutPercentage`, `FxHaircutPercentage` and `AdditionalHaircutPercentage`, have all been adjusted so the higher range of population is <1 instead of <=1. `HaircutPercentage` has been adjusted so the lower range is >=0 instead of >0. These changes will address errors reported by users when using the model.

4.   Data type `AssetType` and its related condition `OtherAssetSubType` have been changed as foolows (`assetType` <> `AssetTypeEnum` to `assetType` = `AssetTypeEnum`).  These changes will address errors reported by users when using the model.

5.	Within `MasterAgreementTypeEnum` `ISDA` has been amended to `ISDAMaster`, this change was recommended by ISDA legal team and is more in line with the document name and avoids confusion with the publisher used when identifying the agreement type at a higher level in the model. Updates have also been made to the related ISDACreate and FPML synonyms


_Review Directions_

In the CDM Portal, select the Textual Browser and inspect the changes outlined above 

