# CDM Model: ProductIdentifier Refactoring
 
_What is being released_
 
The refactoring rationalizes the use of ``ProductIdentifier`` and standardizes related references.  Changes include:
- Creating a ``ProductBase`` abstract type with one encapsulated data type: ``ProductIdentifier``
- Setting cardinality of the id and the source in ``ProductIdentifier`` to (1..1)
- Define Index, Loan, Commodity, and Security as extensions of ``ProductBase``
- Remove ``IdentifiedProduct``
- Restructure Security to align with ``CollateralAssetType`` and rename data types and attributes with the word bond by replacing bond with debt
- The products that were previously extensions of ``IdentifiedProduct`` can now be identified in ``securityType`` and related attributes in the ``Security`` data type: ``Bond``, ``ConvertibleBond``, and ``MortgageBackedSecurity`` are identified as the enumerated value of Debt in ``security``, and ``ConvertibleBond`` and ``MortgageBackedSecurity`` can be further identified in ``debtClass`` as the enumerated values of Convertible and AssetBacked. Similiarly, ``ExchangeTradedFund`` and ``MutualFund``  are identified as the enumerated value Fund in ``security``, and can be further identified in ``fundType``.  Otherwise, the pre-existing types (``Equity`` and ``Warrant``) can be identified as enumerated values in ``security``.
- In ``ProductIdentification``, replace ``productType`` with ``externalProductType`` (of type ``ExternalProductType``) and replace ``productId`` with ``productIdentifier`` (of type ``ProductIdentifier``)
 
_Review Directions_
 
In the CDM Portal, navigate to the Textual Browser and search for the data types noted above.

# *CDM Model: Model Definitions*

_What is being released_

Added definitions for attribute `TradableProduct.counterparties` and type `Counterparty`.