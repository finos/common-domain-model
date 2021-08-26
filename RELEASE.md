# *Product Model – FpML Synonym Mappings for Equity Swaps and Equity Options*

_What is being released?_

This release contains a number of FpML synonym mapping fixes for Equity Swaps and Equity Options as detailed below

_Details_

- For Equity Options the `OptionPayout` has a `PayoutQuantity` populated referencing the `PriceQuantity`.
- For all products the `ExternalProductType` is enumerated based on the metadata scheme associated with the product type value.
- For specification of Equity Dividend treatment the following attributes and enumerations, and associated synonym mappings, have been added to data type `DividendReturnTerms`.
    - Attributes `nonCashDividendTreatment, dividendComposition and specialDividends`.
    - Enumerations `DividendCompositionEnum and NonCashDividendTreatmentEnum` for specification of Equity Dividend Treatment.
- Added boolean election `mutualEarlyTermination` to `OptionalEarlyTermination`.
- Added mappings for `multipleExercise` and `partialExercise`.

_Review Directions_

In the CDM Portal, select the Ingestion view and review the samples in `fpml-5-10->products->equity`.

# *Product Model – Composable Product Qualification*

_What is being released?_

A hierarchical approach to product qualification has been introduced in order to a) eliminate unnecessary repetition in the product qualification functions, and b) create a set of functions that can be reused in the development of the Digital Regulatory Reporting ("DRR") ruleset.

_Background_

Product Qualification functions are currently written as independent expressions for qualification into the ISDA product taxonomy.  This leads to repetition in the model as the functional component identifying the Asset Class and Base Product components of the Taxonomy are repeated.  This is in conflict with the CDM design principle of composability and re-usability.

Regulatory reporting rules also refer to classifications of products at a variety of levels as a component of regulatory functional expressions.  For this purpose, `utility rules` have been developed which replicate the same structure.

This release focuses on creating a composable set of qualification rules for Interest Rate products that can then be extended to cover additional asset classes, and also used to simplify the development of the DRR rules.

_Details_

New product qualification rules have been added to the model to describe the hierarchy of Interest Rate products as defined by the ISDA Product Taxonomy:
- Qualify_AssetClass_InterestRate_Swap - Qualifies a product as having the Asset Class classification Interest Rate.
- Qualify_BaseProduct_IRSwap - Qualifies a product as having the Base Product classification Interest Rate Swap.
- Qualify_BaseProduct_CrossCurrency - Qualifies a product as having the Base Product classification Cross Currency.
- Qualify_BaseProduct_Fra - Qualifies a product as having the Base Product classification Forward Rate Agreement.
- Qualify_BaseProduct_Inflation - Qualifies a product as having the Base Product classification Inflation Swap.
- Qualify_SubProduct_FixedFloat - Qualifies a product as having the Sub Product classification Fixed Float.
- Qualify_SubProduct_FixedFixed - Qualifies a product as having the Sub Product classification Fixed Fixed.
- Qualify_SubProduct_Basis - Qualifies a product as having the Sub Product classification Basis.
- Qualify_Transaction_ZeroCoupon - Qualifies a product as having the Transaction classification Zero Coupon.
- Qualify_Transaction_YoY - Qualifies a product as having the Transaction classification Year on Year.
- Qualify_Transaction_OIS - Qualifies a product as having the Transaction classification OIS.

The following product qualification rules have been updated to leverage the new composable product qualification approach:
- Qualify_InterestRate_IRSwap_FixedFloat
- Qualify_InterestRate_IRSwap_FixedFixed
- Qualify_InterestRate_IRSwap_Basis
- Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon
- Qualify_InterestRate_IRSwap_FixedFloat_OIS
- Qualify_InterestRate_IRSwap_Basis_OIS
- Qualify_InterestRate_CrossCurrency_FixedFloat
- Qualify_InterestRate_CrossCurrency_Basis
- Qualify_InterestRate_CrossCurrency_FixedFixed
- Qualify_InterestRate_InflationSwap_FixedFloat_YearOn_Year
- Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon
- Qualify_InterestRate_InflationSwap_Basis_YearOn_Year
- Qualify_InterestRate_InflationSwap_Basis_ZeroCoupon
- Qualify_InterestRate_Fra
- Qualify_InterestRate_CapFloor

_Review Directions_

In the CDM Portal, select the Textual Browser and review the functions detailed above.

# *Infrastructure – CDM Portal Download*

- Fixed CDM portal download link.

_Review Directions_

In the CDM Portal, select Downloads, and the links to download the CDM distributable are now working.