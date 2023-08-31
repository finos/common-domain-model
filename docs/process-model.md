---
title: Process Model
---

# Purpose

## Why a Process Model

**The CDM lays the foundation for the standardisation, automation and
inter-operability of industry processes**. Industry processes represent
events and actions that occur through the transaction's lifecycle, from
negotiating a legal agreement to allocating a block-trade or calculating
settlement amounts.

While FINOS defines the protocols for industry processes in its library
of FINOS Documentation, differences in the implementation minutia may
cause operational friction between market participants. Evidence shows
that even when calculations are defined in mathematical notation (for
example, day count fraction formulae which are used when calculating
interest rate payments) can be a source of dispute between parties in a
transaction.

## What Is the Process Model

**The CDM Process Model has been designed to translate the technical
standards that support those industry processes** into a standardised
machine-readable and machine-executable format.

Machine readability and executability is crucial to eliminate
implementation discrepancy between market participants and increase
interoperability between technology solutions. It greatly minimises the
cost of adoption and provides a blueprint on which industry utilities
can be built.

## How Does It Work

The data and process model definitions of the CDM are systematically
translated into executable code using purpose-built code generation
technology. The CDM executable code is available in a number of modern,
widely adopted and freely available programming languages and is
systematically distributed as part of the CDM release.

