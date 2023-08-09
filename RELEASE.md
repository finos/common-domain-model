# _Product Model - Product Classication for Commodity Products_

_Background_

The CDM classification of commodities does not include support for the commodity classification requirements of commodity taxonomies such as those for EMIR and ISDA. This release adds a generic representation of commodity classifications compatible with several classification systems including these two.

_What is being released?_

- Added support for several, different commodity classifications.
- Added support for hierarchical, multi-layered commodity taxonomies.


_Data types_

- Removed elements `commodityBase` and `subCommodity` from type `CommodityReferenceFramework`.
- Added repeatable element `commodityClassification` from type `CommodityReferenceFramework`.
- Added conditions for type `CommodityReferenceFramework` controlling the new added elements.
- Cardinality for element `className` in type `TaxonomyClassification` changed to optional.
- Added element `ordinal` to type `TaxonomyClassification`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes listed above.
