# *User Documentation: Namespace section*

_What is being released_

The Namespace section has been added to the CDM user documentation, in line with recent changes to the model.

Notable changes and updates introduced:

- New Namespace section.
- Detailed description of individual components within the Namespace concept.
- Addition of relevant examples to demonstrate model content.

_Review direction_

In the CDM Portal, review the CDM Documentation, in particular the section:

- [Namespace](https://docs.rosetta-technology.io/cdm/documentation/source/documentation.html#namespace)

# *Updated Product Identification Type Enumeration*

_What is being released_

The enumeration ProductIdSourceEnum has been changed to ProductIdTypeEnum to more accurately define a representation of an enumerated list of Product Identification Types.

_Review Directions_

In the CDM portal, review the enumeration listed above.

# *CDM Model: Minor update to regulatory rule logic and regulatory test pack*

_What is being released_

Regulatory rule logic and regulatory test cases were updated to account for recent changes to Primitives.

_Review Directions_

* See `reporting rule TradeForEvent`, which has re-enabled support for events using `QuantityChangePrimitive`.

# *CDM Model: Update to Legal Agreement and Eligible Collateral model*

_What is being released_

The Legal Agreement and Eligible Collateral model has been updated to address deficiencies in the model identified following review with ISDA members.

- Regime - rationalised data type to more efficiently reflect clause structure.
- Substituted Regime - aligned structure of Substituted Regime data type with Regime data type to achieve consistency in modelling approach.
- Asset Criteria - added Listing Criteria to filter eligibility based on Exchange, Sector and/or Index.
- Various small fixes to model descriptions and ISDA Create synonyms.

_Review Directions_

- In the CDM portal textual browser review data types `Regime`, `SubstitutedRegime`, and `ListingType` within `AssetCriteria`.
- In the CDM review ISDA Create ingestion samples.
