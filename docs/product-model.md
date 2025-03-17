---
title: Product Model
---

Where applicable, the CDM follows the data structure of the Financial
Products Markup Language (FpML), which is widely used in the OTC
Derivatives market. For example, the CDM type `PayerReceiver` is
equivalent to the FpML PayerReceiver.model. Both of these are data
structures used frequently throughout each respective model. In other
cases, the CDM data structure is more normalised, per Development Guidelines. For example, price and quantity are
represented in a single type, `TradableProduct`, which is shared by all
products. Another example is the use of a composable product model
whereby:

-   **Economic terms are specified by composition**, For example, the
    `InterestRatePayout` type is a component used in the definition of
    any product with one or more interest rate legs (e.g. Interest Rate
    Swaps, Equity Swaps, and Credit Default Swaps).
-   **Product qualification is inferred** from those economic terms
    rather than explicitly naming the product type, whereas FpML
    qualifies the product explcitly through the *product* substitution
    group.

Regardless of whether the data structure is the same or different from
FpML, the CDM includes defined Synonyms that map to FpML (and other
models) and can be used for transformation purposes. More details on
Synonyms are provided in the Mapping (Synonym) section of this document.

## TradableProduct {#tradable-product}

A tradable product represents a financial product that is ready to be
traded, meaning that there is an agreed financial product, price,
quantity, and other details necessary to complete an execution of a
security or a negotiated contract between two counterparties. Tradable
products are represented by the `TradableProduct` type.

``` Haskell
type TradableProduct:
   product Product (1..1)
   tradeLot TradeLot (1..*)
   counterparty Counterparty (2..2)
   ancillaryParty AncillaryParty (0..*)
   adjustment NotionalAdjustmentEnum (0..1)
```

---
**Note:**
The conditions for this data type are excluded from the snippet above
for purposes of brevity.

---

The primary set of attributes represented in the `TradableProduct` data
type are ones that are shared by all trades and transactions. For
example, every trade has a price, a quantity (treated jointly as a trade
lot), and a pair of counterparties. In some cases, there are ancillary
parties, or an allowable adjustment to the notional quantity. All of the
other attributes required to describe a product are defined in distinct
product data types.

## Counterparty

The `counterparty` attribute of a `TradableProduct` is constrained to be
exactly of cardinality 2. The CDM enforces that a transaction can only
occur between a pair of counterparties, with any other party involved in
the transaction represented by the `ancillaryParty` attribute.

The `counterparty` attribute uses the `Counterparty` data type, which
links a specific `Party` object identifying that party to its role in
the transaction. The counterparty roles in the CDM are normalised to be
either `Party1` or `Party2` and captured as a pair of enumerated values.

This design allows to use anonymised `Party1` and `Party2` values to
specify the direction of flows in the definition of a tradable product
without having to reference specific parties. This means that the same
product can now be defined in a party-agnostic way and used to represent
transactions between potentially many different parties.

``` Haskell
type Counterparty:
  role CounterpartyRoleEnum (1..1)
  partyReference Party (1..1)
    [metadata reference]
```

``` Haskell
enum CounterpartyRoleEnum:
  Party1
  Party2
```

``` Haskell
type Party:
  [metadata key]
  partyId PartyIdentifier (1..*)
  name string (0..1)
    [metadata scheme]
  businessUnit BusinessUnit (0..*)
  person NaturalPerson (0..*)
  personRole NaturalPersonRole (0..*)
  account Account (0..1)
  contactInformation ContactInformation (0..1)
```

---
**Note:**
The `partyReference` attribute in `Counterparty` is annotated with a
`[metadata reference]`, which means that a reference to the party object
can be passed in instead of a copy. In that case, the attribute's type
must itself be annotated with a `[metadata key]`, so that it is
referenceable via a key. The use of the key / reference mechanism is
further detailed in the Rosetta DSL documentation.

---

## TradeLot

A trade lot represents the quantity and price at which a product is
being traded.

In certain markets, trading the same product with the same economics
(except for price and quantity) and the same counterparty may be treated
as a separate trade. Each trade is represented by a tradable product
containing only 1 trade lot. In other markets, trading the same product
with the same characteristics (except for price and quantity) is
represented as part of the same trade. In this case, a single tradable
product contains multiple trade lots represented as an array of the
`TradeLot` data type.

When a trade can have multiple trade lots, increases (or upsize) and
decreases (or unwind) are treated differently. An increase adds a new
`TradeLot` instance to the tradadable product, whereas a decrease
reduces the quantity of one or more of the existing trade lots.

---
**Note:**
The term *lot* is borrowed from the Equity terminology that refers to
each trade lot as a *tax lot*, where the capital gains tax that may
arise upon unwind is calculated based on the price at which the lot was
entered.

---

For each trade lot, the quantity and price are represented by an
attribute called `priceQuantity`.

