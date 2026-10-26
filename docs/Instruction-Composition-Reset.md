---
title: Reset Instruction Composition
---

Build a CDM Reset instruction using an 8-step typed `CompositionStep` model.

## Overview

The Reset Instruction Composition workflow builds a standard CDM `Reset` instruction for an interest-rate payout across an eight-step execution model. It separates identification, date determination, business-day adjustment, observation, recording market data, and reset-value calculation into typed composition steps.

The final `Reset` instruction (`InstructionCompositionOutput`) is supplied to the CDM Reset `BusinessEvent` workflow. This document describes the instruction-composition path and does not replace standard event-creation functions.

For the generic pattern, see [Instruction Composition Model](Instruction-Composition.md).

## Scope

The workflow begins after an **unadjusted Reset Date** has been provided by the calling process (determining when a Reset Date should be initiated lies outside this boundary).

The implementation supports three observation shapes:
- **Single observation**: One observed market rate becomes the Reset value.
- **Multiple observations**: A sequence of observations is used with supported calculation logic to determine the Reset value.
- **Compounded index**: Start and end index observations are used to derive a compounded index rate for the Reset value.

The workflow relies on external reference data for Floating Rate Option (FRO), holidays/non-business dates, and published market rates.

## External Reference Data

Three `[codeImplementation]` placeholder functions supply inputs that CDM does not retrieve natively:

| Placeholder | Boundary | Inputs | Result |
| :--- | :--- | :--- | :--- |
| `ExtractFloatingRateOptionData` | Between steps 4 and 5 | Floating-rate index, trade date | `FloatingRateIndexDefinition` |
| `GetNonBusinessDates` | Between steps 2/3, 3/4, and 5/6 | Business centers, period/dates | Non-business dates list |
| `GetMarketRates` | Between steps 6 and 7 | Floating-rate index, adjusted observation dates | Date/value market observations |

External integration calls occur **between**, not inside, composition steps. The caller invokes a placeholder at the boundary and passes the typed result to the subsequent instruction builder. This separation maintains model determinism while permitting flexible integration with enterprise calendar, reference-data, and market-data services.

## Reset Composition Steps & Builder Mapping

Every Reset step follows a standard pattern:
1. Inputs are sent to `Create_<Step>Instruction` function to create the Instruction for the step (`CompositionStepInstruction`).
2. Create the Instruction Composition object for the step-instruction sending the output of the step 1 above to the function `Create_InstructionComposition`.
3. The `CompositionState` of the `InstructionComposition` created gets updated with the new instruction.

Only the step builders are process specific. The eight Create_<Step>Instruction functions take the step's inputs and do the Reset specific work, each returning a typed instruction result. Everything else in the pattern (`CompositionStepInstruction`, `Create_InstructionComposition` and `CompositionState`) is generic. Any multi-step process can reuse it unchanged.

The eight step builders that perform the process-specific work for this Reset Instruction Composition are given below with the typed instruction results they return:
| Step | Instruction Builder | Typed Result (`CompositionStepInstruction`) | Result Retained in `CompositionState` |
| :---: | :--- | :--- | :--- |
| **1** | `Create_CollectFloatingRateOptionInstruction` | `CollectFloatingRateOptionInstruction` | Extracts floating-rate index and trade date from `InterestRatePayout` for external FRO lookup. |
| **2** | `Create_DetermineUnadjustedCalculationPeriodInstruction` | `DetermineUnadjustedCalculationPeriodInstruction` | Receives `CalculationPeriodDates` and the unadjusted reset date; isolates unadjusted calculation period and reset date. |
| **3** | `Create_AdjustPeriodInstruction` | `AdjustPeriodInstruction` | Applies calculation-period business-day conventions using `GetNonBusinessDates`. |
| **4** | `Create_AdjustDateInstruction` | `AdjustDateInstruction` | Independently applies reset-date business-day conventions using `GetNonBusinessDates`. |
| **5** | `Create_DetermineUnadjustedObservationDatesInstruction` | `DetermineUnadjustedObservationDatesInstruction` | Derives observation dates or index endpoints based on observation shape. |
| **6** | `Create_AdjustObservationDatesInstruction` | `AdjustObservationDatesInstruction` | Applies fixing-day offsets, lookback shifts, and business-day adjustments to produce rate-retrieval dates. |
| **7** | `Create_CalculateResetValueInstruction` | `CalculateResetValueInstruction` | Calculates and rounds the reset value based on observation type. |
| **8** | `Create_ResetInstruction` | `ResetInstruction` | Generates the canonical `Reset` instruction. |

### Step Builder Independence
The `Create_[...]Instruction` functions execute domain specific logic without mutating state objects directly. They return a typed `CompositionStepInstruction` capturing the result of one operation to be applied in each step.

### Generic Orchestration
After every step builder call, append the resulting typed instruction using `Create_CompositionStepFromInstruction`.

The orchestration function:
- Receives prior `CompositionState` and `CompositionStep` collections (0 / empty for Step 1),
- Receives stable composition identifiers (compositionIdentifier, tradeId, compositionType),
- Appends a new `CompositionStep` containing the step instruction,
- Updates cumulative `CompositionState` with the step result, and
- Populates `InstructionCompositionOutput` upon reaching the terminal `Reset` step.

