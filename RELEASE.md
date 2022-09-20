# *Product Model - Legacy Product Model Concepts*

_Background_

The `Product` qualification functions contain logic referring to legacy product model concepts, specifically references to the cashflow payout that was previously used to represent payments.

_What is being released?_

- Removed product qualification logic that refers to legacy product model concepts (e.g. using cashflow to represent payments)

_Review directions_

In the CDM Portal, select Textual Browser and review the types mentioned above.

# *Product Model - FpML Mapping of Product Identifier Source*

_Background_

The data type `ProductIdentifier` comprises an identifier and source to uniquely identify a product. For example, a Security identifier may be assigned a source of ISIN.  The previous FpML mapping approach contained a number of issues where the identifier and source were mismatched.

_What is being released?_

- `Product Identifier` - A Build mapper for for the attribute `source` has been created to simplify the conditions and correctly map the relevant values.

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
