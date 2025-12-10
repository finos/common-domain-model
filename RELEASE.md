# Credit Support Document Concept of "catch all" - Added CreditDocumentSupportTypes and relevant validation

_Background_

In the situation where an ISDA master agreement has been defined in the CDM structure of legal documentation, if it has any supporting documents that are not currently available in CDM format, or are simply not to hand for data entry, the current structure enforces that the legalAgreement must be defined if it is specified.
This is counter intuitive, in so much that constructing a CDM representation of an ISDA (if it has an accompanying support document such as a CSA or CSD, etc.) requires the full CDM representation of that supporting document.
As a workaround currently a user could specify the details as "None" or "Any", but that would technically be incorrect, and it is impossible to capture the details without needing to have available the supporting document to convert to CDM format.

_What is being released?_

Add a "catch all" string array, that allows a user to express which credit support document types are being provided, without needing to completely define the support document in question in the legalAgreement format. This will allow for an ISDA agreement to be captured in CDM format without needing to locate and categorise all supporting documents immediately.
Also updated the validation such that if "Specified" is selected as the creditSupportDocumentTerms, EITHER one of the actual CDM representation OR a string array representation can be provided. This ensure backwards compatibility and removes potential for overlap

_Review Directions_

Changes can be reviewed in PR: [#4256](https://github.com/finos/common-domain-model/pull/4256)