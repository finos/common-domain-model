# _Commodity Model - Commodity Classification_

_Background_

The CDM model's classification of commodities does not provide support
for the commodity classification requirements of EMIR Refit's and ISDA's
commodity taxonomies. This release changes the `ProductTaxonomy` structure 
in order to allow for it to accommodate a generic representation of 
commodity classifications compatible with ISDA's, EMIR's and other commodity
classification systems.

_What is being released?_

- Added support for several different commodity classifications.
- Added support for hierarchical, multi-layered commodity taxonomies.


_Data types_

- Removed elements `commodityBase` and `subCommodity` from type `CommodityReferenceFramework`.
- Added conditions for type `Commodity` controlling the new added elements.
- Cardinality for element `className` in type `TaxonomyClassification` changed to optional.
- Added element `ordinal` to type `TaxonomyClassification`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

