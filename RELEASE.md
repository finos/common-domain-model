# _CDM Model - TaxonomySourceEnum_

_Background_
A DRR issue has been identified where reporting the Underlying CO values was not supported for MAS. To address this, we proposed replicating the reporting logic used for BaseProduct and SubProduct in EMIR. In CDM, this involves adding "MAS" as a value to the TaxonomySourceEnum, since the TaxonomySource in CDM determines the jurisdiction based on the commodityClassificationScheme being used. So the "MAS" value will be added in the TaxonomySourceEnum.

_What is being released?_

- Updated `TaxonomySourceEnum` in cdm.base.staticdata.asset.common:enum

_Enumerations_

- Updated `TaxonomySourceEnum` by adding MAS to support Monetary Authority of Singapore (MAS) as a taxonomy source.

_Review directions_

In Rosetta, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: [#3265](https://github.com/finos/common-domain-model/pull/3265)

# *Cardinality warning fixes*

_Background_

Due to a recent DSL update (see [DSL release notes](https://github.com/finos/rune-dsl/releases/tag/9.25.0)) which adds additional logical checks related to cardinality, some errors were found in the model.
These errors stem from an ambiguity of how to handle multiple elements in certain situations.

In general, the solution is to follow one of two approaches:
- Do we expect only a single item to be present? Then use the `only-element` operator.
- Should we support multiple elements? Then use the `extract` operation to handle all of them, and reduce them
  to a single result according to the context, e.g., using the `all = True` operation.

_What is being released?_

This release includes fixes for all cardinality related errors detected by the DSL, which are listed below.
It also includes a related fix to the `Qualify_CashAndSecurityTransfer` function, which is described in more detail below the list.

1. The function `SecurityFinanceCashSettlementAmount` contained a multiplication of which one operand - `securityQuantity` -
   was of multi cardinality. In practice, due to filtering, it should always be a single element, so this was fixed with `only-element`.
2. The function `ResolveSecurityFinanceBillingAmount` had a similar problem as in (1).
3. The function `Qualify_Repurchase` was performing an `only exists` operation on multiple `primitiveInstruction`s at
   once. Since a former check verifies there is only one, this was fixed with `only-element`.
4. The function `Qualify_Shaping` had a similar problem as in (3).
5. The function `Qualify_PartialDelivery` was comparing two multi cardinality quantities. In practice, due to filtering,
   both should always be a single element, so this was fixed with `only-element`.
6. The function `Qualify_OnDemandPayment` had a similar problem as in (3).
7. The function `Qualify_CashTransfer` was performing an `only exists` operation on multiple `primitiveInstruction`s at
   once. To resolve ambiguity, the check is now performed on all `primitiveInstruction`s separately using `extract`.
8. The function `Qualify_OpenOfferClearedTrade` was performing an `only exists` operation on two `primitiveInstruction`s at
   once. The check has been replaced with two equivalent `exists` operations, one for each of the two `primitiveInstruction`s.
9. The function `Qualify_Renegotiation` had a similar problem as in (8).
10. The function `Qualify_SecuritySettlement` was performing two `only exists` operations on unsupported input, which
    always resulted in `False`. This was fixed by replacing them with an `exists` check instead.
11. The function `Qualify_ValuationUpdate` was performing an `only exists` operation on multiple `primitiveInstruction`s at
    once. Given that the specification requires only a single component to be present, this was fixed with `only-element`.
12. The function `CheckAgencyRating` was performing a `contains` operation on two operands of single cardinality.
    The operation has been replaced with an equality check `=` instead.
13. The function `CheckAssetType` had a similar problem as in (13).
14. The function `Qualify_EquityOption_PriceReturnBasicPerformance_SingleName` was performing an `only exists` operation on multiple `payout`s
    at once. Since a former check verified there is only one, this was fixed with `only-element`.
15. The function `Qualify_EquityOption_PriceReturnBasicPerformance_Index` had a similar problem as in (15).
16. The function `Qualify_EquityOption_PriceReturnBasicPerformance_Basket` had a similar problem as in (15).
17. The function `Qualify_ForeignExchange_Spot_Forward` was performing an `only exists` operation on the `underlier`s
    of multiple `forwardPayout`s. Since a later check verifies there is only one, this was fixed with `only-element`.
18. The function `Qualify_ForeignExchange_Swap` was performing an `only exists` operation the `underlier`s
    of multiple `forwardPayout`s. To resolve ambiguity, the check is now performed on all `underlier`s separately using `extract`.
19. The function `Qualify_ForeignExchange_NDF` had a similar problem as in (17).
20. The function `Qualify_ForeignExchange_NDS` had a similar problem as in (18).
21. The function `Qualify_ForeignExchange_ParameterReturnCorrelation` was performing an `only exists` operation on multiple `basketConstituent`s at
    once. To resolve the ambiguity, the check is now performed on all `basketConstituent`s separately using `extract`.
22. The function `Qualify_ForeignExchange_VanillaOption` was performing an `only exists` operation on multiple `optionPayout`s
    at once. To resolve the ambiguity, the check is now performed on all `optionPayout`s separately using `extract`.
23. The function `Qualify_SecuritiesFinance` was performing an `only exists` operation on multiple `assetPayout`s
    at once. To resolve the ambiguity, the check is now performed on all `assetPayout`s separately using `extract`.

Due to the bug fix in the function `Qualify_SecuritySettlement`, another bug in the function `Qualify_CashAndSecurityTransfer`
came to light. According to its specification, a business event should only be qualified as a `CashAndSecurityTransfer`
only if the cash and security move in the same direction - however, this was never actually checked. The check has been implemented
and three expectation files have been updated accordingly.

_Review Directions_

Please inspect the changes identified above for the functions and conditions in the Textual Viewer Rosetta.

The changes can also be reviewed in PR: [#3294](https://github.com/finos/common-domain-model/pull/3294).
