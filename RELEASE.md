# _Product Model - Product Classication for Commodity Products_

_Background_

The CDM's classification of commodities does not include support for the commodity classification requirements of several
commodity taxonomies such as those specified by EMIR Refit and by ISDA. This release adds a generic representation of
commodity classifications compatible with several classification systems, including these two.

_What is being released?_

- Added support for several, different commodity classifications.
- Added support for hierarchical, multi-layered commodity taxonomies.

_Data types_

- Removed elements `commodityBase` and `subCommodity` from type `CommodityReferenceFramework`.
- Added repeatable element `commodityClassification` from type `CommodityReferenceFramework`.
- Added conditions for type `CommodityReferenceFramework` to control the new added elements.
- Cardinality for element `className` in type `TaxonomyClassification` has been changed to optional.
- Added element `ordinal` to type `TaxonomyClassification`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes listed above.

# *Product Model - Price Components*

_Background_
- A price that consists of two additive components (e.g. clean + accrued) is represented in different ways depending on the use case.
- This change introduces a single pattern intended to simplify the model and facilitate re-usability and extensibility.

_What is being released?_

The core changes can be found in the following types both located in the `cdm.observable.asset` namespace.

_Data types_

- Added `PriceComposite` type which defines the inputs required to calculate a price as a simple composite of two other values.

- Updated `PriceSchedule` type with:
  - Added new field `priceType` of type  `PriceTypeEnum (1..1)`
  - Added new field `arithmeticOperator` of type `ArithmeticOperationEnum (0..1)`
  - Added new field `composite` of type  `PriceComposite (0..1)`
  - Updated `priceExpression` to use `PriceExpressionEnum` instead of obsolete `PriceExpression` type

_Supporting Changes_

A number of functions and synonyms have been modified to support this change.

The function changes can be found in the following namespaces:
- `cdm.event.common`
- `cdm.event.qualification`
- `cdm.observable.asset`
- `cdm.observable.event`
- `cdm.product.asset`
- `cdm.product.template`

The supporting synonym changes can be found in the following namespaces:
- `cdm.mapping.fpml.confirmation.tradestate`
- `cdm.mapping.fpml.confirmation.workflowstep`
- `cdm.mapping.ore`

_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the `PriceSchedule` and `PriceComposite` types. For the function and synonym changes please see the above listed files in the supporting changes section.
