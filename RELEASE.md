# *Product Model - Representation of Package Transactions*

_What is being released_

Modelling components for the representation of package transactions have been added to the product model and mapped from FpML

_Background_

A package transaction consists of several trades which, even though they may be represented separately, are tied into a single package and typically executed together. Executing trades as a package has implications for post-trade processes such as reporting or confirmation and therefore requires specific package attributes to be captured.

This capture has 2 components:

1. When separate trades are executed together as part of a single package, the atomic business event representing such bundled execution needs to record the attributes of that package - i.e. at minimum, an identifier for the package
2. Individual trades that are part of a package must each refer to such package

_Details_

The package representation is achieved by introducing the following:

- A new `IdentifiedList` data type is designed to represent any list of objects as a list of identifiers (when those objects have an identifier), with an identifier for the list itself. While this data type can be used to represent a package as a list of trade identifiers, the naming is generic to allow potential re-use for other use cases. The identifier attributes use the generic `Identifier` data type.
- A new `packageInformation` attribute of type `IdentifiedList` has been added to `BusinessEvent`, to represent a single execution with multiple trades as a package.
- A new `packageReference` attribute, also of type `IdentifiedList` has been added to `ExecutionDetails` (as contained in a standalone `Trade`), to provide a reference to the package when that trade was executed as part of a package.

The latter attribute is handled as a reference to preserve referential integrity. The package object, represented as a simple list of trade identifiers, should exists independently of the underlying trades and those trades simply make reference to it.

Several FpML samples representing package transactions (either the packages themselves, or single underlying trades) have been used to test the model and synonym mappings updated accordingly. Those samples are:

- `pkg-ex02-swap-spread-single-trade-execution-notification`
- `pkg-ex55-execution-notification`
- `pkg-ex60-request-clearing`
- `pkg-ex61-clearing-confirmed`

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the above mentioned data types.
Select the Ingestion Viewer and review the above samples.
