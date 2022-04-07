# *Legal Agreement Extension to ensure user has a normalised model to categorise agreements – Story 1229*

_What is being Released_

1.	Change of data type name `LegalAgreementType` to `LegalAgreementIdentification` 
2.	Add condition to ensure correct use of defining CSA types.
3.	In addition changes have been made to logic in haircut percentages and related conditions 
4.	Within `MasterAgreementTypeEnum` `ISDA` has been amended to `ISDAMaster`

_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the change in name the data type `LegalAgreementType` has been renamed to `legalAgreementIdentification`. 

Please also inspect the other related conditions that have been updated in the model due to the change in data type `LegalAgreementType` being  renamed to `legalAgreementIdentification`.

Search for data type `AgreementName` and inspect the additional condition added for `creditSupportAgreement` this ensures that a credit support agreement type is specified if legal agreement type is a credit support agreement.

Search for data type `CollateralValuationTreatment` and inspect the changes to the related conditions `HaircutPercentage`, `FxHaircutPercentage` and `AdditionalHaircutPercentage`, These have all been adjusted so the higher range of population is <1 instead of <=1. `HaircutPercentage` has been adjusted so the lower range is >=0 instead of >0.

Search for data type `AssetType` and inspect the changes to the related condition `OtherAssetSubType`. This has been changed `assetType` <> `AssetTypeEnum` to `assetType` = `AssetTypeEnum`

Search for enum `MasterAgreementTypeEnum` and inspect changes to enumeration `ISDA` to `ISDAMaster` , please also inspect the updates to the related ISDACreate and FPML

