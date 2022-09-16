# *Event Model - Product Identifier*

_Background_

The `Product Identifier.source` had complex mapping condition. 

_What is being released?_

- `Product Identifier` - Build mapper for ProductIdentifier.source mappings.

_Review directions_

In the CDM Portal, select Textual Browser and review the types mentioned above.

# *Event Model - Price of Packaged Transaction*

_Background_

The `BusinessEvent` type allows for the representation of packages details with the packageInformation attribute which is of type `IdentifiedList`.  This release adds a `price` attribute to reflect the price of the related package.

_What is being released?_

- Added attribute `price` of type `Price` into the `IdentifiedList` type.

_Review directions_

In the CDM Portal, select Textual Browser and review the types mentioned above.
