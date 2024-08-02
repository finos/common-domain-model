---
title: Product Model
---

## Financial Products {#product}

A financial product is an instrument that is used to transfer financial
risk between two parties. 

Within the CDM, a financial product is composed of several building blocks 
to define the characteristics of the product that is traded between the parties.
The most fundamental of these is an `Asset` which is used to represent the 
basic transferable financial products such as cash, commodities or securities.

The CDM also supports negotiated products, where the terms of the
product are agreed between the parties, as is typically seen in the OTC 
derivatives markets. The concept of `EconomicTerms` enables the modelling
of these products through the use of composable `Payouts` which define the
conditions of future cashflows that might be driven by terms such as 
interest rates, options, or equity performance. 

### Asset  {#asset}

:::tip Definition: Asset

An Asset is defined as something that can be owned and transferred in the financial markets, for example, cash, a 
commodity, a loan or a security. As a choice data type, one and only one of the attributes must be used.

:::

The Asset data type is a `choice`:

``` Haskell
choice Asset:  
    Cash
    Commodity
    DigitalAsset
    Instrument
```

Each of the choice values are defined as data types within the model and each also extends a common base type
`AssetBase`:

``` Haskell
type AssetBase: 
    identifier AssetIdentifier (1..*) 
    taxonomy Taxonomy (0..*) 
    isExchangeListed boolean (0..1) 
    exchange LegalEntity (0..1)  
    relatedExchange LegalEntity (0..*) 
```

The data types are designed to carry the minimal amount of information that is needed to uniquely identify the asset
in question. 

The base type ensures that every instance of all types of an `Asset` has a defined `AssetIdentifier` which is
itself composed of an `identifier` and an `identifierType` enumerator that defines the symbology source of the identifier,
for example a CUSIP or ISIN.

The base type also includes an optional `taxonomy` which aligns the asset to one of the asset classification
sources defined by industry organisations, such as ISDA, or regulators, such as CFTC or EMIR. It is also possible
to define the exchange listing characteristics of the asset.

Conditions are applied on each of the asset types to enforce certain rules; for example, a `Cash` asset
can not have an `exchange`.

The `Asset` definitions are as follows:

* **Cash**: An asset that consists solely of a monetary holding in a currency.  The only attribute on this
  data type is the `Identifier`, populated with the currency code (using the `CurrencyCodeEnum` list) for the currency
  of the cash.
* **Commodity**: An Asset comprised of raw or refined materials or agricultural products, eg gold, oil or wheat.
  The applicable identifiers are the ISDA definitions for reference benchmarks. If no such benchmark exists, the
  characteristics of a commodity asset can be more fully identified using a `CommodityProductDefinition`.
* **DigitalAsset**: An Asset that exists only in digital form, eg Bitcoin or Ethereum; excludes the digital representation
  of other Assets.
* **Instrument**: An asset that is issued by one party to one or more others; Instrument is also a choice data type.

The `Instrument` data type is further broken down using the `choice` construct:

``` Haskell
choice Instrument:  
    ListedDerivative 
    Loan
    Security
```

with these attributes:
* **ListedDerivative**: A securitized derivative on another asset that is created by an exchange.  If the particular
  contract cannot be fully identified using the `identifier`, the optional `deliveryTerm`, `optionType` and
  `strike` attributes can be populated.
* **Loan**: An Asset that represents a loan or borrow obligation. As loans rarely have standard industry identifiers,
  the data type includes optional attributes to help uniquely identify the loan, including `borrower`, `lien`,
  `facilityType`, `creditAgreementDate` and `tranche`.
* **Security**: An Asset that is issued by a party to be held by or transferred to others. As "security" covers a
  broad gamut of assets, the `securityType` attribute (which is a list of enumerators including "Debt" and "Equity")
  must always be specified. Further categorisation, by `debtType`, `equityType` and `FundType`, can also be used
  and are governed by conditions on the data type definition.