``` Haskell
type TradeLot:
  lotIdentifier Identifier (0..*)
  priceQuantity PriceQuantity (1..*)
```

The `pricequantity` attribute is represented as an array of the
`PriceQuantity` data type. For composite financial products that are
made of different legs, each leg may require its own price and quantity
attributes, and each instance of a `PriceQuantity` data type identifies
the relevant information for the leg of a trade. For example, for an
Interest Rate Swap, a trade lot would have one instance of the
`PriceQuantity` data type for each interest leg, and potentially a third
one for an upfront fee. By comparison, the purchase or sale of a
security or listed derivative would typically have a single
`PriceQuantity` instance in the trade lot.

## PriceQuantity {#price-quantity}

The price and quantity attributes of a trade, or of a leg of a trade in
the case of composite products, are part of a data type called
`PriceQuantity`. This data type also contains (optionally):

-   an observable, which describes the asset or reference index to which
    the price and quantity are related
-   settlement terms and the buyer/seller direction, in case that price
    and quantity are meant to be settled
-   a date, which indicates when these price and quantity become
    effective

``` Haskell
type PriceQuantity:
  [metadata key]
  price PriceSchedule (0..*)
    [metadata location]
  quantity NonNegativeQuantitySchedule (0..*)
    [metadata location]
  observable Observable (0..1)
  buyerSeller BuyerSeller (0..1)
  settlementTerms SettlementTerms (0..1)
  effectiveDate AdjustableOrRelativeDate (0..1)
```

---
**Note:**
The conditions for this data type are excluded from the snippet above
for purposes of brevity.

---

The price, quantity and observable attributes are joined together in a
single `PriceQuantity` data type because in some cases, those 3
attributes need to be considered together. For example, the return leg
of an Equity Swap will have:

-   the identifier for the shares as `observable`
-   the number of shares as `quantity`
-   the initial share price as `price`

However, those attributes are optional because in other cases, only some
of them will be specified. In the fixed leg of an Interest Rate Swap,
there is no observable as the rate is already fixed. An option trade
will contain an instance of a `PriceQuantity` containing only the
premium as price attribute, but no quantity or observable (the quantity
and/or observable for the option underlyer will be specified in a
different `PriceQuantity` instance).

Both the price and quantity can be specified as arrays in a single
`PriceQuantity`. All elements in the array express the same values but
according to different conventions. For example, the return leg of an
Equity Swap may specify both the number of shares and the notional (a
currency amount equal to: number of shares x price per share) as
quantities. In a Forward FX trade, the spot rate, forward points and
forward rate (equal to spot rate + forward points) may all be specified
as prices. When mutiple values are specified for either the price or
quantity attributes in a single `PriceQuantity` instance, they will be
tied by rules that enforce that they are internally consistent.

The effective date attribute is optional and will usually be specified
when a single trade has multiple trade lots, to indicate when each trade
lot become effective (usually on or around the date when the lot was
traded). The trade itself will have an effective date, corresponding to
the date when the first lot was traded and the trade opened.

The `price` and `quantity` attributes in the `PriceQuantity` data type
each have a metadata location which can reference a metadata address in
one of the `Payout` data types. The metadata address-location pair
allows for a reference to link objects without populating the address
object in persistence. This capability helps to support an agnostic
definition of the product in a trade (i.e. a product definition without
a price and quantity). However, the reference can be used to populate
values for an input into a function or for other purposes.

## Measure

A *measure* is a basic component that is useful in the definition of
price and quantity (both things that can be measured) and consists of
two attributes:

-   `value`, which is defined as a number and could be a price or a
    quantity
-   `unit`, which defines the unit in which that value is expressed

`MeasureBase` defines the basic structure of a measure in which both
attributes are optional. Various other data types that extend
`MeasureBase` can further constrain the existence of those attributes:
for instance, a `Measure` requires the `value` attribute to be present
(but `unit` is still optional because a measure could be unit-less).

``` Haskell
type MeasureBase:
  value number (0..1)
  unit UnitType (0..1)
```

``` Haskell
type Measure extends MeasureBase:

  condition ValueExists:
    value exists
```

The `UnitType` data type used to defined the `unit` attribute requires
the definition of units using one of five defined types:

``` Haskell
type UnitType:
  capacityUnit CapacityUnitEnum (0..1)
  weatherUnit WeatherUnitEnum (0..1)
  financialUnit FinancialUnitEnum (0..1)
  currency string (0..1)
    [metadata scheme]

  condition UnitType:
     one-of
```

A measure can vary over time. One often used case is a series of
measures indexed by date. Such measures are all homogeneous, so the unit
only needs to be represented once.

To represent this, the `MeasureSchedule` type extends `MeasureBase` with
a set of date and value pair attributes represented by the `DatedValue`
type. In that structure, the existing `value` attribute can still be
omitted but, when present, represents the schedule's initial value.

