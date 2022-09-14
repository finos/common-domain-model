# *Product Model - Principal Payments*

_Background_

This release refactors the data types and attributes used to specify Principal Payments in the CDM.

_What is being released?_

Data type `PrincipalExchanges` has been renamed `PrincipalPayments` and added to `PayoutBase` so that all Payouts that extend `PayoutBase` can describe Principal Payment features.  These features define whether intial, intermediate and final exchanges should occur, using a boolean attribute.  The timing, value and direction of initial and final payments.  And a schedule of intermediate payments.

A new attribute `principalPaymentSchedule` has been added to `PrincipalPayments` allowing the definition of initial and final principal payments, and a schedule of intermediate principal payments.

Data type `PrincipalExchange` has been renamed `PrincipalPayment` and refactored to explicitly define the direction of movement using a `payerReceiver` attribute, and the amount of principal to be paid, defined using the complex type `Money`.

The attribute `principalExchange` has been removed from data type `CashflowRepresentation`.

_Review Directions_
 
In the CDM Portal, select Textual Browser and view the data types and attributes identified above.
In the CDM Portal, select Ingestion and view the following example trade to review the new model structure:

- fpml-5-10 > products > rates > ird-ex06-xccy-swap-uti.xml 

# *Event Model - BusinessEvent and Transfer Intent Enumerations*

_Background_

The Enumeration lists used to specify the intent of a `BusinessEvent` and `Transfer` have been updated to enable a greater set of scenarios to be described using the CDM.

_What is being released_

The enumeration list `EventIntentEnum` has been extended to allow declarative indication of intent for additional types of Business Events.  A new enumeration list `CorporateActionTypeEnum` has been added to identify the specific nature of a Corporate Action.  The enumeration list `TransferTypeEnum` has been updated to allow declarative indication of additional Transfer Types.

The following changes have been made to `EventIntentEnum`:

Enumerations added:

- CashFlow
- PrincipalExchange
- CorporateActionAdjustment
- ObservationRecord
- NotionalStep
- NotionalReset
- EarlyTermination
- OptionalExtension
- OptionalCancellation
- Decrease

Enumerations updated:

- ContractTermsAmendment (previously Renegotiation)
- OptionExercise (previously Exercise)

Enumerations removed:

- StockSplit

`CorporateActionTypeEnum` has been created containing the following enumerations:

- CashDividend
- StockDividend
-	StockSplit
-	ReverseStockSplit
-	SpinOff
-	Merger
-	Delisting
-	StockNameChange
-	StockIdentifierChange
-	RightsIssue

The following changes have been made to TransferTypeEnum:

Enumerations added:
- FixedRateReturn
- FloatingRateReturn
- FractionalAmount

Enumerations updated:
- PrincipalPayment (previously PrincipalExchange)

Enumerations removed:
- Interest

_Review Directions_
 
In the CDM Portal, select Textual Browser and view the enumerations identified above.