### Observable

In addition to assets, there are other parameters which can be observed in the markets and which can directly
influence the pricing or outcomes of financial products. In the CDM, these are termed "Observables":

:::tip Definition: Observable

The `Observable` data type specifies the reference object to be observed
for a price, which could be an underlying asset or a reference to something
which can be observed but not actually transferred, such as an index or
a foreign exchange rate.

:::

In addition to `Asset` (something that can be held or transferred in the 
financial markets), the `Observable` is a choice data type composed as follows:

``` Haskell
choice Observable:
    Asset 
    Basket 
    Index  
```

The definition of these attributes is as follows:

* **Asset**:  The inclusion of Asset in Observable enables the price of an asset to be included within the definition of another
  financial product.
* **Basket**:  The object to be observed is a Basket, ie a collection of Observables with an identifier and optional weightings.
* **Index**:  The object to be observed is an Index, ie an observable computed on the prices, rates or valuations of a number of assets.

The CDM allows both Assets and Observables to included in a trade or used
as an underlier for another product (see the *[Underlier](#underlier)* section). One
unlikely case for a direct trade is Index, which is primarily used as an underlier.

### Contractual Financial Products

A contractual financial product is a bilateral agreement negotiated between two
parties and is represented in the CDM with the `NonTransferableProduct` type. 
(Its name is in direct contrast to `TransferableProduct` which is explained shortly.)

Compared with Asset and Observable, the modelling of a contractual financial
product is complicated and requires the largest data structure. In a contractual
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

By contrast, in the case of the execution of a security (e.g. a listed
equity), the exchange of finanical risk is a one-time event that takes
place on the settlement date, which is usually within a few business
days of the agreement. The other significant distinction is that
securities are fungible instruments for which the terms and security
identifiers are publically available. Therefore, the terms of the
security do not have to be stored in a transaction lifecycle model, but
can be referenced with public identifiers.

An index-based product is an exception because it's not directly tradable,
but is included here because it can be referenced as an underlier for a
tradable product and can be identified by a public identifier.

### Contractual Product Scope

The scope of contractual financial products in the current model are summarized
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
        mortgage and loan underliers, ...)
    -   Options on Credit Default Swaps
-   **Equity derivatives**:
    -   Equity Swaps (single name)
-   **Options**:
    -   Any other OTC Options (incl. FX Options)
-   **Securities Lending**:
    -   Single underlyer, cash collateralised, open/term security loan
-   **Repurchase Agreements**:
    -   Open Term, Fixed Term, Fixed Rate, Floating Rate

### NonTransferableProduct

In the CDM, contractual financial products are represented by the
`NonTransferableProduct` type:

:::tip Definition: NonTransferableProduct

A data type to specify the financial product's economic terms, alongside the product identification and product taxonomy. 
The non-transferable product data type represents a product that can be traded (as part of a TradableProduct) but cannot 
be transferred to others.  It is meant to be used across the pre-execution, execution and (as part of the Contract) 
post-execution lifecycle contexts.

:::

``` Haskell
type NonTransferableProduct:  
    identifier ProductIdentifier (0..*) 
    taxonomy ProductTaxonomy (0..*) 
    economicTerms EconomicTerms (1..1)
```

The definition of these attributes is as follows:

* **identifier**: As is the case for Assets, a product must have a unique identifier composed
  of an `Identifier` string and a `ProductIdTypeEnum`.
* **taxonomy**:  Defines the primary and secondary Asset Classes using the `AssetClassEnum`
  which leverages the FpML classifications.
* **economicTerms**:  Defines the price forming features.

### Economic Terms

:::tip Definition: EconomicTerms

Represents the full set of features associated with a product: the payout component; 
the notional/quantity; the effective date, termination date and the date adjustment 
provisions which apply to all payouts. This data type also includes the legal provisions 
which have valuation implications: cancelable provision; extendible provision; early 
termination provision; and extraordinary events specification.  It defines all the 
commitments between the parties to pay or transfer during the life of the trade.

:::

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

### Payout

The `Payout` type defines the composable payout types, each of which
describes a set of terms and conditions for the financial
responsibilities between the contractual parties. Payout types can be
combined to compose a product. For example, an Equity Swap can be
composed by combining an `InterestRatePayout` and an
`PerformancePayout`.

:::tip Definition: Payout

Represents the set of future cashflow methodologies in the form of 
specific payout data type(s) which result from the financial product.  
Examples: a trade in a cash asset will use only a settlement payout; 
for derivatives, two interest rate payouts can be combined to specify 
an interest rate swap; one interest rate payout can be combined with 
a credit default payout to specify a credit default swap.

:::

``` Haskell
type Payout:
  [metadata key]
  interestRatePayout InterestRatePayout (0..*)
  creditDefaultPayout CreditDefaultPayout (0..1)
  optionPayout OptionPayout (0..*)
  commodityPayout CommodityPayout (0..*)
  settlementPayout SettlementPayout (0..*)
  fixedPricePayout FixedPricePayout (0..*)
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
-   `SettlementPayout`
-   `FixedPricePayout`
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

### TransferableProduct

We saw earlier that the data type `NonTransferableProduct` is used to define
a negotiated contractual financial product between two parties and that this
is done by modelling the `EconomicTerms` and `Payouts` agreen between them.

This is in contrast with an `Asset` which is something that is transferable
and not uniquely negotiated. The `Asset` data type provides for the identification
of the financial product using publicly available identifiers.

Sometimes the identifiers are not sufficient and there is a need to add the
financial terms and cashflows.  This is facilitated through the use of the
`TransferableProduct` data type.

:::tip Definition: TransferableProduct

A TransferableProduct is a type of Product which can be used in a 
SettlementPayout for a basic cash settled trade of either an Asset
with or without the addition of specific EconomicTerms.

:::

``` Haskell
type TransferableProduct extends Asset:
    economicTerms EconomicTerms (1..1)
```

## TradableProduct {#tradable-product}

A tradable product represents a financial product that is ready to be
traded, meaning that there is an agreed financial product, price,
quantity, and other details necessary to complete an execution of a
security or a negotiated contract between two counterparties. Tradable
products are represented by the `TradableProduct` data type.

:::tip Definition: TradableProduct

Definition of a financial product as ready to be traded, i.e. included in 
an execution or contract, by associating a specific price and quantity to
the product definition, plus optional conditions for any potential future
quantity adjustment. A tradable product does not define the trade date,
execution details or other information specific to the execution event
but allows for multiple trade lots, quantities and prices, between the 
same two counterparties.

:::

``` Haskell
type TradableProduct:
   product NonTransferableProduct (1..1)
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

### Counterparty

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

### TradeLot

A trade lot represents the quantity and price at which a product is
being traded.

In certain markets, trading the same product with the same economics
(except for price and quantity) and the same counterparty may be treated
as a separate trade. Each trade is represented by a tradable product
containing only 1 trade lot. In other markets, trading the same product
with the same characteristics (except for price and quantity) is
represented as part of the same trade. In this case, a single tradable
product contains mulle trade lots represented as an array of the
`TradeLot` data type.

When a trade can have mulle trade lots, increases (or upsize) and
decreases (or unwind) are treated differently. An increase adds a new
`TradeLot` instance to the tradadable product, whereas a decrease
reduces the quantity of one or more of the existing trade lots.

:::tip Definition: TradeLot

Specifies the prices and quantities of one or more trades, where the 
same product could be traded multiple times with the same counterparty 
but in different lots (at a different date, in a different quantity and 
at a different price). One trade lot combined with a product definition 
specifies the entire economics of a trade. The lifecycle mechanics of each 
such trade lot (e.g. cashflow payments) is independent of the other lots. 
In a trade decrease, the existing trade lot(s) are decreased of the 
corresponding quantity (and an unwind fee may have to be settled).

:::

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
as prices. When mule values are specified for either the price or
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

### Measure

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

### Price

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

### Quantity

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

### SettlementTerms

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

### BuyerSeller

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

### Underlier

The underlier attribute on types `OptionPayout`, `ForwardPayout` and
`EquityPayout` allows for any product to be used as the underlier for a
corresponding products option, forward, and equity swap.

``` Haskell
type OptionPayout extends PayoutBase:
  [metadata key]
  buyerSeller BuyerSeller (1..1)
  feature OptionFeature (0..1)
  observationTerms ObservationTerms (0..1)
  schedule CalculationSchedule (0..1)
  delivery AssetDeliveryInformation (0..1)
  underlier Product (1..1)
  optionType OptionTypeEnum (0..1)
  exerciseTerms ExerciseTerms (1..1)
  strike OptionStrike (0..1)
```

This nesting of the product component is another example of a composable
product model. One use case is an interest rate swaption for which the
high-level product uses the `OptionPayout` type and underlier is an
Interest Rate Swap composed of two `InterestRatePayout` types.
Similiarly, the product underlying an Equity Swap composed of an
`InterestRatePayout` and an `EquityPayout` would be a non-contractual
product: an equity security.

### Data Templates

The `NonTransferableProduct` type is specified with the
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
page](https://cdm.finos.org/docs/download/), contains a example demonstrating usage of a data template and
the merging tools. See
`com.regnosys.cdm.example.template.TemplateExample`.

### Identifiers

#### Asset Identifiers

The abstract data type AssetBase serves as a base for all Assets,
as illustrated below:

``` Haskell
type AssetBase:
    identifier AssetIdentifier (1..*) 
    taxonomy Taxonomy (0..*) 
    isExchangeListed boolean (0..1)
    exchange LegalEntity (0..1)  
    relatedExchange LegalEntity (0..*)
```

The data types that extend from AssetBase are the Asset
data types (Cash, Commodity, DigitalAsset and Loan) and
the Observable data types (Basket and Index).

Additionally, the Instrument data types extend from InstrumentBase,
which itself extends from AssetBase.

The instrument assets also have their own definitions with additional attributes
which are required to uniquely identify the asset:

``` sourcecode
type Loan extends InstrumentBase:
    borrower LegalEntity (0..*)
    lien string (0..1)
        [metadata scheme]
    facilityType string (0..1)
        [metadata scheme]
    creditAgreementDate date (0..1)
    tranche string (0..1)
        [metadata scheme]

type ListedDerivative extends InstrumentBase:
    deiveryTerm string (1..1)
    optionType PutCallEnum (0..1)
    strike number (0..1)

condition Options:
     if optionType exists then strike exists else strike is absent
```

#### Product Identifiers

The abstract data type ProductBase serves as a base for all products
that have an identifier, as illustrated below:

``` Haskell
type ProductBase:
  productTaxonomy ProductTaxonomy (0..*)
  economicTerms EconomicTerms (1..1)
```

The data types that extend from ProductBase are Commodity 
and Security. 

---
**Note:**
The conditions for this data type are excluded from the snippet above
for purposes of brevity.

---

---
**Note:**
This inheritance and remodelling will be refactored
before CDM 6 is released to production.

---

In the case of Commodity, the applicable product identifiers are the
ISDA definitions for reference benchmarks. Security has a
set of additional attributes, as shown below:

``` Haskell
type Security extends InstrumentBase:
  securityType SecurityTypeEnum (1..1)
  debtType DebtType (0..1)
  equityType EquityTypeEnum (0..1)
  fundType FundProductTypeEnum (0..1)
  economicTerms EconomicTerms (0..1)
  productTaxonomy ProductTaxonomy (0..*)

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

## Product Qualification

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
created when a `NonTransferableProduct` object is created. The product
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