``` Haskell
type MeasureSchedule extends MeasureBase:
  datedValue DatedValue (0..*)

  condition ValueExists:
    value exists or datedValue exists
```

The price and quantity concepts for financial instruments are both
modelled as extensions of the `MeasureSchedule` data type, as detailed
below. This means that by default, price and quantity are considered as
schedules although they can also represent a single value when the
`datedValue` attribute is omitted.

## Price

The `PriceSchedule` data type extends the `MeasureSchedule` data type
with the addition of the `priceExpression` and `perUnitOf` attributes,
which together further qualify the price.

``` Haskell
type PriceSchedule extends MeasureSchedule:
  perUnitOf UnitType (0..1)
  priceType PriceTypeEnum (1..1)
  priceExpression PriceExpressionEnum (0..1)
  composite PriceComposite (0..1)
  arithmeticOperator ArithmeticOperationEnum (0..1)
  cashPrice CashPrice (0..1)
```

Note that the conditions for this data type are excluded from the
snippet above for purposes of brevity.

The `Price` data type further constrains the `PriceSchedule` data type
by requiring the `datedValue` attribute to be absent.

``` Haskell
type Price extends PriceSchedule:
  condition AmountOnlyExists:
    value exists and datedValue is absent
```

Consider the example below for the initial price of the underlying
equity in a single-name Equity Swap, which is a net price of 37.44 USD
per Share:

``` Javascript
"price": [
  {
    "value": {
      "value": 37.44,
      "unit": {
        "currency": {
          "value": "USD"
          }
        },
        "perUnitOf": {
          "financialUnit": "SHARE"
        },
        "priceExpression": {
          "priceType": "ASSET_PRICE",
          "grossOrNet": "NET"
        },
      },
      "meta": {
        "location": [
          {
            "scope": "DOCUMENT",
            "value": "price-1"
          }
        ]
      }
    }
  ]
```

The full form of this example can be seen by ingesting one of the
samples provided in the CDM distribution under products / equity /
eqs-ex01-single-underlyer-execution-long-form-other-party.xml. As can be
seen in the full example, for an interest rate leg, the `unit` and the
`perUnitOf` would both be a currency (e.g. 0.002 USD per USD). The
`priceType` would be an InterestRate and, in the case of a floating leg,
the `spreadType` would be a Spread.

## Quantity

The `QuantitySchedule` data type also extends the `MeasureSchedule` data
type with the addition of an optional `multiplier` attributes. It also
requires the `unit` attribute to exist, i.e. a quantity cannot be
unit-less. The `NonNegativeQuantitySchedule` data type further
constrains it by requiring that all the values are non-negative.

``` Haskell
type QuantitySchedule extends MeasureSchedule:
  multiplier Measure (0..1)
  frequency Frequency (0..1)

  condition Quantity_multiplier:
      if multiplier exists then multiplier -> value >= 0.0
  condition UnitOfAmountExists:
      unit exists
```

``` Haskell
type NonNegativeQuantitySchedule extends QuantitySchedule:

  condition NonNegativeQuantity_amount:
    if value exists then value >= 0.0 and
    if datedValue exists then datedValue -> value all >= 0.0
```

The inherited attributes of `value`, `unit` and `datedValue` (in case
the quantity is provided as a schedule) are sufficient to define a
quantity in most cases.

The additional `multiplier` attribute that is provided for the
`QuantitySchedule` data type allows to further qualify the `value`. This
is needed for listed contracts or other purposes, as shown below. In
this example, the trade involves the purchase or sale of 200 contracts
of the WTI Crude Oil futures contract on the CME. Each contract
represents 1,000 barrels, therefore the total quantity of the trade is
for 200,000 barrels.

``` Javascript
"quantity": [
  {
    "value": {
      "value": 200,
      "unit": {
        "financialUnit": "CONTRACT"
      },
      "multiplier": {
        "value": 1000,
        "unit": "BBL"
      }
    },
    "meta": {
      "location": [
        {
          "scope": "DOCUMENT",
          "value": "quantity-1"
        }
      ]
    }
  }
]
```

The `frequency` attribute is used in a similar way when a quantity may
be defined based on a given time period, e.g. per hour or per day. In
this case, the quantity needs to be multiplied by the size of the
relevant period where it applies, e.g. a number of days, to get the
total quantity.

## Observable

The `Observable` data type specifies the reference object to be observed
for a price, which could be an underlying asset or a reference such as
an index.

The Observable data type requires the specification of either a
`rateOption` (i.e. a floating rate index), `commodity`,
`productIdentifier`, or `currencypair`. This choice constraint is
supported by specifying a one-of condition, as shown below:

