# *Product Model - Credit: Update to Credit Product Qualifications*

_What is being released?_

This release provides an updated set of Credit Product Qualification functions, introducing four new functions, to bring the new total to six.  All of the new and revised functions account for currently supported products in a consistent manner. The revised functions align with the ISDA v1 and/or v2 Taxonomy where applicable, are mutually exclusive, and fill gaps from the previous version, e.g. to separately identify single name Credit Default Swaps (CDS) from loan and index CDS.  

The descriptions for each of these functions are compliant with the CDM style guide.  In addition, inline guidance comments have been added to explain each section of code in each of the Credit Product Qualification functions so that implementers can more easily understand the purpose of each group of lines of code.  This release also adds one new Credit Product FpML Ingestion Examples, bringing the total to 21. The new and revised functions are listed below, with the count of CDM ingestion examples shown in parentheses:

New Product Qualification Functions:
- `Qualify_CreditDefaultSwap_Loan` (2)
- `Qualify_CreditDefaultSwap_Index` (4)
- `Qualify_CreditDefaultSwap_IndexTranche` (1 - new)
- `Qualify_CreditDefaultSwap_Basket` (2)

Revised Product Qualification Functions:
- `Qualify_CreditDefaultSwap_SingleName` (8)
- `Qualify_CreditDefaultSwaption`  (4)

_Review directions_

In the CDM Portal, select the Textual Browser to inspect the changes to the functions specified above.  Also, select the Ingestion feature Products->Credit, and choose one or more examples to view, such as `cd-ex01-long-asia-corp-fixreg-versioned.xml` and note the productQualifier for each in the CDM panel on the right side.



