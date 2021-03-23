# *Model Change - Update to Equity Product Qualifications and addition of a generic Index Option Product Qualification*

_What is being released?_

This release provides an updated set of Equity Product Qualification functions, introducing four new functions, revising one, and dropping one to bring the new total to five. The release also adds a generic Index Option Product Qualification function. All of the new and revised functions account for currently supported products in a consistent manner. The revised functions align with the ISDA v1 and/or v2 Taxonomy where applicable, are mutually exclusive, and fill gaps from the previous version, e.g. to separately identify price return products.  The descriptions for each of these functions are compliant with the CDM style guide.  In addition, inline guidance comments have been added to explain each section of code in each of the IR Product Qualification functions so that implementers can more easily understand the purpose of each group of lines of code.  

This release also adds two new Equity Product FpML Ingestion Examples, one for `Qualify_EquityOption_PriceReturnBasicPerformance_SingleName` and one for `Qualify_IndexVanillaOption`, which appears in the Equity group, bringing the total to eleven.

This expanded set of function are listed below, with the count of CDM ingestion examples shown in parentheses:

New Product Qualification Functions:
- `Qualify_EquitySwap_TotalReturnBasicPerformance_SingleName` (5)
- `Qualify_EquitySwap_PriceReturnBasicPerformance_Index` (2)
- `Qualify_EquitySwap_TotalReturnBasicPerformance_Index' (1)
- `Qualify_EquityOption_PriceReturnBasicPerformance_SingleName` (1)
- `Qualify_IndexVanillaOption`(1)

Revised Product Qualification Function:
- `Qualify_EquitySwap_PriceReturnBasicPerformance_SingleName`(1)

Dropped Product Qualification Function:
- `Qualify_EquitySwap_ParameterReturnDividend_Basket`: Deferred until fully supported in the CDM


_Review directions_

In the CDM Portal, select the Textual Browser to inspect the changes to the functions specified above.  Also, select the Ingestion feature Products->Equity, and choose one or more examples to view, such as `eqs-ex01-single-underlyer-execution-long-form-other-party.xml` and note the `productQualifier` for each in the CDM panel on the right side.
