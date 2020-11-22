# *CDM Model: Primitive Harmonisation Phase 1*

_What is being released_

Phase 1 of work to harmonise Primitive events such that all Primitives use the `TradeState` data type to represent it's before and after state.

Harmonisation will allow for easier combination of Primitves to form  complex business events. Migraqting before and after states to use the same data type means no need for data translations when combining Primitives. 

Whilst data translation can be addressed in the Function Model, defining the correct data types correctly, upfront in the Data Model removes the need for additional complexity. 

*Model Changes*

The key changes in this release relate to the creation of the `TradeState` data type. `TradeState` includes all necessary data definitions to: 

1. Replace trade representations `Execution` and `Contract`.
1. Replace state representations `ContractState`, `PostContractFormationState`.

The following Primitives have been harmonised as a result of creating the `TradeState` data type:

- Contract Formation
- Execution
- Quantity Change
- Terms Change
- Reset
- Split (before state only)

*Special Note*
 
`Contract` represented the root of the Product Model and was referenced extensively in CDM and its associated tooling. All explicit references to `Contract` have been replaced by `TradeState`. This involved updating the following model and tooling elements:

- Ingestion now produces a `TradeState` data instance.
- Function Model references to `Execution`, `Contract`, `ContractState` or `PostContractFormationState` were migrated to use to `TradeState` instead.
- Regulatory reporting makes many references to `Contract`, all of which have been updated.
- Updated Handwritten java examples of event sequences and visualisations to account for the migration.

_Review Directions_

- In the CDM Portal, use the Textual Browser or Graphical Navifator to review the type `TradeState`.