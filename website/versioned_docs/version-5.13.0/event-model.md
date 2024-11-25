---
title: Event Model
---

**The CDM event model provides data structures to represent the
lifecycle events of financial transactions**. A lifecycle event occurs
when a transaction goes through a *state transition* initiated either by
one or both trading parties, by contractual terms, or by external
factors. For example, the execution of a trade is the initial event
which results in the state of an executed trade. Subsequently, one party
might initiate an allocation, both parties might initiate an amendment
to a contractual agreement, or a default by an underlying entity on a
Credit Default Swap would trigger a settlement according to defined
protection terms.

Examples of lifecycle events supported by the CDM Event Model include
the following:

-   Trade execution and confirmation
-   Clearing
-   Allocation
-   Settlement (including any future contingent cashflow payment)
-   Exercise of options

The representation of lifecycle events in the CDM is based on the
following design principles:

-   **A lifecycle event describes a state transition**. There must be
    different before/after trade states based on that lifecycle event.
-   **State transitions are functional and composable**. The CDM
    specifies the entire functional logic to transition from one state
    to another. The state transition logic of all in-scope events is
    obtained by composition from a small set of functional building
    blocks.
-   **The history of the trade state can be reconstructed** at any point
    in the trade lifecycle. The CDM implements a *lineage* between
    states as the trade goes through state transitions.
-   **The product underlying the transaction remains immutable**.
    Automated events, for instance resets or cashflow payments, do not
    alter the product definition. Lifecycle events negotiated between
    the parties that give rise to a change in the trade economics
    generate a new instance of the product or trade as part of that
    specific event.
-   **The state is trade-specific**, not product-specific (i.e. the CDM
    is not an asset-servicing model). The same product may be associated
    to infinitely many trades, each with its own specific state, between
    any two parties.

To represent a state transition, the event model is organised around
four main components:

-   **Trade state** represents the state in the lifecycle that the trade
    is in, from execution to settlement and maturity.
-   **Primitive operator** is the functional building block that is used
    to compose business events. Each operator describes a fundamental
    change to the state of a trade going from a before state to an after
    state and is parameterised by a primitive instruction input.
-   **Business event** represents a lifecycle event affecting one or
    more trades as a composite of primitive instructions. A business
    event can comprise several instructions, each consisting of a set of
    primitive instructions applied to a single trade state (before). The
    resulting trade state (after) can be multiple.
-   **Workflow** represents a set of actions or steps that are required
    to trigger a business event.

The below diagram illustrates the relationship between these components.
Each of them is described in the next four sections.

![](/img/event-model-overview.png)

# Trade State

A trade state is defined in CDM by the `TradeState` data type and
represents the state of a trade at each stage in its lifecycle. With
each trade creation or modification event, a new `TradeState` instance
is created. Chaining together the sequence of `TradeState` instances
then recreates the path each trade took within its lifecycle.

`TradeState` is a foundational data type in the CDM Event Model as it
represents the input and output of any state transition. Therefore, all
trade-related information that can change throughout the trade lifecycle
are representing within `TradeState`.

``` Haskell
type TradeState:
  [metadata key]
  [rootType]
  trade Trade (1..1)
  state State (0..1)
  resetHistory Reset (0..*)
  transferHistory TransferState (0..*)
```

While many different types of events may occur through the trade
lifecycle, the `trade`, `state`, `resetHistory` and `transferHistory`
attributes are deemed sufficient to describe all of the possible
(post-trade) states which may result from lifecycle events. The `Trade`
data type contains the tradable product, which defines all of the
economic terms of the transaction as agreed between the parties.

The `Trade`, `State`, `Reset` and `Transfer` data types that are
utilised within `TradeState` are all detailed in the sections below.

## Trade

The `Trade` data type defines the outcome of a financial transaction
between parties, where the terms are primarily reflected in the tradable
product. Additionally, `Trade` includes attributes such as the trade
date, transacting parties, and settlement terms. Some attributes, such
as the parties, may already be defined in a workflow step or business
event and can simply be referenced in `Trade`.

