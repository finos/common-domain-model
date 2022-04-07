# *Legal Agreement Extensions to ensure user has a normalised model to categorise agreements and related data.*

_What is being Released_


1.	Change of data type name `LegalAgreementType` to `LegalAgreementIdentification`, This change uses a more relevant data label name for identifying the legal agreement and avoids the over use of the word 'type' in the structure. 

2.  Conditions have been updated in the model due to the change in data type `LegalAgreementType` being  renamed to `legalAgreementIdentification`.

2.	Data type `AgreementName` has an additional condition added for `creditSupportAgreement` This condition ensures correct use of defining CSA types, so that a credit support agreement type is specified if legal agreement type is a credit support agreement.

3.	Changes have been made to the logic in haircut percentages and Asset type conditions. In Data type `CollateralValuationTreatment` the related conditions for `HaircutPercentage`, `FxHaircutPercentage` and `AdditionalHaircutPercentage`, have all been adjusted so the higher range of population is <1 instead of <=1. `HaircutPercentage` has been adjusted so the lower range is >=0 instead of >0. The reason for these changes are becuase members reported errors when using the data, these change will fix the issues

4.   Data type `AssetType` and its related condition `OtherAssetSubType`, has been changed as afoolows (`assetType` <> `AssetTypeEnum` to `assetType` = `AssetTypeEnum`).  The reason for these changes are becuase members reported errors when using the data, these change will fix the issues

5.	Within `MasterAgreementTypeEnum` `ISDA` has been amended to `ISDAMaster`, this change was recommended by ISDA legal team and is more in line with the document name and avoids confusion with the publisher ehcih is used when identifying the agreement type at a highr level in the model. Updated have also been made to the related ISDACreate and FPML synonyms


_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the changes outlined above 

