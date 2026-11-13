---
title: Instruction Composition Model
---

Decomposes CDM types or instructions into an auditable sequence of **Instruction Composition** steps.

## Overview

The CDM Instruction Composition model is a pattern for expressing an operational process as a sequence of typed, deterministic instructions. Each step or discrete instruction is built by a function that contains the output of one business operation. Each instruction is then appended to an `InstructionComposition` object, preserved as history, and made available to subsequent steps, ultimately producing a CDM object.

The pattern adds an execution and orchestration layer around CDM's product and event model. Instruction Composition allows implementers to derive a CDM Instruction reproducibly, with the outcome depending on ordered calculations, prior results, and externally supplied data. A completed process performed by the Instruction Composition produces an Instruction (or other CDM type) that can serve as an input to the existing CDM Workflow model for lifecycle processes, such as the creation of a `BusinessEvent`.

The Reset implementation is the first concrete use of this pattern. See [Reset Instruction Composition](Instruction-Composition-Reset.md).

## When to Use Instruction Composition

Use Instruction Composition when a process:
- Consists of several deterministic operations,
- Requires intermediate values to be retained[^1] and/or reused,
- Has dependencies on external reference data, calendars, or market observations that can be isolated,
- Benefits from retrying an individual failed operation rather than restarting the entire process, or
- Produces a final CDM instruction that will subsequently be used in a CDM `BusinessEvent` within the Workflow model.

An Instruction Composition in a workflow should **not** replace a simple, self-contained CDM function. It is intended for processes where a single function would obscure important intermediate state, introduce excessive branching, or make the calculation difficult to audit.

[^1]: For example, to persist and audit the values which were used in a calculation or determination.

## Instruction Composition Model

The model is a composed process represented by an `InstructionComposition` containing the following components:

| Component | Purpose |
| :--- | :--- |
| `InstructionComposition` | The aggregate execution state and container for a process composed of smaller steps (e.g., the process to create a Reset Instruction). |
| `CompositionStep` | An execution step representing a single deterministic operation, including its step identifier, timestamp, and typed instruction. |
| `CompositionState` | The persistent, cumulative record that maintains state across sequential steps and acts as structured input/history for subsequent steps of the process. |
| `CompositionStepInstruction` | An object defining the specific operational input/output produced by a single `CompositionStep`. |
| `InstructionCompositionOutput` | The lifecycle event or instruction populated upon the completion of the terminal `CompositionStep`. |

### Design Principles

1. **One operation per step**: A step must have a narrow, named responsibility: collect an identifier, derive a period, adjust a date, record observations, or calculate a value.
2. **Typed, cumulative state**: Intermediate execution results are retained in typed CDM structured objects (`CompositionState`) rather than held in volatile application memory. Subsequent steps consume their inputs directly from this accumulated history, external references, or the originating workflow context.
3. **Explicit orchestration**: The caller controls the sequence of steps: build the step instruction, record it in the composition state, retrieve any required external data, and advance. This explicit separation of calculation from state transition ensures that a failed or rejected step can be retried without recalculating prior valid steps.
4. **External data at explicit boundaries**: The model defines strict input and output data contracts for external dependencies but does not prescribe an implementation for external data providers (e.g., calendar, market-data, or reference-data providers).
5. **CDM workflow integration**: The terminal step yields an `InstructionCompositionOutput`. For use cases where instructions for a CDM `BusinessEvent` are created, any standard `BusinessEvent` function can immediately consume this output within downstream proposed event/event-creation workflows.

## Generic Orchestration Functions

- `Create_InstructionComposition`: The primary appending function that updates history, retains correlation/sequence identifiers, and exposes the final output upon completion.
- `AddStepToCompositionState`: The central router/dispatcher function that receives a new step and delegates state updates to process-specific history functions. Used alongside `Create_InstructionComposition`.
- `NextCompositionStepToExecute`: The high-level flow controller that inspects current composition state to decide what step must run next.

## Creating a New Instruction Composition Workflow

