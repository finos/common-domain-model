# *Product Model: Update to Interest Rate Product Qualifications*

_What is being released?_

This change provides an update to the set of Product Qualification functions for Interest Rate (IR) products. Sixteen of the seventeen pre-existing functions in this category have been revised to account for currently supported products in a consistent manner. The revised functions align with the ISDA v1 and/or v2 Taxonomy where applicable, are mutually exclusive, and fill gaps from the previous version, e.g. to exclude other payout types where applicable, and properly define rules for FRAs and OIS-indexed swaps where applicable.

All 51 FpML IR ingestion examples in the CDM now resolve to the correct qualifications.  Previously there were a number of cases for which the feature could not uniquely qualify the product or qualified it as the wrong product.   The new descriptions for this set of functions are compliant with the CDM style guide.  In addition, inline guidance comments have been added to explain each section of code in each of the IR Product Qualification functions so that implementers can more easily understand the purpose of each group of lines of code.  These functions are listed in the code in the logical order shown below, with the count of CDM ingestion examples shown in parentheses:

-	`Qualify_InterestRate_IRSwap_FixedFloat` (24 examples)
-	`Qualify_InterestRate_IRSwap_FixedFixed` (0 examples)
-	`Qualify_InterestRate_IRSwap_Basis` (1 examples)
-	`Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon` (3 examples)
-	`Qualify_InterestRate_IRSwap_FixedFloat_OIS` (4 examples)
-	`Qualify_InterestRate_IRSwap_Basis_OIS` (0 examples)
-	`Qualify_InterestRate_CrossCurrency_FixedFloat` (4 examples)
-	`Qualify_InterestRate_CrossCurrency_FixedFixed` (1 examples)
-	`Qualify_InterestRate_CrossCurrency_Basis` (2 examples)
-	`Qualify_InterestRate_InflationSwap_FixedFloat_YearOn_Year` (0 examples)
-	`Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon` (0 examples)
-	`Qualify_InterestRate_InflationSwap_Basis_YearOn_Year` (0 examples)
-	`Qualify_InterestRate_InflationSwap_Basis_ZeroCoupon` (0 examples)
-	`Qualify_InterestRate_Fra` (2 examples)
-	`Qualify_InterestRate_CapFloor` (3 examples)
-	`Qualify_InterestRate_Option_Swaption` (5 examples)
-	`Qualify_InterestRate_Option_DebtOption` (2 examples)

The `Qualify_InterestRate_IRSwap_FixedFloat_PlainVanilla` function was removed from the CDM because it does not align with market requirements for a product qualification in the CDM.  Note that the surviving `Qualify_InterestRate_IRSwap_FixedFloat` qualification (first item in the list above) represents the most basic IR Swap product type.

_Review directions_

In the CDM Portal, select the `TextualBrowser`, click inside the page, search in the text any of the functions listed above.
Also in the CDM Portal, select the Ingestion function, then select Products, then Rates, and review any of the examples, noting the `ProductQualifier`.  For example, select CAD-Long-Initial-Stub-versioned.xml and note that the `ProductQualifier` is `InterestRate_IRSwap_Basis`.

