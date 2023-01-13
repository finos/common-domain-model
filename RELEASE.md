# *Product Model - Package Price and Spread*

_What is being released?_

This release adjusts the model and adds FpML mapping coverage for Package prices and spreads.

- Mappings added to populate CDM attribute `ExecutionDetails->packageReference->price` from FpML element `quote`.
- `BusinessEvent` updated to extend `EventInstruction`, with all attributes (except `eventQualifier` and `after`) moved to `EventInstruction`. 

_Review Directions_

In Rosetta, open the Translate tab, and review examples:

- fpml-5-10 > processes > msg-package-price
- fpml-5-10 > processes > msg-package-spread
