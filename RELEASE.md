# *Product Model - Package Price and Spread*

_Background_

Coverage for package price was introduced in FpML 5.13. This release allows for the mapping of package prices and spreads, and refactors `BusinessEvent` and `EventInstruction` to improve consistency.

_What is being released?_

This release adjusts the model and adds FpML mapping coverage for package prices and spreads.

- Mappings added to populate CDM attribute `ExecutionDetails->packageReference->price` from FpML element `quote`.
- `BusinessEvent` updated to extend `EventInstruction`, with all attributes (except `eventQualifier` and `after`) moved to `EventInstruction`.
 
_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

In the CDM Portal, select Ingestion and review the following samples:

- fpml-5-10 > processes > msg-package-price
- fpml-5-10 > processes > msg-package-spread