``` Haskell
type Observable:
  [metadata key]
  rateOption FloatingRateOption (0..1)
    [metadata location]
  commodity Commodity (0..1)
    [metadata location]
  productIdentifier ProductIdentifier (0..*)
    [metadata location]
  currencyPair QuotedCurrencyPair (0..1)
    [metadata location]
  optionReferenceType OptionReferenceTypeEnum (0..1)

  condition ObservableChoice:
    required choice rateOption, commodity, productIdentifier, currencyPair
```

## SettlementTerms

In both the Equity Swap and Interest Rate Swap trade cases mentioned
above, there are no settlement terms attached to the price and quantity.
Instead, any future settlement is driven by the product mechanics and
the price and quantity are just parameters in the definition of that
product.

In other cases, it is necessary to define settlement terms when either
the price or quantity or both are to be settled. A non-exhaustive list
of cases includes:

-   A cash transaction, i.e. when buying a certain quantity of a
    security or commodity for a certain price
-   An FX spot of forward transaction
-   An option for which a premium must be paid
-   A swap transaction that involves an upfront payment, e.g. in case of
    unwind or novation

In those cases, the corresponding `PriceQuantity` object also contains
`settlementTerms` and `buyerSeller` attributes to define that
settlement. The actual settlement amounts will use the price and
quantity agreed as part of the tradable product.

The `SettlementTerms` data type defines the basic characteristics of a
settlement: the settlement date, currency, whether it will be cash or
physical, and the type of transfer. For instance, a settlement could be
a *delivery-versus-payment* scenario for a cash security transaction or
a *payment-versus-payment* scenario for an FX spot or forward
transaction. Those parameters that are common across all settlement
methods are captured by the `SettlementBase` data type.

Cash and physical settlement methods require different, specific
parameters which are captured by the additional `cashSettlementTerms`
and `physicalSettlementTerms` attributes, respectively. For instance, a
non-deliverable FX forward will use the `cashSettlementTerms` attribute
to represent the parameters of the non-deliverable settlement, such as
the observable FX fixing to use.

``` Haskell
type SettlementTerms extends SettlementBase:
  cashSettlementTerms CashSettlementTerms (0..*)
  physicalSettlementTerms PhysicalSettlementTerms (0..1)
```

``` Haskell
type SettlementBase:
  [metadata key]
  settlementType SettlementTypeEnum (1..1)
  transferSettlementType TransferSettlementEnum (0..1)
  settlementCurrency string (0..1)
    [metadata scheme]
  settlementDate SettlementDate (0..1)
  settlementCentre SettlementCentreEnum (0..1)
  settlementProvision SettlementProvision (0..1)
  standardSettlementStyle StandardSettlementStyleEnum (0..1)
```

## BuyerSeller

When a settlement occurs for the price and/or quantity, it is necessary
to define the direction of that settlement by specifying which party
pays what. That direction is captured by the `BuyerSeller` data type,
that uses the normalised `CounterpartyRoleEnum` enumeration to specify
who is the buyer and seller, respectively.

``` Haskell
type BuyerSeller:
  buyer CounterpartyRoleEnum (1..1)
  seller CounterpartyRoleEnum (1..1)
```

By convention, the direction of the settlement flows will be inferred as
follows:

-   the buyer receives the quantity / pays the price, and
-   the seller receives the price / pays the quantity.

For instance in an FX spot or forward transaction, the respective units
of the quantity and price will determine who is paying or receiving each
currency.

## Financial Product

A financial product is an instrument that is used to transfer financial
risk between two parties. Financial products are represented in the
`Product` type, which is also constrained by a `one-of` condition,
meaning that for a single Tradable Product, there can only be one
Product.

``` Haskell
type Product:
  [metadata key]
  contractualProduct ContractualProduct (0..1)
  index Index (0..1)
  loan Loan (0..1)
  assetPool AssetPool (0..1)
  foreignExchange ForeignExchange (0..1)
  commodity Commodity (0..1)
    [metadata address "pointsTo"=Observable->commodity]
  security Security (0..1)
  basket Basket (0..1)

  condition: one-of
```

The CDM allows any one of these products to included in a trade or used
as an underlier for another product (see the *Underlier* section). One
unlikely case for a direct trade is Index, which is primarily used as an
underlier.

Among this set of products, the contractual product is the most
complicated and requires the largest data structure. In a contractual
product, an exchange of financial risk is materialised by a unique
bilateral contract that specifies the financial obligations of each
party. The terms of the contract are specified at trade inception and
apply throughout the life of the contract (which can last for decades
for certain long-dated products), unless amended by mutual agreement.
Contractual products are fungible (in other words, replaceable by other
identical or similar contracts) only under specific terms: e.g. the
existence of a close-out netting agreement between the parties.

Given that each contractual product transaction is unique, all of the
contract terms must be specified and stored in an easily accessible
transaction lifecycle model so that each party can evaluate the
financial and counterparty risks during the life of the agreement.