Each step follows a strict lifecycle:
1. Obtain inputs from the originating workflow or context, prior `CompositionState`, or an external data boundary.
2. Call a typed `Create_[...]Instruction` builder function to return a `CompositionStepInstruction` (e.g., `Create_ResetInstruction`).
3. Process the step proposal using the generic composition orchestration functions. [generic composition orchestration functions](#generic-orchestration-functions).
4. Update `CompositionState` and store the returned history for subsequent steps.
5. Upon reaching the terminal step, populate `InstructionCompositionOutput` with the final `BusinessEvent` instruction (or other target type).

<img width="1430" height="623" alt="InstructionComposition" src="https://github.com/user-attachments/assets/5a671ad3-e6cc-4b7c-b76f-9a524d131ccf" />

The composition process operates across two distinct layers:
- **Step-instruction generation**: Performs the business operation and produces a typed instruction (e.g., deriving an unadjusted calculation period or applying a business-day convention).
- **Instruction Composition step construction**: Records the new instruction in the composed process. It appends the instruction as a `CompositionStep`, updates `CompositionState`, retains common identifiers, and exposes the final output when the terminal step completes.

Keeping these layers separate is important. The instruction generator remains small, deterministic, and directly testable. The Instruction Composition builder remains reusable across composed workflows and is responsible only for sequencing and state retention.

These are the natural steps to follow when creating a new Instruction Composition use case:

### Lifecycle Steps

#### 1. Define boundary of process you are modelling with Instruction Composition Model
Identify:
- The originating workflow or context,
- The standard BusinessEvent instruction or another object to be produced,
- The appropriate `InstructionCompositionTypeEnum` value (or label) for the process the instructionComposition is being used for,
- The correlation identifiers (compositionIdentifier, tradeId) that must remain consistent across steps, and
- The terminal condition completing process.

Start with information fully natively available within CDM. Do not embed provider-specific data retrieval inside calculation/process logic.

#### 2. Decompose into Instructions
Define a narrow instruction type and builder function (`Create_[...]Instruction`) for each distinct business operation. Each instruction builder should:
- Expose explicit input assumptions via typed parameters,
- Contain only data required for auditability or downstream steps,
- Use descriptive process naming, and
- Avoid combining unrelated calculations.

#### 3. Manage State Composition
Pass 0 prior state/steps for the initial operation. For each subsequent step, supply the returned `CompositionState` and steps from the preceding Instruction Composition object. Maintain stable compositionIdentifier, tradeId, and compositionType values throughout the sequence.

#### 4. Model external dependencies as placeholders
If CDM is the source of truth, to keep track of the general process, use CDM functions tagged with [codeImplementation] when logic relies on runtime context that CDM cannot supply (e.g., market observations, business-day calendars, or external workflow triggers).

#### 5. Produce the final CDM object
The terminal builder gathers accumulated state parameters to yield the final BusinessEvent instruction or other object output by the process via `InstructionCompositionOutput`.

## Implementation Guidance

- **Keep the composition layer generic**: Domain calculation rules belong in typed instructions, not inside the composition runner.
- **Preserve provenance and history**: Retain full execution metadata (dates, conventions, rounding) for end-to-end auditability.
- **Keep external data explicit**: Abstract external service lookups via typed placeholders.
- **Isolate runtime concerns**: Network calls, retries, and persistence reside strictly outside the core model.
- **Enable step-level failure recovery**: Cumulative history allows retrying failed steps without restarting from step 1.
- **Keep calculation paths bounded**: Functions should support only the explicitly implemented methodologies covered by tests and expectations. Unsupported methods must fail cleanly rather than falling through to an unverified calculation.
- **Retain intermediate temporal state**: Operational milestones (unadjusted vs. adjusted calculation periods, reset dates, and observation dates) represent distinct calculation stages. Isolating each into its own instruction makes the complete calculation lineage fully auditable, enables precise troubleshooting of intermediate values, and allows downstream operations to consume the exact state required.
- **Preserve the terminal-output boundary**: Assemble the final output object only after all prerequisite steps have completed.

---

*See also: [Reset Instruction Composition](Instruction-Composition-Reset.md)*
