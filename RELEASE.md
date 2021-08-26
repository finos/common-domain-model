# *Product Model - Interest Payout Calculation Enhancements*

_What is being released?_

A set of enhanced types and functions for computing interest rate payout amounts, and in particular floating amounts.

The enhancements are located in namespaces `cdm.product.asset.calculation`, `cdm.product.asset.floatingrate` and `cdm.observable.asset.calculatedrate`.

* `cdm.product.asset.calculation` includes enhanced fixed and floating amount calculations, with the capability to look up the notional in effect during the calculation period, and to do day count calculations using a simplified day count implementation that uses the base date library.
* `cdm.product.asset.floatingrate` includes floating rate setting and processing, with the capability to look up and apply rate processing such as spreads, multipliers, caps and floors, etc.
* `cdm.observable.asset.calculatedrate` includes a preliminary implementation of the new modular calculated floating rates (such as lookback compound or observation shift daily average) defined in the 2021 ISDA definitions.  Also supports OIS rate calculations.
* `cdm.observable.asset.fro` - includes logic for retrieving Floating Rate Option definitional metadata and index values
*  `cdm.base.daycount` - new day counting logic, independent of calculation period calculations

The current implementation should be viewed as **experimental** and is being released for review and feedback.  Implementers using these capabilities are cautioned that they should test the results carefully, and report any issues or concerns to the CDM team.

Some of the calculation period amount capabilities that are included in FpML and the ISDA Definitions that are not yet fully supported in these enhancements include:
* Initial Rate:  there is a basic implementation of rates specified for the initial period, but it is not thoroughly tested and may require adjustment to handle all cases correctly.
* Stubs:  There is no support for stub rate calculations at this time
* Compounding methods:  there is currently only support for calculating the cash flows for a single calculation period.   There is some support roughed in for compounding over multiple calculation periods using the CompoungingMethod, such as calculating spread-exclusive rates and cashflows, but this has not been used or tested yet.
* Reset averaging:  there is currently no support for averaging using the resetDates concept, where the reset period is set to more frequent than the calculation period.
* The US rate treatment logic has a defined entry point but no implementation as yet.  It may be that the interface to that function will need adjustment to provide all the necessary information for the calculation, although an attempt has been made to suply the necessary information.

In addition, some logic has been refactored.  For example, the DayCountFractionEnum has been moved to cdm.base.daycount.

We anticipate that some additional cleanup changes will be made in subsequent releases, including:
* The existing `FixedAmount` and `FloatingAmount` functions are likely to be rewritten to use the new calculation logic.
* The existing `DayCountFraction` function is likely to be retired, replaced by the new YearFraction function


Following is a description of each of the functions included in the release.

New functions in `cdm.product.asset.calculation` include:

* `FixedAmountEnhanced`: Calculates the fixed amount for a calculation period by looking up the notional and the fixed rate and multiplying by the year fraction
* `FixedAmounCalculationNew`: Calculates the fixed amount for a calculation period by looking up the notional and the fixed rate and multiplying by the year fraction
* `LookupFixedRate`: Look up the fixed rate for a calculation period
* `FloatingAmountEnhanced`: 2006 ISDA Definition Article 6 Section 6.1. Calculation of a Floating Amount: Subject to the provisions of Section 6.4 (Negative Interest Rates), the Floating Amount payable by a party on a Payment Date will be: (a) if Compounding is not specified for the Swap Transaction or that party, an amount calculated on a formula basis for that Payment Date or for the related Calculation Period as follows: Floating Amount = Calculation Amount  Floating Rate + Spread  Floating Rate Day Count Fraction (b) if 'Compounding' is specified to be applicable to the Swap Transaction or that party and 'Flat Compounding' is not specified, an amount equal to the sum of the Compounding Period Amounts for each of the Compounding Periods in the related Calculation Period; or (c) if 'Flat Compounding' is specified to be applicable to the Swap Transaction or that party, an amount equal to the sum of the Basic Compounding Period Amounts for each of the Compounding Periods in the related Calculation Period plus the sum of the Additional Compounding Period Amounts for each such Compounding Period.   This enhanced version supports parameterized rate treatments, such as spread schedules, cap rate schedules, negative rate treatment, final rate rounding, etc.
* `FloatingAmountCalculation`: Calculate a floating amount for a calculation period by determining the raw floating rate, applying any rate treatments, looking up the calculation period notional, then performing the multiplication of the notional, rate, and year fraction.  Floating amount calculations are described in the 2021 ISDA Definitions in Section 6 and 7.
* `LookupNotionalAmount`: Look up the notional amount in effect for a calculation period
* `LookupQuantityScheduleAmount`: Look up a value from a quantity schedule given a supplied starting date.  It will return the value of the last step that is before the supplied starting date, or if none matches, the initial value.
* `FindNonNegativeScheduleSteps`: Find all  schedule step values whose stepDate is before or equal to the supplied periodStartDate, starting from supplied startingStep number.  Returns a list of step values starting from the last matching one and going in reverse order.  Done this slightly odd way for efficiency and simplicity in code generation.
* `SelectNonNegativeScheduleStep`: Retrieve a single step from a  schedule given a step number.  This is an entry point to a function written in a native language like Java.  Returns the step if it exists, else null. The index is 0-based, so 0 returns the first step.
* `CalculateYearFraction`: Calculate the year fraction for a single calculation period, by invoking the base year fraction logic