Foreign Exchange (FX) spot and forward trades (including Non-Deliverable
Forwards) and private loans also represent an exchange of financial risk
represented by a form of bilateral agreements. FX forwards and private
loans can have an extended term, and are generally not fungible.
However, these products share few other commonalities with contractual
products such as Interest Rate Swaps. Therefore, they are defined
separately.

By contrast, in the case of the execution of a security (e.g. a listed
equity), the exchange of finanical risk is a one-time event that takes
place on the settlement date, which is usually within a few business
days of the agreement. The other significant distinction is that
securities are fungible instruments for which the terms and security
identifiers are publically available. Therefore, the terms of the
security do not have to be stored in a transaction lifecycle model, but
can be referenced with public identifiers.

An Index product is an exception because it's not directly tradable,
but is included here because it can be referenced as an underlier for a
tradable product and can be identified by a public identifier.

## Contractual Product

The scope of contractual products in the current model are summarized
below:

-   **Interest rate derivatives**:
    -   Interest Rate Swaps (incl. cross-currency swaps, non-deliverable
        swaps, basis swaps, swaps with non-regular periods, ...)
    -   Swaptions
    -   Caps/floors
    -   FRAs
    -   OTC Options on Bonds
-   **Credit derivatives**:
    -   Credit Default Swaps (incl. baskets, tranche, swaps with
        mortgage and loans underlyers, ...)
    -   Options on Credit Default Swaps
-   **Equity derivatives**:
    -   Equity Swaps (single name)
-   **Options**:
    -   Any other OTC Options (incl. FX Options)
-   **Securities Lending**:
    -   Single underlyer, cash collateralised, open/term security loan
-   **Repurchase Agreements**:
    -   Open Term, Fixed Term, Fixed Rate, Floating Rate

In the CDM, contractual products are represented by the
`ContractualProduct` type:

``` Haskell
type ContractualProduct extends ProductBase:
   [metadata key]
   [metadata template]
   economicTerms EconomicTerms (1..1)
```

Note that price, quantity and counterparties are defined in
`TradableProduct` as these are attributes common to all products. The
remaining economic terms of the contractual product are defined in
`EconomicTerms` which is an encapsulated type in `ContractualProduct` .

## Economic Terms

The CDM specifies the various sets of possible remaining economic terms
using the `EconomicTerms` type. This type includes contractual
provisions that are not specific to the type of payout, but do impact
the value of the contract, such as effective date, termination date,
date adjustments, and early termination provisions. A valid population
of this type is constrained by a set of conditions which are not shown
here in the interests of brevity.

``` Haskell
type EconomicTerms:
  effectiveDate AdjustableOrRelativeDate (0..1)
  terminationDate AdjustableOrRelativeDate (0..1)
  dateAdjustments BusinessDayAdjustments (0..1)
  payout Payout (1..1)
  terminationProvision TerminationProvision (0..1)
  calculationAgent CalculationAgent (0..1)
  nonStandardisedTerms boolean (0..1)
  collateral Collateral (0..1)
```

## Payout

The `Payout` type defines the composable payout types, each of which
describes a set of terms and conditions for the financial
responsibilities between the contractual parties. Payout types can be
combined to compose a product. For example, an Equity Swap can be
composed by combining an `InterestRatePayout` and an
`PerformancePayout`.

``` Haskell
type Payout:
  [metadata key]
  interestRatePayout InterestRatePayout (0..*)
  creditDefaultPayout CreditDefaultPayout (0..1)
  optionPayout OptionPayout (0..*)
  commodityPayout CommodityPayout (0..*)
  forwardPayout ForwardPayout (0..*)
  fixedPricePayout FixedPricePayout (0..*)
  securityPayout SecurityPayout (0..*)
       [deprecated]
  cashflow Cashflow (0..*)
  performancePayout PerformancePayout (0..*)
  assetPayout AssetPayout (0..*)
```

A number of payout types extend a common data type called `PayoutBase`.
This data type provides a common structure for attributes such as
quantity, price, settlement terms and the payer/receiver direction which
are expected to be common across many payouts.

``` Haskell
type PayoutBase:
  payerReceiver PayerReceiver (1..1)
  priceQuantity ResolvablePriceQuantity (0..1)
  principalPayment PrincipalPayments (0..1)
  settlementTerms SettlementTerms (0..1)
```

The list of payouts that extend _PayoutBase_ are:

-   `InterestRatePayout`
-   `CreditDefaultPayout`
-   `OptionPayout`
-   `CommodityPayout`
-   `ForwardPayout`
-   `FixedPricePayout`
-   `SecurityPayout`
-   `Cashflow`
-   `PerformancePayout`
-   `AssetPayout`
-   the `ProtectionTerms` data type encapsulated in
    `CreditDefaultPayout`

For example:

