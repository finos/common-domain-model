# *Product Model – Composable Product Qualification - Equity and Credit*

_What is being released?_

This release refactors Credit and Equity product qualification to leverage the hierarchical approach to product qualification in order to a) eliminate unnecessary repetition in the product qualification functions, and b) create a set of functions that can be reused in the development of the Digital Regulatory Reporting ("DRR") ruleset.

_Background_

Product Qualification functions are currently written as independent expressions for qualification into the ISDA product taxonomy. This leads to repetition in the model as the functional component identifying the Asset Class and Base Product components of the Taxonomy are repeated. This is in conflict with the CDM design principle of composability and re-usability.

Regulatory reporting rules also refer to classifications of products at a variety of levels as a component of regulatory functional expressions. For this purpose, utility rules have been developed which replicate the same structure.

This release focuses on creating a composable set of qualification rules for Credit and Equity products.

_Details_

New product qualification rules have been added to the model to describe the hierarchy of Interest Rate products as defined by the ISDA Product Taxonomy:

- Qualify_AssetClass_CreditDefault - Qualifies a product as having the Asset Class classification Credit Default.
- Qualify_AssetClass_Equity - Qualifies a product as having the Asset Class classification Equity.
- Qualify_BaseProduct_EquitySwap - Qualifies a product as having the Base Product Classification Equity Swap.

The following product qualification rules have been updated to leverage the new composable product qualification approach:

- Qualify_CreditDefaultSwap_SingleName
- Qualify_CreditDefaultSwap_Index
- Qualify_CreditDefaultSwap_IndexTranche
- Qualify_CreditDefaultSwap_Loan
- Qualify_CreditDefaultSwap_Basket
- Qualify_CreditDefaultSwaption
- Qualify_AssetClass_Equity
- Qualify_EquitySwap_PriceReturnBasicPerformance_SingleName
- Qualify_EquitySwap_TotalReturnBasicPerformance_SingleName
- Qualify_EquitySwap_PriceReturnBasicPerformance_Index
- Qualify_EquitySwap_TotalReturnBasicPerformance_Index
- Qualify_EquitySwap_PriceReturnBasicPerformance_Basket
- Qualify_EquitySwap_TotalReturnBasicPerformance_Basket
- Qualify_EquityOption_PriceReturnBasicPerformance_SingleName

_Review Directions_

In the CDM Portal, select the Textual Browser and review the functions detailed above.

# *Event Model - Compression and Novation Visualisation Samples*

_What is being released_

This release follows the recent work on the composable business event model and the corresponding creation function, `Create_BusinessEvent`. Visualisation examples have been created for compression and novation business events.

_Review Directions_

In the CDM Portal, select the Instance Viewer, review the visualisation examples in the Compression Business Event and Novation Business Event folders.
