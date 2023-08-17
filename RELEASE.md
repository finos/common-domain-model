# _Commodity Model - Commodity Classification_

_Background_

The classification of commodity products represented with the type `CommodityReferenceFramework` has been deemed insufficently granular for the various product taxonomies used by practitioners for example the ESMA classification and ISDA's product taxonomies. This release upgrades the `ProductTaxonomy` type to accommodate a more generic representation of commodity classifications compatible with any classification systems.
The `productTaxonomy` attribute inherited from the `ProductBase` type with the `Commodity` type was also observed as the adequate  position for the classification rather than duplicate the information within the  `referenceFramework` of the `commodityProductDefinition` attribute.

_What is being released?_

- Added support for hierarchical, multi-layered commodity taxonomies by making changes to the the "Taxonomy" type and attributes of it.
- Removed the redundant commodity classification documented within the commodity  reference framework

_Data types_

- Removed elements `commodityBase` and `subCommodity` from the type `CommodityReferenceFramework`.
- Added conditions for type `Commodity` controlling the newly added elements.
- Added condition for type `Taxonomy` controlling the newly added elements.
- Cardinality for element `className` in type `TaxonomyClassification` changed to optional.
- Added element `ordinal` to type `TaxonomyClassification`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