``` Haskell
type InterestRatePayout extends PayoutBase:
   [metadata key]
   rateSpecification RateSpecification (0..1)
   dayCountFraction DayCountFractionEnum (0..1)
      [metadata scheme]
   calculationPeriodDates CalculationPeriodDates (0..1)
   paymentDates PaymentDates (0..1)
   paymentDate AdjustableDate (0..1)
   paymentDelay boolean (0..1)
   resetDates ResetDates (0..1)
   discountingMethod DiscountingMethod (0..1)
   compoundingMethod CompoundingMethodEnum (0..1)
   cashflowRepresentation CashflowRepresentation (0..1)
   stubPeriod StubPeriod (0..1)
   bondReference BondReference (0..1)
   fixedAmount calculation (0..1)
   floatingAmount calculation (0..1)
```

---
**Note:**
The code snippets above excludes the conditions in this data type for
purposes of brevity.

---

The price and quantity attributes in the _PayoutBase_
structure are positioned in the _ResolvablePriceQuantity_
data type. This data type mirrors the _PriceQuantity_ data
type and contains both the price and quantity schedules.

In addition that data type supports the definition of additional
information such as a quantity reference, a quantity multiplier or the
indication that the quantity is resettable. Those are used to describe
the quantity of a payout leg that may need to be calculated based on
other inputs: e.g. an exchange rate for the foreign leg in a
Cross-Currency Swap or a share price for the funding leg of an Equity
Swap.

``` Haskell
type ResolvablePriceQuantity:
  [metadata key]
  quantityCumulation CumulationFeature (0..*)
  resolvedQuantity Quantity (0..1)
  quantitySchedule NonNegativeQuantitySchedule (0..1)
    [metadata address "pointsTo"=PriceQuantity->quantity]
  quantityReference ResolvablePriceQuantity (0..1)
    [metadata reference]
  quantityMultiplier QuantityMultiplier (0..1)
  reset boolean (0..1)
  futureValueNotional FutureValueAmount (0..1)
  priceSchedule PriceSchedule (0..*)
    [metadata address "pointsTo"=PriceQuantity->price]
```

By design, the CDM requires that each payout leg can only be associated
with a single quantity schedule that defines this leg's contractual
behaviour (e.g. for the payment of cashflows). In the `PriceQuantity`
object, where that attribute is of multiple cardinality, other
quantities may be provided "for information only" which can be
inferred from the main quantity used in the payout leg: e.g. when a
commodity quantity is associated to a frequency and needs to be
multiplied by the period to get the total quantity.

Both the `quantitySchedule` and `priceSchedule` attributes have a
metadata address that point respectively to the `quantity` and `price`
attributes in the `PriceQuantity` data type. This special
cross-referencing annotation in the Rosetta DSL allows to parameterise
an attribute whose value may be variable by associating it to an
address. The attribute value does not need to be populated in the
persisted object and can be provided by another object, using the
address as a reference.

Other model structures use the `[metadata address]` to point to
`PriceQuantity->price`. An example include the `price` attribute in the
`RateSchedule` data type, which is illustrated below:

``` Haskell
type RateSchedule:
  price PriceSchedule (1..1)
    [metadata address "pointsTo"=PriceQuantity->price]
```

## Reusable Components

There are a number of components that are reusable across several payout
types. For example, the `CalculationPeriodDates` class describes the
inputs for the underlying schedule of a stream of payments.

``` Haskell
type CalculationPeriodDates:
  [metadata key]
  effectiveDate AdjustableOrRelativeDate (0..1)
  terminationDate AdjustableOrRelativeDate (0..1)
  calculationPeriodDatesAdjustments BusinessDayAdjustments (0..1)
  firstPeriodStartDate AdjustableOrRelativeDate (0..1)
  firstRegularPeriodStartDate date (0..1)
  firstCompoundingPeriodEndDate date (0..1)
  lastRegularPeriodEndDate date (0..1)
  stubPeriodType StubPeriodTypeEnum (0..1)
  calculationPeriodFrequency CalculationPeriodFrequency (0..1)
```

## Underlier

The underlier attribute on types `OptionPayout`, `ForwardPayout` and
`EquityPayout` allows for any product to be used as the underlier for a
corresponding products option, forward, and equity swap.

``` Haskell
type OptionPayout extends PayoutBase:
  [metadata key]
  buyerSeller BuyerSeller (1..1)
  optionType OptionTypeEnum (0..1)
  feature OptionFeature (0..1)
  exerciseTerms OptionExercise (1..1)
  underlier Product (1..1)
```

This nesting of the product component is another example of a composable
product model. One use case is an interest rate swaption for which the
high-level product uses the `OptionPayout` type and underlier is an
Interest Rate Swap composed of two `InterestRatePayout` types.
Similiarly, the product underlying an Equity Swap composed of an
`InterestRatePayout` and an `EquityPayout` would be a non-contractual
product: an equity security.

## Data Templates

The `ContractualProduct` type is specified with the
`[metadata template]` annotation indicating that it is eligible to be
used as a template.

