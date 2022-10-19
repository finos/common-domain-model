# _Event Model - Representation of Credit Events and Corporate Actions_


_Background_


_Background_

The representation of credit events and corporate actions and the corresponding functional elements have been introduced in the model. Both types of events are represented as components of an `ObservationEvent`. The later can be recorded in the new `observationHistory` of a `TradeState` by using the new primitive instruction `ObservationInstruction` .

The representation of credit events and corporate actions does not include information about the impact of the event on a particular trade. The focus of this change is  only to document the generic information published by the Determinations Committee on the occurence of a credit event or the basic information describing the ocurrence of a corporate action events.

_What is being released?_

- Model coverage for the determination of credit events.
- Model coverage for the ocurrence of corporate actions.
- Qualification functions for the determination of a credit event and corporate actions.
- Creation functions for credit events and corporate actions.

_Data types_

- Added new `ObservationInstruction` type.
- Added new `ObservationEvent` type.
- Added new `CreditEvent` type.
- Added new `CorporateAction` type.
- `observationHistory` attribute of type `ObservableEvent` added to `TradeState`.
- `observation` attribute of type `ObservationInstruction` added to `PrimitiveInstruction`.
- Type of `excludedReferenceEntity` in `IndexReference information` changed to `ReferenceInformation`.

_Enumerations_

- Added new `CreditEventTypeEnum` enumeration.
- Updated `EventIntentEnum` to support credit events.
- Updated `CorporateActionTypeEnum` with more values and documentation.
- Updated `FeeTypeEnum` enumeration to support credit events and corporate actions.


_Functions_

- Added new `Create_Observation` function.
- Updated `Create_TradeState`function to support `observation`.
- Updated `Create_PrimitiveInstruction` function to support `observation`.
- Updated function `Create_StockSplit` to work with the introduced changes.

_Qualification_

- Added new `Qualify_CreditEventDetermined` function.
- Added new `Qualify_CorporateActionDetermined` function.

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above. In the CDM Portal, select the Visualisation tab and select Credit Event from the menu on the left. In the CDM Portal, select the Functions tab and select the function `Create_BusinessEvent`.
Upload one of the files:

../credit_event_examples/crev_example_1/updated_crev_ex1_business_event_creation_input.json
../credit_event_examples/crev_example_2/updated_crev_ex2_business_event_creation_input.json

../credit_event_examples/coac_example_1/updated_coac_ex1_business_event_creation_input.json
../credit_event_examples/coac_example_2/updated_coac_ex2_business_event_creation_input.json

# *Base Model - Period Enum*

_Background_

A review of Commodity products linked to the electricity market highlighted the need to represent a quantity specified on a per-hour basis. The quantity frequency attribute previously used a period enumeration whose smallest unit was day, not hour.

_What is being released?_

This release adds an `H` enumerated value to `PeriodExtendedEnum` and some corresponding mappings (although this does not affect any FpML sample in the current test pack).

In addition, a further review of time / period enumeration has revealed a number of overlapping components, which should be harmonised in future work (although not part of this release):

- `PeriodEnum`
- `PeriodTimeEnum`
- `TimeUnitEnum`

_Review Directions_

In the CDM Portal, review the enumerations mentioned above.