``` Haskell
type Trade:
  [metadata key]
  tradeIdentifier TradeIdentifier (1..*)
  tradeDate date (1..1)
    [metadata id]
  tradeTime TimeZone (0..1)
    [metadata id]
  tradableProduct TradableProduct (1..1)
  party Party (0..*)
  partyRole PartyRole (0..*)
  executionDetails ExecutionDetails (0..1)
  contractDetails ContractDetails (0..1)
  clearedDate date (0..1)
    [deprecated]
  collateral Collateral (0..1)
  account Account (0..*)
    [deprecated]
```

---
**Note:**
Attributes within `Trade` and `ContractDetails` incorporate elements
from FpML's *trade confirmation* view, whereas the `TradableProduct`
data type corresponds to FpML's *pre-trade* view. The `TradableProduct`
data type is further detailed in the
[`tradable-product`](/docs/product-model#TraableProduct) section of the
documentation.

---

Additionally, `Trade` supports the representation of specific execution
or contractual details via the `executionDetails` and `contractDetails`
attributes.

### ExecutionDetails

The `ExecutionDetails` data type represents details applicable to trade
executions and includes attributes that describe the execution venue and
execution type. Not all trades will have been 'executed', such as
those created from a Swaption Exercise event. In those cases, the
`executionDetails` attributes on `Trade` is expected to be empty.

``` Haskell
type ExecutionDetails:
  [metadata key]
  executionType ExecutionTypeEnum (1..1)
  executionVenue LegalEntity (0..1)
  packageReference IdentifiedList (0..1)

  condition ExecutionVenue:
    if executionType = ExecutionTypeEnum -> Electronic
    then executionVenue exists
```

### ContractDetails

`ContractDetails` are only applicable to trades on contractual products
and are typically provided at or prior to trade confirmation.

``` Haskell
type ContractDetails:
  [metadata key]
  documentation LegalAgreement (0..*)
  governingLaw GoverningLawEnum (0..1)
    [metadata scheme]
```

## State

The `State` data type defines the state of a trade at a point in the
Trade's life cycle. Trades have many state dimensions, all of which are
represented here. For example, states useful for position keeping are
represented alongside those needed for regulatory reporting.

``` Haskell
type State:
  closedState ClosedState (0..1)
  positionState PositionStatusEnum (0..1)
```

When a trade is closed, it is necessary to record that closure as part
of the trade state.

For instance in a full novation scenario, the initial state is a single
`TradeState` and the resulting state is two `TradeState`. The first
resulting `TradeState` represents a new contract, which is the same as
the original but where one of the parties has been changed, and the
second resulting `TradeState` is the original contract, now marked as
*closed*.

The `ClosedState` data type (enclosed within `State`) captures this
closed state and defines the reason for closure.

``` Haskell
enum ClosedStateEnum:
  Allocated
  Cancelled
  Exercised
  Expired
  Matured
  Novated
  Terminated
```

## Reset

In many cases, a trade relies on the value of an observable which will
become known in the future: for instance, a floating rate observation at
the beginning of each period in the case of a Interest Rate Swap, or the
equity price at the end of each period in an Equity Swap. This *reset*
information is captured by the `Reset` data type and associated to the
trade state.

While the reset information is trade-specific, the observation itself is
provided by the relevant market data provider independently of any
specific trade. Such observation is captured by the `Observation` data
type.

Both the `observedValue` (in `Observation`) and the `resetValue` (in
`Reset`) attributes are specified as a `Price` type. In the trade, the
resettable value must be associated to a variable price attribute. It
typically represents a number that is directly used to compute transfer
amounts like cashflows.

In addition to the observation value, a reset specifies the date from
which the resettable value becomes applicable in the trade's context,
which could be different from the observation date if some observation
lag applies. Depending on the trade's economic properties, a reset may
also depend on several observation values based on some aggregation
method - e.g. a compounded interest rate based on daily fixings.

``` Haskell
type Reset:
  [metadata key]
  resetValue Price (1..1)
  resetDate date (1..1)
  rateRecordDate date (0..1)
  observations Observation (1..*)
    [metadata reference]
  averagingMethodology AveragingCalculation (0..1)
```

``` Haskell
type Observation:
  [rootType]
  [metadata key]
  observedValue Price (1..1)
  observationIdentifier ObservationIdentifier (1..1)
```

## Transfer

A transfer is a multi-purpose object that represents the transfer of any
asset, including cash, from one party to another. The `Transfer` object
is associated to an enumeration to qualify the status that the transfer
is in, from instruction to settlement or rejection.

``` Haskell
type TransferState:
  [metadata key]
  [rootType]
  transfer Transfer (1..1)
  transferStatus TransferStatusEnum (0..1)
```

``` Haskell
type Transfer extends TransferBase:
  settlementOrigin SettlementOrigin (0..1)
  resetOrigin Reset (0..1)
  transferExpression TransferExpression (1..1)
```

``` Haskell
type TransferBase:
  identifier Identifier (0..*)
    [metadata scheme]
  deliverableQuantity NonNegativeQuantity (1..1)
  notionalQuantity NonNegativeQuantity (0..1) <"Optionally represents the notional value or nominal amount of the asset to be transferred.">
  price Price (0..1) <"Optionally represents the price at which the asset is transferred.">
  observable Observable (0..1)
  payerReceiver PartyReferencePayerReceiver (1..1)
  settlementDate AdjustableOrAdjustedOrRelativeDate (1..1)
```

## Primitive Operator {#primitive-event}

**Primitive operators are functional building blocks used to compose
business events**. Each primitive operator describes a fundamental state
transition that applies to a trade.

There are nine fundamental operations on trade state. Other than split
and execution, they each impact separate attributes of a trade state and
are therefore independent of each other.

1.  execution: instantiates a new trade.
2.  quantity change: changes the quantity (and/or price) of a trade
3.  terms change: changes the terms of the product of a trade
4.  party change: changes a party on a trade
5.  exercise: exercises an option embedded in a trade
6.  contract formation: associates a legal agreement to a trade
7.  reset: changes a trade's resettable value based on an observation
8.  transfer: transfers some asset (cash, security, commodity) from one
    party to another
9.  split: splits a trade into multiple identical trades

## Primitive Function

A primitive operator is represented by a primitive function that takes a
before trade state as input and returns an after trade state as output,
both of type `TradeState`. The only exceptions to this rule are:

-   execution, for which there is no before state since its purpose is
    to instantiate a new trade, and
-   split, which results in multiple trade states as copies of the
    original trade.

All primitive functions are prefixed by `Create_` followed by the name
of the primitive operator. The business logic of primitive functions is
fully implemented. An example of primitive function, for the
`PartyChange` primitive, is illustrated below.

``` Haskell
func Create_PartyChange:
  inputs:
    counterparty Counterparty (1..1)
    ancillaryParty AncillaryParty (0..1)
    partyRole PartyRole (0..1)
    tradeId TradeIdentifier (1..*)
    originalTrade TradeState (1..1)
  output:
    newTrade TradeState (1..1)
```

## Primitive Instruction

Primitive functions take additional inputs alongside the before trade
state to specify the parameters of the state transition. Each primitive
operator is associated to a primitive instruction data type that
contains the function's required parameters as attributes - illustrated
below using the same `PartyChange` example.

``` Haskell
type PartyChangeInstruction:
  counterparty Counterparty (1..1)
  ancillaryParty AncillaryParty (0..1)
  partyRole PartyRole (0..1)
  tradeId TradeIdentifier (1..*)
```

The `PrimitiveInstruction` data type allows to build composite primitive
instructions and therefore compose primitive operators. This data type
contains one instruction attribute for each of the possible nine
primitive instruction types - aligned onto the nine fundamental
primitive operators.

``` Haskell
type PrimitiveInstruction:
  contractFormation ContractFormationInstruction (0..1)
  execution ExecutionInstruction (0..1)
  exercise ExerciseInstruction (0..1)
  partyChange PartyChangeInstruction (0..1)
  quantityChange QuantityChangeInstruction (0..1)
  reset ResetInstruction (0..1)
  split SplitInstruction (0..1)
  termsChange TermsChangeInstruction (0..1)
  transfer TransferInstruction (0..1)
```

## Primitive Composition

The separation between the before trade state and primitive instructions
allows to compose primitive operators. Primitive operators can be
chained by applying a composite primitive instruction to a single trade
state, as represented in the diagram below.

![](/img/composing-primitive-operators.png)

---
**Note:**
When a primitive instruction is composite, interim trade states will be
created when executing each primitive operator. These interim trade
states may not correspond to any actual business outcome (only the final
after trade state does), so implementors will usually choose not to
persist them.

---

The `Create_TradeState` function performs such composition of primitive
operators. It takes a single trade state and a composite primitive
instruction as inputs and returns a single trade state. The before trade
state input is optional, in which case a new execution must be specified
in the instructions.

This function applies each of the primitive operators (other than split)
to the trade state in the order listed in the [primitive
operator](#primitive-operator) section. Apart from execution which, when
present, must always be applied first, the order does not affect the
outcome because each primitive operator impacts a different part of the
trade state.

``` Haskell
func Create_TradeState:
  inputs:
    primitiveInstruction PrimitiveInstruction (0..1)
    before TradeState (0..1)
  output:
    after TradeState (1..1)
```

## Special Case: Split

Split is a special case of primitive operator. It is used in many
lifecycle events that require a trade to be copied, such as in clearing
or allocation scenarios.

-   In itself, it does not change the state of a trade - it just creates
    identical copies
-   Contrary to other operators, it outputs multiple trade states
-   Order matters: when present, a split must be executed before other
    operators can be applied to its multiple output

Like other primitive operators, split is associated to a split function
and a split instruction. But unlike other operators, the split function
cannot be executed in the `Create_TradeState` function because it
returns a multiple output. Instead, a split instruction provides a
breakdown of primitive instructions to apply to each post-split trade
state.

For example, an allocation instruction would be specified as a split
breakdown, each with a quantity change instructions to divide the
initial block trade into smaller pieces, and then a party change
instruction to assign each piece to a different legal entity related to
the executing party.

The split function iterates on each element of the breakdown and applies
the corresponding primitive instruction to each copy of the original
trade using the `Create_TradeState` function. The size of that breakdown
directs the size of the split.

``` Haskell
type SplitInstruction:
  breakdown PrimitiveInstruction (1..*)
```

``` Haskell
func Create_Split:
  inputs:
    breakdown PrimitiveInstruction (1..*)
    originalTrade TradeState (1..1)
  output:
    splitTrade TradeState (1..*)

  add splitTrade:
    breakdown
      extract Create_TradeState(item, originalTrade)
```

Examples of how primitive operators work are illustrated below.

## Examples of Primitive Operators

### Execution Primitive

The first step in instantiating a transaction between two parties in the
CDM is an *execution*. In practice, this execution represents the
conclusion of a pre-trade process, which may be a client order that gets
filled or a quote that gets accepted by the client. However, the CDM
event model only covers post-trade lifecycle events so assumes that a
trade gets instantiated "from scratch" at execution.

Therefore, the execution function does not take any before state as
input and all the trade details are contained in the execution
instruction input.

``` Haskell
func Create_Execution:
  inputs:
    instruction ExecutionInstruction (1..1)
  output:
    execution TradeState (1..1)
```

``` Haskell
type ExecutionInstruction:
  product Product (1..1)
  priceQuantity PriceQuantity (1..*)
  counterparty  Counterparty (2..2)
  ancillaryParty AncillaryParty (0..*)
  parties Party (2..*)
  partyRoles PartyRole (0..*)
  executionDetails ExecutionDetails (1..1)
  tradeDate date (1..1)
      [metadata id]
  tradeTime TimeZone (0..1)
      [metadata id]
  tradeIdentifier TradeIdentifier (1..*)
  collateral Collateral (0..1)
```

### Contract Formation Primitive

Once an execution is confirmed, a legally binding contract is signed
between the two executing parties and a *contract formation* associates
a legal agreement to the transaction. The contract formation primitive
function represents the transition of the trade state to a legally
binding legal agreement after the trade is confirmed.

The function takes an existing trade state (typically, but not
necessarily, an execution) as input and returns a trade state output
containing the contract details. The `ContractDetails` object can
reference some higher-order legal documentation governing the
transaction - usually a *master agreement*. This legal documentation
information is provided in the contract formation instruction input.

``` Haskell
func Create_ContractFormation:
  inputs:
    instruction ContractFormationInstruction (1..1)
    execution TradeState (1..1)
  output:
    contractFormation TradeState (1..1)
```

``` Haskell
type ContractFormationInstruction:
  legalAgreement LegalAgreement (0..*)
```

Because a transaction may change through some lifecycle events before
getting confirmed, the contract formation primitive is separated from
the execution primitive so that it can be invoked appropriately
depending on the scenario. E.g. in an allocation, the trade would first
get split into sub-accounts as designated by one of the executing
parties, before a set of legally binding contracts is signed with each
of those sub-accounts. A contract formation may not even follow an
execution and could occur as part of later lifecycle events. E.g. in a
novation scenario, a new contract will need to be instantiated with the
step-in party and the right legal agreement associated to that trade.

In other cases, the execution and confirmation happen in one go and a
contract is instantiated immediately. Such contract instantiation
scenario can be represented using a compositive primitive instruction
that comprises both an execution and a contract formation instruction
and applies to a null trade state.

### Reset Primitive

The reset function associates a reset object to the trade state. The
reset function creates an instances of the `Reset` data type and adds it
to the `resetHistory` attribute of a given `TradeState`. The reset
instruction specifies the payout that is subject to the reset, via a
reference. A reset does not modify the underlying `Trade` object.

``` Haskell
func Create_Reset:
  inputs:
    instruction ResetInstruction (1..1)
    tradeState TradeState (1..1)
  output:
    reset TradeState (1..1)
```

``` Haskell
type ResetInstruction:
  payout Payout (1..1)
    [metadata reference]
  rateRecordDate date (0..1)
  resetDate date (1..1)
```

### Transfer Primitive

The transfer primitive function takes a `grossTransfer` object as
transfer instruction input and adds it to the `transferHistory`
attribute of the `TradeState`.

By design, the CDM treats the reset and the transfer primitive operators
separately because there is no one-to-one relationship between reset and
transfer.

-   Many transfer events are not tied to any reset: for instance, the
    currency settlement from an FX spot or forward transaction.
-   Conversely, not all reset events generate a cashflow: for instance,
    the single, final settlement that is based on all the past floating
    rate resets in the case of a compounding floating zero-coupon swap.

``` Haskell
type TransferInstruction:
    grossTransfer TransferState (1..*)
    netTransfer TransferState (0..1)

  condition MultipleIndividualTransferExistInNetTransfer:
      if netTransfer exists
      then grossTransfer count > 1
      and grossTransfer -> transferStatus all = TransferStatusEnum -> Netted

  condition NetTransferIsNotNetted:
      if netTransfer exists
      then netTransfer -> transferStatus <> TransferStatusEnum -> Netted
```

## Business Event

Business events are built according to the following principles:

-   **A business event is specified functionally by composing primitive
    operators**, each of which representing a fundamental change to the
    trade state and described by a primitive instruction.
-   **Business event qualification is inferred from those primitive
    components** and, in some relevant cases, from an additional
    *intent* qualifier associated with the business event. The inferred
    value is populated in the `eventQualifier` attribute.

``` Haskell
type BusinessEvent extends EventInstruction:
  [metadata key]
  [rootType]

  eventQualifier string (0..1)
  after TradeState (0..*)
```

``` Haskell
type Instruction:
  [rootType]
  primitiveInstruction PrimitiveInstruction (0..1)
  before TradeState (0..1)
    [metadata reference]
```

The only mandatory attributes of a business event are:

-   The event instruction. This is a list of `Instruction` objects, each
    representing a composite primitive instruction applied to a single
    (before) trade state. This attribute is of multiple cardinality, so
    a business event may impact multiple trades concurrently and result
    in multiple (after) trade states.
-   The event date. The time dimension has been purposely ommitted from
    the event's attributes. That is because, while a business event has
    a unique date, several time stamps may potentially be associated to
    that event depending on when it was submitted, accepted, rejected
    etc, all of which are *workflow* considerations.

---
**Note:**
The `primitives` attribute corresponds to a previous implementation of
the primitive operators, now deprecated but maintained for
backward-compatibility purposes. Because some implementations rely on
this former mechanism instead of the new primitive instruction
mechanism, the lower bound of the `instruction` attribute's cardinality
is 0 instead of 1. It will be updated to 1 once the former primitive
mechanism is fully retired.

---

## Event Composition

An example composition of primitive instructions to represent a complete
lifecycle event is shown below. The event represents the *partial
novation* of a contract, which comprises the following:

-   a split primitive
-   a quantity change primitive applied to each post-split trade, where
    the total quantity should match the quantity of the original trade
    and none of the quantities is 0. A quantity of 0 for the remaining
    trade would result in a termination and represent a *full novation*.
-   a party change primitive applied to the post-split trade whose
    quantity corresponds to the novated quantity.

``` JSON
"primitiveInstruction" : {
     "split" : {
       "breakdown" : [ {
         "partyChange" : {
           "counterparty" : {
             "partyReference" : {
               "value" : {
                 "name" : {
                   "value" : "Bank Z"
                 },
                 "partyId" : [ {
                   "identifier" : {
                     "value" : "LEI3RPT0003"
                   },
                   "identifierType" : "LEI",
                 } ]
               }
             },
             "role" : "PARTY_1"
           },
           "tradeId" : [ {
             "assignedIdentifier" : [ {
               "identifier" : {
                 "value" : "LEI3RPT0003DDDD"
               },
               "identifierType" : "UNIQUE_TRANSACTION_IDENTIFIER"
             } ],
             "issuer" : {
               "value" : "LEI3RPT0003"
             },
           } ]
         },
         "quantityChange" : {
           "change" : [ {
             "quantity" : [ {
               "value" : {
                 "amount" : 5000,
                 "unitOfAmount" : {
                   "currency" : {
                     "value" : "USD"
                   }
                 }
               }
             } ]
           } ],
           "direction" : "REPLACE"
         }
       }, {
         "quantityChange" : {
           "change" : [ {
             "quantity" : [ {
               "value" : {
                 "amount" : 8000,
                 "unitOfAmount" : {
                   "currency" : {
                     "value" : "USD"
                   }
                 }
               }
             } ]
           } ],
           "direction" : "REPLACE"
         }
       } ]
     }
   }
```

A business event is *atomic*, i.e. its primitive components cannot
happen independently. They either all happen together or not at all. In
the above partial novation example, the existing trade between the
original parties must be downsized at the same time as the trade with
the new party is instantiated. Trade compression is another example,
that involves multiple before trades being downsized or terminated and
new trades being created between multiple parties, all of which must
happen concurrently.

## Event Qualification {#event-qualification-section}

**The CDM qualifies lifecycle events as a function of their primitive
components** rather than explicitly declaring the event type. The CDM
uses the same approach for event qualification as for product
qualification and is based on a set of Event Qualification functions.
These functions are identified with a `[qualification BusinessEvent]`
annotatation.

Event Qualification functions apply a taxonomy-specific business logic
to identify if the state-transition attributes values, which are
embedded in the primitive event components, match the specified criteria
for the event named in that taxonomy. Like Product Qualification
functions, the Event Qualification function name is prefixed with the
word `Qualify_` followed by the taxonomy name.

The CDM uses the ISDA taxonomy V2.0 leaf level to qualify the event. 23
lifecycle events have currently been qualified as part of the CDM.

One distinction with the product approach is that the `intent`
qualification is also deemed necessary to complement the primitive event
information in certain cases. To this effect, the Event Qualification
function allows to specify that when present, the intent must have a
specified value, as illustrated by the below example.

``` Haskell
func Qualify_Termination:
  [qualification BusinessEvent]
  inputs:
    businessEvent BusinessEvent (1..1)
  output: is_event boolean (1..1)
  alias primitiveInstruction: businessEvent -> instruction -> primitiveInstruction only-element
  alias transfer: TransfersForDate( businessEvent -> after -> transferHistory -> transfer, businessEvent -> eventDate ) only-element

  set is_event:
    businessEvent -> intent is absent
      and (primitiveInstruction -> quantityChange only exists
          or (primitiveInstruction -> quantityChange, primitiveInstruction -> transfer) only exists)
      and (QuantityDecreasedToZero(businessEvent -> instruction -> before, businessEvent -> after) = True)
      and (businessEvent -> after -> state -> closedState -> state all = ClosedStateEnum -> Terminated)
```

If all the statements above are true, then the function evaluates to
True. In this case, the event is determined to be qualified as the event
type referenced by the function name.

The output of the qualification function is used to populate the
`eventQualifier` attribute of the `BusinessEvent` object, similar to how
product qualification works. An implementation of the CDM would call all
of the Event Qualification functions following the creation of each
event and then insert the appropriate value or provide an exception
message.

---
**Note:**
The type of the `eventQualifier` attribute in `BusinessEvent`, called
`eventType`, is a *meta-type* that indicates that its value is meant to
be populated using some functional logic. That functional logic must be
represented by a qualification function annotated with
`[qualification BusinessEvent]`, as in the example above. This mechanism
is further detailed in the Rosetta DSL documentation.

---

## Intent

The intent attribute is an enumeration value that represents the intent
of a particular business event. It is used in the event qualifcation
logic in cases where the primitive composition is not sufficient to
uniquely infer a lifecycle event.

The description of each possible enumeration value provides an
illustration of how this attribute is used.

``` Haskell
enum EventIntentEnum:
   Allocation
   CashFlow
   Clearing
   Compression
   ContractFormation
   ContractTermsAmendment
   CorporateActionAdjustment
   CreditEvent
   Decrease
   EarlyTerminationProvision
   Increase
   IndexTransition
   NotionalReset
   NotionalStep
   Novation
   ObservationRecord
   OptionExercise
   OptionalExtension
   OptionalCancellation
   PortfolioRebalancing
   PrincipalExchange
   Reallocation
   Repurchase
```

## Lineage

The `BusinessEvent` data type implements *lineage* by tying each trade
state to the trade state(s) that came before it in the lifecyle. The
`before` attribute in each instruction is included as a reference using
the `[metadata reference]` annotation. By definition the primitive
instruction applies to a trade state that *already* exists.

By contrast, the `after` trade state in the business event provides a
full definition of that object. That trade state is occurring for the
first time and as triggered by the application of the primitive
operators specified in the business event. The after trade state is
optional because it may be latent while the business event is going
through some acceptance workflow.

## Other Misc. Information

Other selected attributes of a business event are explained below.

-   The effective date is optional as it is not applicable to certain
    events (e.g. observations), or may be redundant with the event date.
-   The event qualifier attribute is derived from the event
    qualification features. This is further detailed in the [event
    qualification](#event-qualification) section.

# Workflow

The CDM provides support for implementors to develop workflows to
process transaction lifecycle events.

A *workflow* represents a set of actions or steps that are required to
trigger a business event, including the initial execution or contract
formation. A workflow is organised into a sequence in which each step is
represented by a *workflow step*. A workflow may involve multiple
parties in addition to the parties to the transaction, and may include
automated and manual steps. A workflow may involve only one step.

The CDM supports a workflow's audit trail by providing lineage from one
step to another in that workflow.

``` Haskell
type WorkflowStep:
  [metadata key]
  [rootType]
  businessEvent BusinessEvent (0..1)
  counterpartyPositionBusinessEvent CounterpartyPositionBusinessEvent (0..1)
  proposedEvent EventInstruction (0..1)
  rejected boolean (0..1)
  approval WorkflowStepApproval (0..*)
  previousWorkflowStep WorkflowStep (0..1)
    [metadata reference]
  nextEvent EventInstruction (0..1)
  messageInformation MessageInformation (0..1)
  timestamp EventTimestamp (1..*)
  eventIdentifier Identifier (1..*)
  action ActionEnum (0..1)
  party Party (0..*)
  account Account (0..*)
  lineage Lineage (0..1)
    [deprecated]
  creditLimitInformation CreditLimitInformation (0..1)
  workflowState WorkflowState (0..1)
```

The different attributes of a workflow step are detailed in the sections
below.

## Workflow Step Business Event

This attribute specifies the business event that the workflow step is
meant to generate. It is optional because the workflow may require a
number of interim steps before the state-transition embedded within the
business event becomes effective, therefore the business event does not
exist yet in those steps. The business event attribute is typically
associated with the final step in the workflow.

## Proposed Event

This attribute specifies the inputs required to perform the event's
state transition and comprises a subset of the attributes of the
business event itself. It is optional because it is only required for
all pre-acceptance workflow steps. Once accepted, the business event is
entirely represented, including its instructions, by the `businessEvent`
attribute.

Validation components are in place to check that the `businessEvent` and
`proposedEvent` attributes are mutually exclusive.

``` Haskell
type EventInstruction:
  intent EventIntentEnum (0..1)
  corporateActionIntent CorporateActionTypeEnum (0..1)
  eventDate date (0..1)
  effectiveDate date (0..1)
  packageInformation IdentifiedList (0..1)
  instruction Instruction (0..*)
```

## Next Event

Parties sometimes pre-agree a follow-on event that is meant to be
executed after the current event completes, but separately from it. A
typical scenario is an execution that specifies that the trade is
intended for clearing at a pre-specified clearing house. In this case,
the parameters of the next event are known in advance need to be agreed
between the parties as part of the current event.

The parameters of this next event are represented by an
`EventInstruction` data type included in the workflow process.

## Previous Workflow Step

This attribute, which is provided as a reference, defines the lineage
between steps in a workflow. The result is an audit trail for a business
event, which can trace the various steps leading to the business event
that was triggered.

## Action

The action enumeration qualification specifies whether the event is a
new one or a correction or cancellation of a prior one, which are trade
entry references and not reflective of negotiated changes to a contract.

## Message Information

The `messageInformation` attribute defines details for delivery of the
message containing the workflow steps.

``` Haskell
type MessageInformation:
  messageId string (1..1)
    [metadata scheme]
  sentBy string (0..1)
    [metadata scheme]
  sentTo string (0..*)
    [metadata scheme]
  copyTo string (0..*)
    [metadata scheme]
```

`sentBy`, `sentTo` and `copyTo` information is optional, as possibly not
applicable in a all technology contexts (e.g. in case of a distributed
architecture).

---
**Note:**
MessageInformation corresponds to some of the components of the FpML
*MessageHeader.model*.

---

## Timestamp

The CDM adopts a generic approach to represent timestamp information,
consisting of a `dateTime` and a `qualification` attributes, with the
latter specified through an enumeration value.

``` Haskell
type EventTimestamp:
  dateTime zonedDateTime (1..1)
  qualification EventTimestampQualificationEnum (1..1)
```

The benefits of the CDM generic approach are twofold:

-   It allows for flexibility in a context where it would be challenging
    to mandate which points in the process should have associated
    timestamps.
-   Gathering all of those in one place in the model allows for
    evaluation and rationalisation down the road.

Below is an instance of a CDM representation ([serialised](https://en.wikipedia.org/wiki/Serialization) into JSON)
of this approach.

``` JSON
"timestamp": [
 {
    "dateTime": "2007-10-31T18:08:40.335-05:00",
    "qualification": "EVENT_SUBMITTED"
 },
 {
    "dateTime": "2007-10-31T18:08:40.335-05:00",
    "qualification": "EVENT_CREATED"
 }
]
```

## Event Identifier

The Event Identifier provides a unique id that can be used for reference
by other workflow steps. The data type is a generic identifier component
that is used throughout the product and event models. The event
identifier information comprises the `assignedIdentifier` and an
`issuer`, which may be provided as a reference or via a scheme.

``` Haskell
type Identifier:
  [metadata key]
  issuerReference Party (0..1)
    [metadata reference]
  issuer string (0..1)
    [metadata scheme]
  assignedIdentifier AssignedIdentifier (1..*)

  condition IssuerChoice:
    required choice issuerReference, issuer
```

---
**Note:**
FpML also uses an event identifier construct: the `CorrelationId`, but
it is distinct from the identifier associated with the trade itself,
which comes in different variations: `PartyTradeIdentifier`, with the
`TradeId` and the `VersionedTradeId` as sub-components).

---

## Other Misc. Attributes

-   The `party` and `account` information are optional because not
    applicable to certain events.
-   The `lineage` attribute, now deprecated, was previously used to
    reference an unbounded set of contracts, events and/or payout
    components that an event may be associated to.

---
**Note:**
The `lineage` attribute is superseded by the implementation in the CDM
of: (i) trade state lineage, via the `before` reference in the primitive
instructions, and (ii) workflow lineage, via the `previousWorkflowStep`
attribute.

---
