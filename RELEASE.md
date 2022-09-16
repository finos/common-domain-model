# *Product Model - FpML Mapping of Product Identifier Source*

_Background_

The data type `ProductIdentifier` comprises an identifier and source to uniquely identify a product. For example, a Security identifier may be assigned a source of ISIN.  The previous FpML mapping approach contained a number of issues where the identifier and source were mismatched.

_What is being released?_

- `Product Identifier` - Build mapper for ProductIdentifier.source mappings has been created to simplify the conditions to correctly Map values.

_Review directions_

In the CDM Portal, select Ingestion and review the FpML examples, including:

- fpml-5-10 > products > credit > cdm-cds-ref-ob-versioned.xml
- fpml-5-10 > products > equity > eqd-ex01-american-call-stock-long-form.xml

# *Event Model - Price of Packaged Transaction*

_Background_

The `BusinessEvent` type allows for the representation of packages details with the packageInformation attribute which is of type `IdentifiedList`.  This release adds a `price` attribute to reflect the price of the related package.

_What is being released?_

- Added attribute `price` of type `Price` into the `IdentifiedList` type.

_Review directions_

In the CDM Portal, select Textual Browser and review the types mentioned above.