## Detailed Step Implementation

### Step 1 — Collect the Floating Rate Option Name
- **Source Path**: `InterestRatePayout -> rateSpecification -> FloatingRateSpecification -> rateOption -> FloatingRateIndex -> floatingRateIndex`
- **Inputs**: `InterestRatePayout` (originating context), `tradeDate` (`TradeState -> trade -> tradeDate`)
- **Output**: `CollectFloatingRateOptionInstruction`
- **Details**: Preserves `tradeDate` to resolve against the operative version of the ISDA Floating Rate Option (FRO) Matrix at the time of execution. Extracts the index identifier for downstream metadata lookups.

### Step 2 — Determine the Unadjusted Calculation Period
- **Inputs**: `InterestRatePayout -> calculationPeriodDates`, unadjusted reset date provided by calling workflow.
- **Output**: `DetermineUnadjustedCalculationPeriodInstruction`
- **Details**: Isolates the specific unadjusted calculation period matching the unadjusted reset date to set up boundary context prior to date adjustments.

### Step 3 — Adjust the Calculation Period
- **Inputs**: Unadjusted calculation period (Step 2), non-business dates (`GetNonBusinessDates`), `InterestRatePayout -> calculationPeriodDates -> calculationPeriodDatesAdjustments -> businessDayConvention`.
- **Output**: `AdjustPeriodInstruction`
- **Details**: Applies business-day conventions (`Following`, `Preceding`, `Modified Following`). *Modified Preceding* and *Nearest* are explicitly out of scope.

### Step 4 — Adjust the Reset Date
- **Inputs**: Unadjusted reset date (Step 2), non-business dates (`GetNonBusinessDates`), `InterestRatePayout -> resetDates -> resetDatesAdjustments -> businessDayConvention`.
- **Output**: `AdjustDateInstruction`
- **Details**: Independently adjusts the reset date. Keeping Step 3 and Step 4 distinct ensures that differing calendars between calculation periods (e.g., London/New York) and fixing dates (e.g., New York only) do not leak conventions across boundaries.

### Step 5 — Determine Unadjusted Observation Dates
- **Inputs**: Adjusted calculation period (Step 3), adjusted reset date (Step 4), FRO category/style (Step 1), calculation method.
- **Output**: `DetermineUnadjustedObservationDatesInstruction`
- **Details**: Generates date endpoints according to observation shape (Single: adjusted reset date; Multiple: calendar dates across adjusted calculation period; Compounded Index: start and end dates). Trade confirmation specifications take precedence over FRO defaults.

### Step 6 — Adjust Observation Dates
- **Inputs**: Unadjusted observation dates (Step 5), fixing-date offset, lookback parameters, FRO metadata, non-business dates.
- **Output**: `AdjustObservationDatesInstruction`
- **Details**:
  - **Single observation**: Applies fixing offset.
  - **Compounded index**: Returns start/end observation dates.
  - **Multiple observations (weighting & gap filling)**: Returns one observation date per calendar day in the period. Non-business days repeat the preceding business day's fixing (`ExpandMissingDates`, e.g., `[Thu, Fri, Fri, Fri, Mon]`), avoiding explicit numeric weight arrays. Step 7 applies a 1-day weight per item.

### Step 7 — Calculate the Reset Value
- **Inputs**: Market observations (`GetMarketRates`), adjusted calculation period (Step 3), FRO rounding and currency (Step 1), day-count fraction, FRO metadata.
- **Output**: `CalculateResetValueInstruction`
- **Details**:
  - **Single observation**: Observed rate becomes the reset value directly (no rounding/calculation applied).
  - **Compounded index**: Requires exactly 2 observations; applies Compounded Index formula.
  - **Multiple observations**: Requires  observations; applies Compounding Formula using calendar weights.
  - **Rounding**: Default fallback for calculated rates is 7 decimal places (0.00001% or 0.0000001). Explicit contract rounding takes precedence.

### Step 8 — Record the Reset Instruction Output
- **Inputs**: Reset value and currency (Step 7), adjusted reset date (Step 4), `Observable` (`TradeLot -> priceQuantity -> observable`).
- **Output**: `InstructionCompositionOutput` (`Reset` instruction).
- **Details**: Assembles calculated values and identifiers into the final canonical CDM Reset instruction.

## Implementation Traceability

| Workstream Stage | CDM Issue | Contributed PR |
| :--- | :---: | :---: |
| Framework & Base Models | #4593 | #4834 |
| Step 1: Collect FRO Name | #4168 | #4858 |
| Step 2: Unadjusted Calc Period | #4379 | #4921 |
| Step 3: Adjust Period | #4389 | #4993 |
| Step 4: Adjust Reset Date | #4388 | #4994 |
| Step 5: Unadjusted Observations | #4413 | #5069 |
| Step 6: Adjust Observation Dates | #4414 | #5073 |
| Step 7: Calculate Reset Value | #4482 | #5128 |
| Step 8: Output Instruction | #4483 | #5262 |
| State Machine: Reset Instruction Next Step | #5172 | #5290 |

---

Click [here](Instruction-Composition.md) to return to the generic Instruction Composition Model page.