New functions in `cdm.product.asset.floatingrate` include:
* `DetermineFloatingRateReset`: Get the value of a floating rate by either observing it directly or performing a rate calculation.  This function works differently depending on the rate category and style, as described in the 2021 ISDA Definitions, Section 6.6.
* `GetFloatingRateProcessingType`:  Get a classification of  the floating rate is processed. This is based on FRO category, style, and calculation method, as described in the 2021 ISDA Definitions Section 6.6.  The categorization information is obtained from the FRO metadata.
* `ProcessFloatingRateReset`: Entry point for the function that performs the floating rate resetting operation.  There are different variations depending on the processing type (e.g. screen rate, OIS, modular calculated rate).
* `GetCalculatedFROCalculationParameters`: Initialize a calculation parameters block for an OIS or a daily average rate. Used to support FROs that include an embedded calculation.
* `ProcessFloatingRateReset(processingType: FloatingRateIndexProcessingTypeEnum->CompoundIndex): Call the compounded index processing logic to calculate the reset
* `EvaluateScreenRate`: Evaluate/lookup the value of a screen rate
* `DetermineResetDate`: Determine the value of the reset date given a reset dates structure and a calculation period for which it's needed. Reset dates are defined in the 2021 ISDA Definition in Section 6.5.5.
* `DetermineFixingDate`: Determine the observation (fixing) date needed given a reset dates structure and a reset date.
* `GetFloatingRateProcessingParameters`: Determine the processing parameters to use from the InterestRatePayout by looking them up if necessary from the corresponding schedules in the interest rate stream
* `SpreadAmount`: Look up the spread amount for a calculation period
* `MultiplierAmount`: Look up the multiplier amount for a calculation period
* `CapRateAmount`: Look up the cap rate amount for a calculation period
* `FloorRateAmount`: Look up the floor rate amount for a calculation period
* `LookupRateScheduleAmount`: Look up an amount for a calculation period from a rate schedule
* `FindScheduleSteps`: Find all rate schedule step values whose stepDate is before or equal to the supplied periodStartDate, starting from supplied startingStep number.  Returns a list of step values starting from the last matching one and going in reverse order.  Done this slightly odd way for efficiency and simplicity in code generation.  Assumes schedule step are in ascending date order.
* `SelectScheduleStep`: Retrieve a single step from a  schedule given a step number
* `ApplyFloatingRateProcessing`: Perform rate treatments on floating rates, such as applying spreads, multipliers, caps and floors, rounding, and negative interest treatment.  
* `ApplyFloatingRatePostSpreadProcessing`: Perform post-spread rate treatments on floating rates, such as applying caps and floors, rounding, and negative interest treatment.  
* `ApplyCapsAndFloors`: Apply any cap or floor rate as a constraint on a regular swap rate, as discussed in the 2021 ISDA Definitions, section 6.5.8 and 6.5.9
* `ApplyUSRateTreatment`: Apply the US rate treatment logic where applicable (Bond Equivalent Yield, Money Market Yield, as described in the 2021 ISDA Definitions, section 6.9.  (NB: this function does not have an implementation.)
* `ApplyFinalRateRounding`: Apply the final rate rounding treatment logic as described in the 2021 ISDA Definitions, section 4.8.1.

New functions in `cdm.observable.asset.calculatedrate` include:
* `EvaluateCalculatedRate`: Evaluate a calculated rate as described in the 2021 ISDA Definitions, Section 7
* `GenerateObservationDatesAndWeights`: Apply shifts to generate the list of observation dates and weights for each of those dates
* `ComputeCalculationPeriod`: Determine the calculation period to use for computing the calculated rate (it may not be the same as the normal calculation period, for instance if the rate is set in advance)
* `DetermineObservationPeriod`: Determine any applicable offsets/shifts for the period for observing an index, and then generate the date range to be used for observing the index, based on the calculation period, plus any applicable offsets/shifts
* `GenerateObservationPeriod`: Generate the date range to be used for observing the index, based on the calculation period, plus any applicable offsets/shifts.
* `GenerateObservationDates`: Generate the list of observation dates given an observation period
* `DetermineWeightingDates`: Determine the dates to be used for weighting observations
* `ProcessObservations`: Apply daily observation parameters to rate observation.  These are discussed in the 2021 ISDA Definitions, section 7.2.3 and 7.2.4.
* `GenerateWeights`: Recursively creates a list of weights based on the date difference between successive days.
* `ApplyCompoundingFormula`:  Implements the compounding formula:   Product of ( 1 + (rate * weight) / basis), then backs out the final rate. This is used to support section 7.3 of the 2021 ISDA Definitions.
* `ApplyAveragingFormula`: Implements the weighted arithmetic averaging formula.  Sums the weighted rates and divides by the total weight.  This is used to support section 7.4 of the 2021 ISDA Definitions.

ew functions in `cdm.observable.asset.fro` include:
* `IndexValueObservation`: Retrieve the values of the supplied index on the specified observation date.
*  `IndexValueObservationMultiple`: Retrieve the values of the supplied index on the specified observation dates.
*  `FloatingRateIndexMetadata`: Retrieve all available metadata for the floating rate index.
*  `ValidateFloatingRateIndexName`: Return whether the supplied floating rate index name is valid for the supplied contractual definitions.

New functions in `cdm.base.daycount.calculation` include:
* `YearFraction`: The fraction of a year represented by a date range
* `YearFractionForOneDay`: Return the year fraction represented by a single day, i.e. 1 / dayCountBasis, where dayCountBasis represents the denominator of the day count fraction. This perhaps should take into account leap years, though the ISDA compounding formulas do not cover ACT basis at the moment.
* `DayCountBasis`: Return the day count basis (the denominator of the day count fraction) for the day count fraction
