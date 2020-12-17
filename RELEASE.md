# *CDM Model: Primitive Harmonisation Phase 2 and part of Phase 3*
 
_What is being released_ 

Phase 2 and parts of Phase 3 work to harmonise Primitive events to make use of TradeState data type to represent before and after attributes.
 
*Background*

Harmonisation will allow for easier combination of Primitives to form complex business events. Migrating before and after states to use the same data type eliminates the need for data translations when combining Primitives.
 
Whilst data translation can be addressed in the Function Model, defining the applicable data types correctly in the Data Model removes the need for additional complexity.
 
*Model Changes*

The following primitives and their corresponding business events have been updated:
 
* `SplitPrimitive`
* `ExercisePrimitive` (deleted, replaced with quantity change and contract formation/transfer)
* `ResetPrimitive` (deleted, replaced with the ‘Reset’ data type
* `ObservationPrimitive` (deleted, observation details now captured in newly created `Reset` data type)
 
The corresponding business events for this set of primitives are Allocation, Exercise, and Reset. 
 
The key changes made in relation to each of the primitive events are listed below: 
 
* `SplitPrimitive` has been harmonised to use `TradeState`. However, the `SplitOutcome` data type has been deleted because, as a result of this change, it is no longer referenced by any other types or functions.  
* `ExercisePrimitive` was deleted and replaced with a combination of `QuantityChangePrimitive`, `ContractFormationPrimitive`, and `TransferPrimitive`. In addition, the following data types have been deleted, as they were all referenced solely by `ExercisePrimitive`:  `ExerciseOutcome`, `PhysicalExercise`, and `CashExercise`.
* `Reset` is a new data type replacing the ResetPrimitive. The Observation-Reset proposal from April 2020 was used as the baseline design for creating this new type and for  updating the Reset process.  The mechanism of recording reset values utilises the newly introduced `Reset` data type. Values that are observed in the market in order to fulfil contractual product obligations for resets and other events are now represented in the `Observation` data type, which is referenced by the `Reset` data type, removing the need for the `ObservationPrimitive`  data type, which has been deleted. `Observation` is a root type, meaning data instances can exist independently from others and are decoupled from the event model.  
 
_Review Directions_

In the CDM Portal, use the Textual Browser or Graphical Navigator to review the types referenced in this note. 
Use the Function icon to see the changes to the reset process in the context of an Equity Swap with single underlier.

# *CDM and Rosetta Documentation*
 
 _What is being released_
 
 Improvements to the documentation of Rosetta and the CDM distribution including
 
 * Renaming Contract to Trade in documentation to match the previous change in CDM
 * Better explanation of functions and expressions in Rosetta including the precise semantics of various expression operators
 * Better documentation of how to create mappings
 * Details of how to obtain artifacts required to use the CDM Java distribution
 
 _Review Directions_
 
 On the rosetta docs site https://docs.rosetta-technology.io/ there are new pages for expressions and mapping in the DSL section as well as changes to the existing pages.
 