The code generation process is based on the Rosetta DSL and is further
described in the [Code Generation Section](https://docs.rosetta-technology.io/rosetta/rosetta-dsl/rosetta-code-generators), including an up-to-date
list of [available languages](https://docs.rosetta-technology.io/rosetta/rosetta-dsl/rosetta-code-generators/#what-code-generators-are-available). Support for further languages can be
added as required by market participants.

# Scope

The scope of the process model has two dimensions:

1.  **Coverage** - which industry processes should be covered.
2.  **Granularity** - at which level of detail each process should be
    specified.

## Coverage

**The CDM process model currently covers the post-trade lifecycle of
securities, contractual products, and foreign exchange**. Generally, a
process is in-scope when it is already covered in FINOS Documentation or
other technical documents. For example, the following processes are all
in scope:

-   Trade execution and confirmation
-   Clearing
-   Allocation
-   Reallocation
-   Settlement (including any future contingent cashflow payment)
-   Return (settlement of the part and/or full return of the loaned
    security as defined by a Securities Lending transaction.)
-   Billing (calculation and population of invoicing for Securities
    Lending transactions)
-   Exercise of options
-   Margin calculation
-   Regulatory reporting (although covered in a different documentation
    section)

## Granularity

**It is important for implementors of the CDM to understand the scope of
the model** with regard to specifications and executable code for the
above list of post-trade lifecycle processes.

The CDM process model leverages the *function* component of the Rosetta
DSL. A function receives a set of input values and applies logical
instructions to return an output. The input and output are both CDM
objects (including basic types). While a function specifies its inputs
and output, its logic may be *fully defined* or only *partially defined*
depending on how much of the output's attribute values it builds.
Unspecified parts of a process represent functionality that firms are
expected to implement, either internally or through third-parties such
as utilities.

It is not always possible or practical to fully specify the business
logic of a process from a model. Parts of processes or sub-processes may
be omitted from the CDM for the following reasons:

-   The sub-process is not needed to create a functional CDM output
    object.
-   The sub-process has already been defined and its implementation is
    widely adopted by the industry.
-   The sub-process is specific to a firm's internal process and
    therefore cannot be specified in an industry standard.

Given these reasons, the CDM process model focuses on the most critical
data and processes required to create functional objects that satisfy
the below criterion:

-   All of the qualifiable constituents (such as `BusinessEvent` and
    `Product`) of a function's output can be qualified, which means
    that they evaluate to True according to at least one of the
    applicable Qualification functions.
-   Lineage and cross-referencing between objects is accurate for data
    integrity purposes.

For any remaining data or processes, implementors can populate the
remaining attribute values required for the output to be valid by
extending the executable code generated by the process model or by
creating their own functions.

For the trade lifecycle processes that are in scope, the CDM process
model covers the following sub-process components, which are each
detailed in the next sections:

1.  Validation process
2.  Calculation process
3.  Event creation process

## Validation Process

In many legacy models and technical standards, validation rules are
generally specified in text-based documentation, which requires software
engineers to evaluate and translate the logic into code. The frequently
occuring result of this human interpretation process is inconsistent
enforcement of the intended logic.

By contrast, in the CDM, validation components are an integral part of
the process model specifications and are distributed as executable code
in the Java representation of the CDM. The CDM validation components
leverage the *validation* components of the Rosetta DSL.

## Product Validation

As an example, the *FpML ird validation rule #57*, states that if the
calculation period frequency is expressed in units of month or year,
then the roll convention cannot be a weekday. A machine readable and
executable definition of that specification is provided in the CDM, as a
`condition` attached to the `CalculationPeriodFrequency` type:

``` Haskell
condition FpML_ird_57:
  if period = PeriodExtendedEnum -> M or period = PeriodExtendedEnum -> Y
  then rollConvention <> RollConventionEnum -> NONE
    and rollConvention <> RollConventionEnum -> SFE
    and rollConvention <> RollConventionEnum -> MON
    and rollConvention <> RollConventionEnum -> TUE
    and rollConvention <> RollConventionEnum -> WED
    and rollConvention <> RollConventionEnum -> THU
    and rollConvention <> RollConventionEnum -> FRI
    and rollConvention <> RollConventionEnum -> SAT
    and rollConvention <> RollConventionEnum -> SUN
```

## Calculation Process

The CDM provides certain FINOS Definitions as machine executable formulas
to standardise the industry calculation processes that depend on those
definitions. Examples include the FINOS 2006 definitions of *Fixed
Amount* and *Floating Amount* , the FINOS 2006 definitions of Day Count
Fraction rules, and performance calculations for Equity Swaps. The CDM
also specifies related utility functions.

These calculation processes leverage the *calculation function*
component of the Rosetta DSL which is associated to a `[calculation]`
annotation.

Explanations of these processes are provided in the following sections.

## Base Libraries - Vector Math

The CDM includes a very basic library for performing vector math. This
is intended to support more complex calculations such as daily
compounded floating amounts. The CDM includes a basic implementation of
these functions in Java, and allows individual implementations to
substitute their own more robust representations.

A small library of functions for working with vectors (ordered
collections of numbers) has been added to CDM to support Rosetta
functions needing to perform complex mathematical operations.
Anticipated uses include averaging and compounding calculations for
floating amounts, but the functions are designed to be general use.

The functions are located in `base-math-func`.

Functions include:

-   `VectorOperation`: Generates a result vector by applying the
    supplied arithmetic operation to each element of the supplied left
    and right vectors in turn. i.e. result\[n\] = left\[n\] \[op\]
    right\[n\], where \[op\] is the arithmetic operation defined by
    arithmeticOp. This function can be used to, for example, multiply or
    add two vectors.
-   `VectorScalarOperation`: Generates a result vector by applying the
    supplied arithmetic operation and scalar right value to each element
    of the supplied left vector in turn. i.e. result\[n\] = left\[n\]
    \[op\] right, where \[op\] is the arithmetic operation defined by
    arithmeticOp. This function can be used to, for example, multiply a
    vector by a scalar value, or add a scalar to a vector.
-   `VectorGrowthOperation`: Generates a result vector by starting with
    the supplied base value (typically 1), and then multiplying it in
    turn by each growth factor, which is typically a number just
    above 1. For instance, a growth factor of 1.1 represents a 10%
    increase, and 0.9 a 10% decrease. The results will show the
    successive results of applying the successive growth factors, with
    the first value of the list being the supplied baseValue, and final
    value of the results list being the product of all of the supplied
    values. i.e. result\[1\] = baseValue \* factor\[1\], result\[n\] =
    result\[n-1\] \* factor\[n\]. The resulting list will have the one
    more element than the supplied list of factors. This function is
    useful for performing compounding calculations.
-   `AppendToVector`: Appends a single value to a vector.

Also a new scalar functions has been added to better support floating
rate processing:

-   `RoundToPrecision`: Rounds a supplied number to a specified
    precision (in decimal places) using a roundingMode of type
    `RoundingDirectionEnum`. This is similar to `RoundToNearest` but
    takes a precision rather than an amount, and uses a different
    rounding mode enumeration that supports more values.

## Base Libraries - Date Math

The CDM includes a very basic library for performing date math. This is
intended to support more complex calculations such as daily compounded
floating amounts. The CDM includes a basic implementation of these
functions in Java, and allows individual implementations to substitute
their own more robust representations.

A small library of functions for working with dates and lists of dates
has been added to CDM to support Rosetta functions needing to perform
date mathematics. Anticipated uses include date list generation for
modular rate calculations for floating amounts, but the functions are
designed to be general use.

There is a basic Java language implementation that can be used, or users
can provide their own implementations of these functions using a more
robust date math library.

The functions are located in `base-datetime-func`.

Functions include:

-   `GetAllBusinessCenters`: Returns a merged list of
    BusinessCenterEnums for the supplied BusinessCenters.
-   `BusinessCenterHolidaysMultiple`: Returns a sorted list of holidays
    for the supplied business centers.
-   `BusinessCenterHolidays`: Returns a list of holidays for the
    supplied business center.
-   `DayOfWeek`: Returns the day of week corresponding to the supplied
    date.
-   `AddDays`: Adds the specified number of calendar days to the
    supplied date. A negative number will generate a date before the
    supplied date.
-   `DateDifference`: Subtracts the two supplied dates to return the
    number of calendar days between them . A negative number implies
    first is after second.
-   `LeapYearDateDifference`: Subtracts the two supplied dates to return
    the number of leap year calendar days between them (that is, the
    number of dates that happen to fall within a leap year). A negative
    number implies firstDate is after secondDate.
-   `AppendDateToList`: Add a date to a list of dates.
-   `PopOffDateList`: Remove last element from a list of dates.

The following are implemented in Rosetta based on the above primitives:

-   `IsWeekend`: Returns whether the supplied date is a weekend. This
    implementation currently assumes a 5 day week with Saturday and
    Sunday as holidays. A more sophisticated implementation might use
    the business centers to determine which days are weekends, but most
    jurisdictions where derivatives are traded follow this convention.
-   `IsHoliday`: Returns whether a day is a holiday for the specified
    business centers.
-   `IsBusinessDay`: Returns an indicator of whether the supplied date
    is a good business date given the supplied business centers. True
    =\> good date, i.e. not a weekend or holiday. False means that it is
    either a weekend or a holiday.
-   `AddBusinessDays`: Returns a good business date that has been offset
    by the given number of business days given the supplied business
    centers. A negative value implies an earlier date (before the
    supplied originalDate), and a positive value a later date (after the
    supplied date).
-   `GenerateDateList`: Creates a list of good business days starting
    from the startDate and going to the end date, inclusive, omitting
    any days that are weekends or holidays according to the supplied
    business centers.

## Base Libraries - Daycounting

The CDM includes a library for performing day counting calculations.

It includes functions as follows: \* `YearFraction`: The fraction of a
year represented by a date range. \* `YearFractionForOneDay`[: Return
the year fraction represented by a single day, i.e. 1 / dayCountBasis,
where dayCountBasis represents the denominator of the day count
fraction. This perhaps should take into account leap years, though the
FINOS compounding formulas do not cover ACT basis at the moment. *`DayCountBasis`: Return the day count basis
(the denominator of the day count fraction) for the day count fraction.

## Floating Rate Option/Index Features

The CDM includes features for retrieving information about floating rate
options and for calculating custom ("modular") floating rates.

Functions for retrieving information about FROs include:

-   `IndexValueObservation`: Retrieve the values of the supplied index
    on the specified observation date.
-   `IndexValueObservationMultiple`: Retrieve the values of the supplied
    index on the specified observation dates.
-   `FloatingRateIndexMetadata`: Retrieve all available metadata for the
    floating rate index.
-   `ValidateFloatingRateIndexName`: Return whether the supplied
    floating rate index name is valid for the supplied contractual
    definitions.

Functions for calculating modular floating rates include:

-   `EvaluateCalculatedRate`: Evaluate a calculated rate as described in
    the 2021 FINOS Definitions, Section 7
-   `GenerateObservationDatesAndWeights`: Apply shifts to generate the
    list of observation dates and weights for each of those dates.
-   `ComputeCalculationPeriod`: Determine the calculation period to use
    for computing the calculated rate (it may not be the same as the
    normal calculation period, for instance if the rate is set in
    advance)
-   `DetermineObservationPeriod`: Determine any applicable
    offsets/shifts for the period for observing an index, and then
    generate the date range to be used for observing the index, based on
    the calculation period, plus any applicable offsets/shifts.
-   `GenerateObservationPeriod`: Generate the date range to be used for
    observing the index, based on the calculation period, plus any
    applicable offsets/shifts.
-   `GenerateObservationDates`: Generate the list of observation dates
    given an observation period.
-   `DetermineWeightingDates`: Determine the dates to be used for
    weighting observations.
-   `ProcessObservations`: Apply daily observation parameters to rate
    observation. These are discussed in the 2021 FINOS Definitions,
    section 7.2.3 and 7.2.4.
-   `GenerateWeights`: Recursively creates a list of weights based on
    the date difference between successive days.
-   `ApplyCompoundingFormula`: Implements the compounding formula:
    Product of ( 1 + (rate \* weight) / basis), then backs out the final
    rate. This is used to support section 7.3 of the 2021 FINOS
    Definitions.
-   `ApplyAveragingFormula`: Implements the weighted arithmetic
    averaging formula. Sums the weighted rates and divides by the total
    weight. This is used to support section 7.4 of the 2021 FINOS
    Definitions.

## Fixed Amount and Floating Amount Definitions

The CDM includes preliminary features for calculating fixed and floating
amounts for interest rate payouts.

Base calculation functions include:

-   `FixedAmountCalculation`: Calculates the fixed amount for a
    calculation period by looking up the notional and the fixed rate and
    multiplying by the year fraction
-   `GetFixedRate`: Look up the fixed rate for a calculation period
-   `FloatingAmountCalculation`: Calculate a floating amount for a
    calculation period by determining the raw floating rate, applying
    any rate treatments, looking up the calculation period notional,
    then performing the multiplication of the notional, rate, and year
    fraction. Floating amount calculations are described in the 2021
    FINOS Definitions in Section 6 and 7.
-   `GetNotionalAmount`: Look up the notional amount in effect for a
    calculation period
-   `GetQuantityScheduleStepValues`: Find all schedule step values whose
    stepDate is before or equal to the supplied periodStartDate. Returns
    a list of step values starting from the initial quantity value, to
    the last step value before the periodStartDate.
-   `CalculateYearFraction`: Calculate the year fraction for a single
    calculation period, by invoking the base year fraction logic

Floating rate processing an calculation functions include:

-   `DetermineFloatingRateReset`: Get the value of a floating rate by
    either observing it directly or performing a rate calculation. This
    function works differently depending on the rate category and style,
    as described in the 2021 FINOS Definitions, Section 6.6.
-   `GetFloatingRateProcessingType`: Get a classification of the
    floating rate is processed. This is based on FRO category, style,
    and calculation method, as described in the 2021 FINOS Definitions
    Section 6.6. The categorization information is obtained from the FRO
    metadata.
-   `ProcessFloatingRateReset`: Entry point for the function that
    performs the floating rate resetting operation. There are different
    variations depending on the processing type (e.g. screen rate, OIS,
    modular calculated rate).
-   `GetCalculatedFROCalculationParameters`: Initialize a calculation
    parameters block for an OIS or a daily average rate. Used to support
    FROs that include an embedded calculation.
-   `ProcessFloatingRateReset(processingType: FloatingRateIndexProcessingTypeEnum->CompoundIndex)`:
    Call the compounded index processing logic to calculate the reset
-   `EvaluateScreenRate`: Evaluate/lookup the value of a screen rate
-   `DetermineResetDate`: Determine the value of the reset date given a
    reset dates structure and a calculation period for which it's
    needed. Reset dates are defined in the 2021 FINOS Definition in
    Section 6.5.5.
-   `DetermineFixingDate`: Determine the observation (fixing) date
    needed given a reset dates structure and a reset date.
-   `GetFloatingRateProcessingParameters`: Determine the processing
    parameters to use from the InterestRatePayout by looking them up if
    necessary from the corresponding schedules in the interest rate
    stream
-   `SpreadAmount`: Look up the spread amount for a calculation period.
-   `MultiplierAmount`: Look up the multiplier amount for a calculation
    period.
-   `CapRateAmount`: Look up the cap rate amount for a calculation
    period.
-   `FloorRateAmount`: Look up the floor rate amount for a calculation
    period.
-   `GetRateScheduleAmount`: Look up an amount for a calculation period
    from a rate schedule
-   `ApplyFloatingRateProcessing`: Perform rate treatments on floating
    rates, such as applying spreads, multipliers, caps and floors,
    rounding, and negative interest treatment.
-   `ApplyFloatingRatePostSpreadProcessing`: Perform post-spread rate
    treatments on floating rates, such as applying caps and floors,
    rounding, and negative interest treatment.
-   `ApplyCapsAndFloors`: Apply any cap or floor rate as a constraint on
    a regular swap rate, as discussed in the 2021 FINOS Definitions,
    section 6.5.8 and 6.5.9
-   `ApplyUSRateTreatment`: Apply the US rate treatment logic where
    applicable (Bond Equivalent Yield, Money Market Yield, as described
    in the 2021 FINOS Definitions, section 6.9. (NB: this function does
    not have an implementation.)
-   `ApplyFinalRateRounding`: Apply the final rate rounding treatment
    logic as described in the 2021 FINOS Definitions, section 4.8.1.

Most of the above have a preliminary implementation for feedback. A few
are only defined as "do-nothing" interfaces, and users needing these
features would need to implement the functions.

## Fixed Amount and Floating Amount Definitions

The CDM expressions of `FixedAmount` and `FloatingAmount` are similar in
structure: a calculation formula that reflects the terms of the FINOS
2006 Definitions and the arguments associated with the formula.

``` Haskell
func FloatingAmount:
  [calculation]
  inputs:
      interestRatePayout InterestRatePayout (1..1)
      rate number (0..1)
      notional number (0..1)
      date date (0..1)
      calculationPeriodData CalculationPeriodData (0..1)
  output:
      floatingAmount number (1..1)

  alias calculationPeriod:
      if calculationPeriodData exists then calculationPeriodData else CalculationPeriod(interestRatePayout -> calculationPeriodDates, date)
  alias calcPeriodBase : Create_CalculationPeriodBase(calculationPeriod)
  alias floatingCalc : FloatingAmountCalculation(interestRatePayout, calcPeriodBase, False, notional, rate)

  set floatingAmount : floatingCalc-> calculatedAmount
```

## Year Fraction

The CDM process model incorporates calculations that represent the set
of day count fraction rules specified as part of the FINOS 2006
Definitions, e.g. the *ACT/365.FIXED* and the *30E/360* day count
fraction rules. Although these rules are widely accepted in
international markets, many of them have complex nuances which can lead
to inconsistent implementations and potentially mismatched settlements.

For example, there are three distinct rule sets in which the length of
each month is generally assumed to be 30 days for accrual purposes (and
each year is assumed to be 360 days). However there are nuances in the
rule sets that distinguish the resulting calculations under different
circumstances, such as when the last day of the period is the last day
of February. These distinct rule sets are defined by FINOS as 30/360
(also known as 30/360 US), 30E/360 (formerly known as 30/360 ICMA or
30/360 Eurobond), and the 30E/360.FINOS.

The CDM process model eliminates the need for implementors to interpret
the logic and write unique code for these rules. Instead, it provides a
machine-readable expression that generates executable code, such as the
example below:

``` Haskell
func YearFraction(dayCountFractionEnum: DayCountFractionEnum -> _30E_360):
   [calculation]

   alias startYear: startDate -> year
   alias endYear: endDate -> year
   alias startMonth: startDate -> month
   alias endMonth: endDate -> month
   alias endDay: Min(endDate -> day, 30)
   alias startDay: Min(startDate -> day, 30)

   set result:
       (360 * (endYear - startYear) + 30 * (endMonth - startMonth) + (endDay - startDay)) / 360
```

## Utility Function

CDM elements often need to be transformed by a function to construct the
arguments for a formula in a calculation. A typical example is the
requirement to identify a period start date, end date, and other
date-related attributes required to compute a cashflow amount in
accordance with a schedule (as illustrated in the day count fraction
calculation shown above). The CDM has two main types to address this
requirement:

-   `CalculationPeriodDates` specifies the inputs required to construct
    a calculation period schedule
-   `CalculationPeriodData` specifies actual attribute values of a
    calculation period such as start date, end date, etc.

The CalculationPeriod function receives the `CalculationPeriodDates` and
the current date as the inputs and produces the `CalculationPeriodData`
as the output, as shown below:

``` Haskell
func CalculationPeriod:
  inputs:
    calculationPeriodDates CalculationPeriodDates (1..1)
    date date (1..1)
  output: result CalculationPeriodData (1..1)
```

## Equity Performance

The CDM process model includes calculations to support the equity
performance concepts applied to reset and pay cashflows on Equity Swaps.
Those calculations follow the definitions as normalised in the new *2018
FINOS CDM Equity Confirmation for Security Equity Swap* (although this is
a new template that is not yet in use across the industry).

Some of those calculations are presented below:

``` Haskell
func EquityCashSettlementAmount:
    inputs:
        tradeState TradeState (1..1)
        date date (1..1)
    output:
        equityCashSettlementAmount Transfer (1..1)

    alias equityPerformancePayout:
        tradeState -> trade -> tradableProduct -> product -> contractualProduct -> economicTerms -> payout -> performancePayout only-element
    alias equityPerformance:
        EquityPerformance(tradeState ->trade, tradeState -> resetHistory only-element -> resetValue, date)
    alias payer:
        ExtractCounterpartyByRole( tradeState -> trade -> tradableProduct -> counterparty, equityPerformancePayout -> payerReceiver -> payer ) -> partyReference
    alias receiver:
        ExtractCounterpartyByRole( tradeState -> trade -> tradableProduct -> counterparty, equityPerformancePayout -> payerReceiver -> receiver ) -> partyReference

    set equityCashSettlementAmount -> quantity -> value:
        Abs(equityPerformance)
    set equityCashSettlementAmount -> quantity -> unit -> currency:
        ResolveEquityInitialPrice(
           tradeState -> trade -> tradableProduct -> tradeLot only-element -> priceQuantity -> price
        ) -> unit -> currency
    set equityCashSettlementAmount -> payerReceiver -> payerPartyReference:
        if equityPerformance >= 0 then payer else receiver
    set equityCashSettlementAmount -> payerReceiver -> receiverPartyReference:
        if equityPerformance >= 0 then receiver else payer
    set equityCashSettlementAmount -> settlementDate -> adjustedDate:
        ResolveCashSettlementDate(tradeState)
    set equityCashSettlementAmount -> settlementOrigin -> performancePayout:
        equityPerformancePayout as-key
```

``` Haskell
func RateOfReturn:
   inputs:
       initialPrice PriceSchedule (1..1)
       finalPrice PriceSchedule (1..1)
   output:
       rateOfReturn number (1..1)

   alias initialPriceValue:
       initialPrice->value
   alias finalPriceValue:
       finalPrice->value
   set rateOfReturn:
       if finalPriceValue exists and initialPriceValue exists and initialPriceValue > 0 then
           (finalPriceValue - initialPriceValue) / initialPriceValue
```

## Billing

The CDM process model includes calculations to support the billing event
consisting of the individual amounts that need to be settled in relation
to a portfolio of Security Loans. These calculations leverage the
_FixedAmount_, _FloatingAmount_ and _Day Count
Fraction_ calculations described earlier in the
documentation. A functional model is provided to populate the
_SecurityLendingInvoice_ data type following the definitions
as normalised in the *ISLA best practice handbook*

The data type and function to generate a Security Lending Invoice:

``` Haskell
type SecurityLendingInvoice:
  [rootType]
  [metadata key]
  sendingParty Party (1..1)
  receivingParty Party (1..1)
  billingStartDate date (1..1)
  billingEndDate date (1..1)
  billingRecord BillingRecord (1..*)
  billingSummary BillingSummary (1..*)
```

``` Haskell
func Create_SecurityLendingInvoice:
   inputs:
     instruction BillingInstruction (1..1)
   output:
     invoice SecurityLendingInvoice (1..1)

   set invoice -> sendingParty:
     instruction -> sendingParty
   set invoice -> receivingParty:
     instruction -> receivingParty
   set invoice -> billingStartDate:
     instruction -> billingStartDate
   set invoice -> billingEndDate:
     instruction -> billingEndDate
   add invoice -> billingRecord:
     Create_BillingRecords( instruction -> billingRecordInstruction )
   add invoice -> billingSummary:
     Create_BillingSummary( invoice -> billingRecord )
```

# Lifecycle Event Process

While the lifecycle event model described in the
[event-model-section](/docs/event-model) provides a
standardised data representation of those events using the concept of
*primitive event* components, the CDM must further specify the
processing of those events to ensure standardised implementations across
the industry. This means specifying the *logic* of the state-transition
as described by each primitive event component.

In particular, the CDM must ensure that:

-   The lifecycle event process model constructs valid CDM event
    objects.
-   The constructed events qualify according to the qualification logic
    described in the [event-qualification-section](/docs/event-model#event-qualification-section).
-   The lineage between states allows an accurate reconstruction of the
    trade's lifecycle sequence.

There are three levels of function components in the CDM to define the
processing of lifecycle events:

1.  Primitive creation
2.  Event creation
3.  Workflow step creation

Each of those components can leverage any calculation or utility
function already defined in the CDM. As part of the validation processe
embedded in the CDM, an object validation step is included in all these
object creation functions to ensure that they each construct valid CDM
objects. Further details on the underlying calculation and validation
processes are described in the [calculation-process](#calculation-process) and [validation-process](#validation-process).

Illustration of the three components are given in the sections below.

## Primitive Creation

Primitive creation functions can be thought of as the fundamental
mathematical operators that operate on a *trade state*. While a
primitive event object describes each state transition in terms of
*before* and *after* trade states, a primitive creation function defines
the logic to transition from that *before* trade state to the *after*
trade state, using a set of *instructions*.

An example of such use is captured in the reset event of an Equity Swap.
The reset is processed in following steps:

1.  Resolve the `Observation` that contains the equity price, using
    specific product definition terms defined on `EquityPayout`.
2.  Construct a `Reset` using the equity price on `Observation`. In this
    scenario, the reset value is the equity price.
3.  Append `Reset` onto `TradeState`, creating a new instance of
    `TradeState`.

At the end of each period in the life of the Equity Swap, the reset
process will append further reset values onto the *trade state*. The
series of equity prices then supports equity performance calculation as
each reset value will represent the equity price at the end of one
period and the start of the next.

These above steps are codified in the `Create_Reset` function, which
defines how the `Reset` instance should be constructed.

``` Haskell
func Create_Reset:
    inputs:
        instruction ResetInstruction (1..1)
        tradeState TradeState (1..1)
    output:
        reset TradeState (1..1)

    alias payout:
       instruction -> payout

   alias observationDate:
       if instruction -> rateRecordDate exists
       then instruction -> rateRecordDate
       else instruction -> resetDate

   alias observationIdentifiers:
       if payout -> performancePayout count = 1 then ResolvePerformanceObservationIdentifiers(payout -> performancePayout only-element, instruction -> resetDate)
       else if payout -> interestRatePayout exists then ResolveInterestRateObservationIdentifiers(payout -> interestRatePayout only-element, observationDate)

   alias observation:
       ResolveObservation([observationIdentifiers], empty)

   set reset:
       tradeState

   add reset -> resetHistory:
       if payout -> performancePayout count = 1 then ResolvePerformanceReset(payout -> performancePayout only-element, observation, instruction -> resetDate)
       else if payout -> interestRatePayout exists then ResolveInterestRateReset(payout -> interestRatePayout, observation, instruction -> resetDate, instruction -> rateRecordDate)
```

First, `ResolvePerformanceObservationIdentifiers` defines the specific
product definition terms used to resolve `ObservationIdentifier`s. An
`ObservationIdentifier` uniquely identifies an `Observation`, which
inside holds a single item of market data and in this scenario will hold
an equity price.

Specifying precisely which attributes from `PerformancePayout` should be
used to resolve the equity price is important to ensure consistent
equity price resolution for all model adopters.

``` Haskell
func ResolvePerformanceObservationIdentifiers:
    inputs:
        payout PerformancePayout (1..1)
        adjustedDate date (1..1)
    output:
        identifiers ObservationIdentifier (1..1)

    alias adjustedFinalValuationDate:
        ResolveAdjustableDate( payout -> valuationDates -> valuationDatesFinal -> valuationDate )

    alias valuationDates:
        if adjustedDate < adjustedFinalValuationDate then
            payout -> valuationDates -> valuationDatesInterim
        else
            payout -> valuationDates -> valuationDatesFinal

    add identifiers -> observable -> productIdentifier:
        payout -> underlier -> security -> productIdentifier

    set identifiers -> observationDate:
        AdjustedValuationDates( payout -> valuationDates )
            filter item <= adjustedDate
            then last

    set identifiers -> observationTime:
        ResolvePerformanceValuationTime(valuationDates -> valuationTime,
            valuationDates -> valuationTimeType,
            identifiers -> observable -> productIdentifier only-element,
            valuationDates -> determinationMethod )

    set identifiers -> determinationMethodology -> determinationMethod:
        valuationDates -> determinationMethod
```

`ResolveObservation` provides an interface for adopters to integrate
their market data systems. It specifies the input types and the output
type, which ensures the integrity of the observed value.

``` Haskell
func ResolveObservation:
    inputs:
        identifiers ObservationIdentifier (1..*)
        averagingMethod AveragingCalculationMethod (0..1)
    output:
        observation Observation (1..1)
```

The construction of the `Reset` in our scenario then becomes trivial,
once the equity price has been retrieved, as the equity price and reset
date are simply assigned to the corresponding attributes on the `Reset`.

``` Haskell
func ResolvePerformanceReset:
    inputs:
        performancePayout PerformancePayout (1..1)
        observation Observation (1..1)
        date date (1..1)
    output:
        reset Reset (1..1)

    set reset -> resetValue:
        observation -> observedValue

    set reset -> resetDate:
        date

    add reset -> observations:
        observation
```

## Workflow Step Creation

(*This feature is currently being developed and will be documented upon
release in the CDM*)