Financial markets often trade a high volume of trades with near
identical contractual product data. Templates provide a way to store
this data more efficiently. The contractual product data which is
duplicated on each contract can be extracted into a single template and
replaced by a reference. This allows each trade to specify only the
unique contractual product data. The template reference can be resolved
to a template object which can then be merged in to form a single,
complete object.

For instance, Equity Swaps used by Equity Financing desks sometimes
refer to a *Master Confirmation* agreement, which is an overall
agreement that specifies all the standard Equity Swap terms that do not
need to be renegotiated on each trade. Each contractual product would
only specify the unique product details (such as start and end date,
underlier, price and spread) together with a reference to the Master
Confirmation containing the template product details.

Code libraries, written in Java and distributed with the CDM, contain
tools to merge CDM objects together. Implementors may extend these
merging tools to change the merging strategy to suit their requirements.
The CDM Java Examples download, available via the [CDM Portal Downloads
page](https://portal.cdm.rosetta-technology.io/#/downloads), contains a example demonstrating usage of a data template and
the merging tools. See
`com.regnosys.cdm.example.template.TemplateExample`.

## Products with Identifiers

The abstract data type ProductBase serves as a base for all products
that have an identifier, as illustrated below:

``` Haskell
type ProductBase:
  productTaxonomy ProductTaxonomy (0..*)
  productIdentifier ProductIdentifier (0..*)
```

The data types that extend from ProductBase are Index, Commodity, Loan,
and Security. Index and Commodity do not have any additional attributes.
In the case of Commodity, the applicable product identifiers are the
ISDA definitions for reference benchmarks. Loan and Security both have a
set of additional attributes, as shown below:

``` Haskell
type Loan extends ProductBase:
  borrower LegalEntity (0..*)
  lien string (0..1)
    [metadata scheme]
  facilityType string (0..1)
    [metadata scheme]
  creditAgreementDate date (0..1)
  tranche string (0..1)
    [metadata scheme]
```

``` Haskell
type Security extends ProductBase:
  securityType SecurityTypeEnum (1..1)
  debtType DebtType (0..1)
  equityType EquityTypeEnum (0..1)
  fundType FundProductTypeEnum (0..1)
  economicTerms EconomicTerms (0..1)

condition DebtSubType:
  if securityType <> SecurityTypeEnum -> Debt
  then debtType is absent

condition EquitySubType:
  if securityType <> SecurityTypeEnum -> Equity
  then equityType is absent

condition FundSubType:
  if securityType <> SecurityTypeEnum -> Fund
  then fundType is absent
```

The product identifier will uniquely identify the security. The
`securityType` is required for specific purposes in the model, for
example for validation as a valid reference obligation for a Credit
Default Swap. The additional security details are optional as these
could be determined from a reference database using the product
identifier as a key

# Product Qualification

**Product qualification is inferred from the economic terms of the
product** instead of explicitly naming the product type. The CDM uses a
set of Product Qualification functions to achieve this purpose. These
functions are identified with a `[qualification Product]` annotation.

A Product Qualification function applies a taxonomy-specific business
logic to identify if the product attribute values, as represented by the
product's economic terms, match the specified criteria for the product
named in that taxonomy. For example, if a certain set of attributes are
populated and others are absent, then that specific product type is
inferred. The Product Qualification function name in the CDM begins with
the word `Qualify` followed by an underscore `_` and then the product
type from the applicable taxonomy (also separated by underscores).

The CDM implements the ISDA Product Taxonomy v2.0 to qualify contractual
products, foreign exchange, and repurchase agreements. Given the
prevalence of usage of the ISDA Product Taxonomy v1.0, the equivalent
name from that taxonomy is also systematically indicated in the CDM,
using a `synonym` annotation displayed under the function output. An
example is provided below for the qualification of a Zero-Coupon
Fixed-Float Inflation Swap:

``` Haskell
func Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon:
  [qualification Product]
  inputs: economicTerms EconomicTerms (1..1)
  output: is_product boolean (1..1)
    [synonym ISDA_Taxonomy_v2 value "InterestRate_IRSwap_Inflation"]
  set is_product:
    Qualify_BaseProduct_Inflation(economicTerms) = True
    and Qualify_BaseProduct_CrossCurrency( economicTerms ) = False
    and Qualify_SubProduct_FixedFloat(economicTerms) = True
    and Qualify_Transaction_ZeroCoupon(economicTerms) = True
```

If all the statements above are true, then the function evaluates to
True, and the product is determined to be qualified as the product type
referenced by the function name.

---
**Note:**
In a typical CDM model implementation, the full set of Product
Qualification functions would be invoked against each instance of the
product in order to determine the inferred product type. Given the
product model composability, a single product instance may be qualified
as more than one type: for example in an Interest Rate Swaption, both
the Option and the underlying Interest Rate Swap would be qualified.

---

The CDM supports Product Qualification functions for Credit Derivatives,
Interest Rate Derivatives, Equity Derivatives, Foreign Exchange, and
Repurchase Agreements. The full scope for Interest Rate Products has
been represented down to the full level of detail in the taxonomy. This
is shown in the example above, where the `ZeroCoupon` qualifying suffix
is part of the function name. Credit Default products are qualified, but
not down to the full level of detail. The ISDA Product Taxonomy v2.0
references the FpML *transaction type* field instead of just the product
features, whose possible values are not publicly available and hence not
positioned as a CDM enumeration.

The output of the qualification function is used to populate the
`productQualifier` attribute of the `ProductTaxonomy` object, which is
created when a `ContractualProduct` object is created. The product
taxonomy includes both the product qualification generated by the CDM
and any additional product taxonomy information which may come from the
originating document, such as FpML. In this case, taxonomy schemes may
be associated to such product taxonomy information, which are also
propagated in the `ProductTaxonomy` object.

Many different financial taxonomies may be used by various segments of
the financial industry to describe the same product. To support a
multitude of taxonomies without adding any specific identity to data
types in the model, a Taxonomy type is used to identify the source and
attributes any particular taxonomy structure.

``` Haskell
type Taxonomy:
   source TaxonomySourceEnum (0..1)
   value TaxonomyValue (0..1)
```

`TaxonomyValue` has been expanded to represent a complex type:

``` Haskell
type TaxonomyValue: 

   name string (0..1)
       [metadata scheme]
   classification TaxonomyClassification (0..*)

   condition ValueExists:
       name exists or classification exists
```

`TaxonomyClassification` is also a complex type that support a
hierarchical structure of any depth:

``` Haskell
type TaxonomyClassification:
    className string (0..1)
    value string (1..1)
    description string (0..1)
    ordinal int (0..1)
```

The `ProductTaxonomy` data structure and an instance of a CDM object
([serialised](https://en.wikipedia.org/wiki/Serialization) into JSON) are shown below:

``` Haskell
type ProductTaxonomy extends Taxonomy:
    primaryAssetClass AssetClassEnum (0..1)
        [metadata scheme]
    secondaryAssetClass AssetClassEnum (0..*)
        [metadata scheme]
    productQualifier string (0..1)

    condition TaxonomyType:
        required choice source, primaryAssetClass, secondaryAssetClass

    condition TaxonomySource:
        if source exists then ( value exists or productQualifier exists )

    condition TaxonomyValue:
        optional choice value, productQualifier
```

``` Javascript
"productTaxonomy": [
  {
    "primaryAssetClass": {
      "meta": {
        "scheme": "http://www.fpml.org/coding-scheme/asset-class-simple"
      },
      "value": "INTEREST_RATE"
    },
  },
  {
    "taxonomyValue": {
      "meta": {
        "scheme": "http://www.fpml.org/coding-scheme/product-taxonomy"
      },
      "value": "InterestRate:IRSwap:FixedFloat"
    }
    "taxonomySource": "ISDA"
  },
  {
    "productQualifier": "InterestRate_IRSwap_FixedFloat",
    "taxonomySource": "ISDA"
  }
]
```

# Listing

`exchange` and `relatedExchange` are attributes that refer to the financial markets where the asset is listed. Specifically, their definitions are as follows:
- Exchange refers to the principal financial market where the asset is listed. This is the main venue for the buying and selling of the asset, such as a stock exchange for equities or a futures exchange for commodities.
- Related Exchange denotes any additional exchange or trading platform where the asset is listed. For instance, if the asset is a particular stock, the related exchange might be the exchange where the stock is listed.

These attributes help identify the platforms on which the asset is listed.

`Listing` is an intermediate type created to add both `exchange` and `relatedExchange` attributes to `Commodity` and `Security` assets, as they are the only types that can be a listed on an exchange.

``` Haskell
type Listing extends ProductBase:
    exchange LegalEntity (0..1)
    relatedExchange LegalEntity (0..*)
   
    condition RelatedExchange:
        if exchange is absent then relatedExchange is absent
```
The following snippets show that `Commodity` and `Security` are extended by `Listing`:
``` Haskell
type Commodity extends Listing:
    commodityProductDefinition CommodityProductDefinition (0..1) 
    priceQuoteType QuotationSideEnum (1..1) 
    deliveryDateReference DeliveryDateParameters (0..1) 
    description string (0..1) 
```

``` Haskell
type Security extends Listing:
    securityType SecurityTypeEnum (1..1)
    debtType DebtType (0..1)
    equityType EquityTypeEnum (0..1)
    fundType FundProductTypeEnum (0..1)
    economicTerms EconomicTerms (0..1)
```

---
**Note:**
The code snippets above exclude the conditions in these data types for
purposes of brevity.

---