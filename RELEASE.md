# *Credit Notations – Agency Rating Criteria additions and added descriptions – USER STORY 677*

_What is being released?_

Addition to data type (AgencyRatingCriteria) to the list of available data attributes the following has been added (boundary) which can be used to indicate the boundary of a credit agency rating i.e minimum or maximum. An enumeration list is added to support this (CreditNotationBoundaryEnum).

Missing descriptions have been added to attributes and enumeration list (CreditNotationMismatchResolution).

Under data type (AgencyRatingCriteria) a typo has been corrected to an existing data attribute (qualifier) and changed from quantifier to qualifier this has also been changed elsewhere in the model where referenced. 


_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types and review as per the following instructions:

Search for the data type `AgencyRatingCriteria` and inspect the descriptions added to attributes `mismatchResolution` and `referenceAgency`. Also inspect the new added data attribute boundary and its related enumerations `CreditNotationBoundaryEnum` with descriptions.

Search for `CreditNotationMismatchResolutionEnum` and inspect the descriptions now populated that where previously missing in the model.

Finally review the changes made to data attribute `qualifier QualifierEnum` where typos have been corrected to detail (qualifier) rather than (quantifier), this has been corrected whereelse referenced in CDM. 

