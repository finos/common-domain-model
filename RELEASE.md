# *DSL Syntax - Distinct keyword*

_What is being released?_

This release introduces the new DSL keyword `distinct` used to remove duplicates from a list.

The `distinct` keyword can appear after an attribute with multiple cardinality in a path expression, as shown in the example below.

- `quantity -> unitOfAmount -> currency distinct`

The operation will return a subset of the list containing only distinct elements.  It’s useful for removing duplicate elements from a list, and can be combined with other syntax features such as `count` to determine if all elements of a list are equal, as shown in the example below.

- `payout -> interestRatePayout -> payoutQuantity -> quantitySchedule -> initialQuantity -> unitOfAmount -> currency distinct count = 1`

The product qualification function, `Qualify_BaseProduct_CrossCurrency`, has been updated to use the distinct keyword to identify when a product has a different currency specified on each `InterestRatePayout`.

```
func Qualify_BaseProduct_CrossCurrency: 
	inputs: economicTerms EconomicTerms (1..1)
	output: is_product boolean (1..1)
	assign-output is_product:
		Qualify_AssetClass_InterestRate_Swap(economicTerms) = True
		and economicTerms -> payout -> interestRatePayout count = 2
	 	and (
	 	    economicTerms -> payout -> interestRatePayout -> payoutQuantity -> quantitySchedule -> initialQuantity -> unitOfAmount -> currency distinct count = 2
            or (
                economicTerms -> payout -> interestRatePayout -> payoutQuantity -> quantitySchedule -> initialQuantity -> unitOfAmount -> currency exists
                and economicTerms -> payout -> interestRatePayout -> payoutQuantity -> quantityMultiplier -> fxLinkedNotionalSchedule -> varyingNotionalCurrency exists
                and economicTerms -> payout -> interestRatePayout -> payoutQuantity -> quantitySchedule -> initialQuantity -> unitOfAmount -> currency &lt;> economicTerms -> payout -> interestRatePayout -> payoutQuantity -> quantityMultiplier -> fxLinkedNotionalSchedule -> varyingNotionalCurrency
            )
        )
```

_Review Directions_

In the CDM Portal, select the Textual Browser and review the following functions:

- `Qualify_BaseProduct_CrossCurrency`
- `Qualify_BaseProduct_IRSwap`
- `Qualify_BaseProduct_Inflation`

In the CDM Portal, select the User Documentation tile and navigate to the Rosetta DSL > Expression > Rosetta Path Expressions section, or review the documentation section directly:

- [DSL Documentation - Distinct](https://docs.rosetta-technology.io/dsl/expressions.html#distinct)
