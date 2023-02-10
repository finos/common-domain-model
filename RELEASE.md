# *Product Model - Orphan Types clean-up*

_Background_

This release relocates and deletes some unused types in the model and adjusts the corresponding FpML synonym mappings.

_What is being released?_

- Attribute `personRole` of type NaturalPersonRole added to type `Party`
- Attribute `assetPool` of type AssetPool added to type `Product`
- Attribute `assetPool` of type AssetPool added to type `Product`
- Enumeration 'MortgageSectorEnum' was deleted
- Attribute commodityInfoPublisher which uses the enumeration'commodityInfoPublisherEnum' added to type CommodityProductDefinition
- Attribute `deliveryNearby` added to type `DeliveryDateParameters`

Related synonymm mappings were also adjusted to deal with changes.

_Review Directions_

In the CDM Portal, select the Textual Browser to inspect the types mentioned above and review the changes.
