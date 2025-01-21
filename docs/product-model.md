---
title: Product Model
---

## Financial Product {#product}

In the CDM, a financial product describes a thing that is used to transfer financial
risk between two parties. 

The model is based on several building blocks to define the characteristics of that risk transfer.
The most fundamental of these building blocks is an `Asset`, which represents 
a basic, transferable financial product such as cash, a commodity or security.
From those basic transferable assets, any other financial product can be built using
other composable building blocks called `Payout` that are assembled to represent the 
complete `EconomicTerms` of that product.
A `Payout` is a parametric description of the commitment between two parties to the transfer
of one or more assets in the future - for instance, but not exclusively, future cashflows
when that asset is cash. These future transfers may be contingent on the future value
or performance of that asset or other, as in the case of options.

### Asset  {#asset}

:::tip Definition: Asset

An Asset is defined as something that can be held by one party and is transferable from one party to another: for example, cash, a 
commodity, a loan or a security.

:::
`[![](/assets/ART-asset.png)](img/ARTasset.png)`
[![](/assets/ART-asset.png)](img/ARTasset.png)

The Asset data type is represented as a `choice` of several underlying data types, which means one and only one of those data types must be used.

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
sources defined by industry organisations, such as ISDA, or regulators, such as CFTC or ESMA. It is also possible
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

In the case of `Commodity`, the applicable product identifiers are the ISDA definitions for reference benchmarks. 

#### Instrument  {#instrument}

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
  broad range of assets, the `securityType` attribute (which is a list of enumerators including `Debt` and `Equity`)
  must always be specified. Further categorisation, by `debtType`, `equityType` and `FundType`, can also be used
  and are governed by conditions on the data type definition.

All the `Instrument` data types extend `InstrumentBase`, which itself extends `AssetBase`.
Each type of instrument also has its own definitions with additional attributes which are required to uniquely
identify the asset.

The additional attributes on `Loan` can be used when needed to uniquely identify the specific loan: 

``` Haskell
type Loan extends InstrumentBase:
    borrower LegalEntity (0..*)
    lien string (0..1)
        [metadata scheme]
    facilityType string (0..1)
        [metadata scheme]
    creditAgreementDate date (0..1)
    tranche string (0..1)
        [metadata scheme]
```

Likewise, additional `ListedDerivative` attributes are used to uniquely identify the contract:

``` Haskell
type ListedDerivative extends InstrumentBase:
    deliveryTerm string (0..1)
    optionType PutCallEnum (0..1)
    strike number (0..1)

    condition Options:
      if optionType exists then strike exists else strike is absent
```

---
**Note:**
The conditions for this data type are excluded from the snippet above
for purposes of brevity.

---

Security has a set of additional attributes, as shown below:

``` Haskell
type Security extends InstrumentBase: 
    debtType DebtType (0..1)
    equityType EquityTypeEnum (0..1) 
    fundType FundProductTypeEnum (0..1)

    condition DebtSubType:
        if instrumentType <> InstrumentTypeEnum -> Debt
        then debtType is absent

    condition EquitySubType:
        if instrumentType <> InstrumentTypeEnum -> Equity
        then equityType is absent

    condition FundSubType:
        if instrumentType <> InstrumentTypeEnum -> Fund
        then fundType is absent
```

The asset identifier will uniquely identify the security. The
`securityType` is required for specific purposes in the model, for
example for validation as a valid reference obligation for a Credit
Default Swap. The additional security details are optional as these
could be determined from a reference database using the asset
identifier as a key.

### Index   {#index}

dssd

:::tip Definition: Index

An `Index` is data type to record information about prices, rates or 
valuations of a number of assets that are tracked in a standardized way.  
Examples include equity market indices as well as indices on interest rates, 
foreign exchange rates, inflation and credit instruments.

:::

The index data types extend the `IndexBase` data type which in turn
extends the `AssetBase` type.  Within `IndexBase`, an index name can
be assigned, the index provider can be identified, and the asset
class specified.

[![](/img/ARTindex.png)](img/ART-index.png)

### Observable  {#observable}

In addition to assets, there are variables which can be observed in the markets and which can directly
influence the outcomes of financial products. In the CDM, the observed value represents the price of
an "observable".

:::tip Definition: Observable

The `Observable` data type specifies the reference object whose price is to be observed.
It could be an underlying asset, if it can be held or transferred,
or something which can be observed but not transferred, such as an index.

:::

[![](/img/ARTobservable.png)](img/ART-observable.png)

In addition to `Asset`, the `Observable` is a choice betwen the following data types:

``` Haskell
choice Observable:
    Asset 
    Basket 
    Index  
```

* **Asset**:  The inclusion of Asset in Observable enables the price of an asset to be included within the definition of another
financial product.
* **Basket**:  The object to be observed is a Basket, ie a collection of Observables with an identifier and optional weightings.
* **Index**:  The object to be observed is an Index, ie an observable whose value is computed on the prices, rates or valuations
of a number of assets.

Like `Asset`, both the `Basket` and `Index` types also extend `AssetBase`. This ensures that all types of `Observable` share
a common set of attributes, in particular an identifier.

The CDM allows both assets and observables to be used as underlying building blocks to construct
complex products (see the *[Underlier](#underlier)* section).

### Product

The model defines a product using three attributes:

* **identifier**: a product must have a unique identifier composed of an `identifier` string
and an `identifierType` enumerator that defines the symbology source of the identifier.
* **taxonomy**: a product can be classified according to one or more classification sources.
Compared to assets, the taxonomy value can be _inferred_ from the product's economic terms
rather than explicitly naming the product type.
* **economicTerms**: a parametric representation of the future financial obligations (e.g. cashflows)
generated by the product, built by composing payouts.

The first two attributes are common with the definition of an asset. Therefore, the defining feature
of a product compared with an asset is that it includes economic terms.

There are two types of products:

* A **transferable product** associates an asset, itself transferable, with the economic terms describing that asset.
* A **non-transferable product** describes a commitment between two parties to one or more transfers of assets in the future.

``` Haskell
choice Product:
    TransferableProduct
    NonTransferableProduct
```

:::

[![](/img/ARTtransferable.png)](img/ART-transferable.png)

#### TransferableProduct

Because an asset is a basic type of financial product, the `Asset` data type only needs to provide limited information
about that product: essentially it allows to identify the product using publicly available identifiers.

Sometimes, there is a need to specify the full economic terms of that product, when that product in turn
generates some future asset transfers - e.g. cashflows in the case of a loan, bond or equity (dividends). 
This is supported by the `TransferableProduct` data type.

:::tip Definition: TransferableProduct

A TransferableProduct is a type of Product which allows to specify the EconomicTerms of an Asset.
It can be used as the underlier of a basic Payout that describes the buying and selling of that Asset.

``` Haskell
type TransferableProduct extends Asset:
    economicTerms EconomicTerms (1..1)
```

Because `TransferableProduct` extends `Asset`, it inherits its `identifier` and `taxonomy` attributes from it.
In that case, those attributes are of type, respectively, `AssetIdentifier` and `Taxonomy`.

#### NonTransferableProduct

By contrast with a transferable product, which can be held by a single party who can in turn transfer it to another,
some financial products consist of bilateral agreements between two parties. As such, they cannot be freely transferred
by one of the parties to a third party (at least not without the consent of the other party). Such product is usually
materialised by a financial contract between those parties and can also be referred to as a "contractual" product.

In the CDM, those products are represented by the `NonTransferableProduct` type:

:::tip Definition: NonTransferableProduct

A non-transferable product represents a financial product that is agreed bilaterally between two parties.
The data type specifies the financial product's economic terms alongside its identifier and taxonomy.
A non-transferable product is instantiated by a trade between the two parties that defines the tradable product,
and evolves through the CDM's lifecycle event model.

:::

``` Haskell
type NonTransferableProduct:  
  [metadata key]
  identifier ProductIdentifier (0..*) 
  taxonomy ProductTaxonomy (0..*) 
  economicTerms EconomicTerms (1..1)
```

While a `NonTransferableProduct` shares the `identifier` and `taxonomy` attributes with its `TransferableProduct` counterpart,
those attributes use different types, respectively:

* **`ProductIdentifier`** uses a more restrictive enumerator to specify the `identifierType` compared to `AssetIdentifier`.
* **`ProductTaxonomy`** enriches the simpler `Taxonomy` data type with the product's primary and secondary asset classes
using the `AssetClassEnum`, which leverages the FpML classification.

Compared with Asset and Observable, which are minimally defined,
the modelling of a contractual product requires a larger data structure
to support the representation of economic terms.

The terms of the contract are specified at trade inception and
apply throughout the life of the contract (which can last for decades
for certain long-dated products) unless amended by mutual agreement.
Contractual products may be fungible (replaceable by other
identical or similar contracts) only under specific terms: e.g. the
existence of a close-out netting agreement between the parties.

Given that each contractual product transaction is unique, all of the
contract terms must be specified and stored in an easily accessible
transaction lifecycle model so that each party can evaluate their
financial risks during the life of the agreement.

#### Product Scope

The scope of (non-transferable) products in the model is summarized below:

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
  payout Payout (1..*)
  terminationProvision TerminationProvision (0..1)
  calculationAgent CalculationAgent (0..1)
  nonStandardisedTerms boolean (0..1)
  collateral Collateral (0..1)
```

[![](/img/ARTproduct.png)](img/ART-product.png)

### Payout

The `Payout` type defines the composable payout types, each of which
describes a set of terms and conditions for the financial
obligation between the contractual parties. Payout types can be
combined to compose a product.

:::tip Definition: Payout

Represents the set of future cashflow methodologies in the form of 
specific payout data type(s) which combine to form the financial product.  
Examples: a "cash" trade (buying and selling an asset) will use a settlement payout; 
for derivatives, two interest rate payouts can be combined to specify 
an interest rate swap; one interest rate payout can be combined with 
a credit default payout to specify a credit default swap; an equity swap
combines an interest rate payout and a performance payout; etc.

:::

``` Haskell
choice Payout:
  [metadata key]
  AssetPayout
  CommodityPayout
  CreditDefaultPayout
  FixedPricePayout
  InterestRatePayout
  OptionPayout
  PerformancePayout
  SettlementPayout

```

All payout types extend a common data type called `PayoutBase`.
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

For example:

``` Haskell
type InterestRatePayout extends PayoutBase:
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

[![](/img/ARTpayout.png)](img/ART-payout.png)

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

The price and quantity attributes in the `PayoutBase`
structure are positioned in the `ResolvablePriceQuantity`
data type. This data type mirrors the `PriceQuantity` data
type and contains both the price and quantity schedules.

That data type supports the definition of additional
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
cross-referencing annotation in the Rune DSL allows to parameterise
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

#### Underlier

The concept of an underlier allows for financial products to be used
within the definition of another product to drive outcomes, for example
a forward or option (contingent on an underlying asset), or an equity swap
(contingent on an underlying stock price or index).

:::tip Definition: Underlier

The underlying financial product can be of any type: e.g. an asset such as
cash or a security, an index, or a product, and may be physically or cash settled
as specified in the payout definition.  Conditions are usually applied when used in 
a payout to ensure that the underlier aligns with the payout's use case.

:::

[![](/img/ARTcomplete.png)](img/ART-complete.png)

This fact that a product can be nested as an underlier in the definition of
another product is what makes the product model composable. One use case
is an interest rate swaption for which the
high-level product uses the `OptionPayout` type and the underlier is an
interest rate swap composed of two `InterestRatePayout` types.
Similiarly, the product underlying an Equity Swap composed of an
`InterestRatePayout` and a `PerformancePayout` could be an equity security
defined as a transferable product.

In the simplest case, the underlier in an `AssetPayout` can only ever be
a security, so the definition within this data type is constrained as such.

In a `CommodityPayout` or a `PerformancePayout`, the purpose of the underlier
is to influence the values of the future returns, so the appropriate data
type to use for the underlier is an Observable.

In the case of a `SettlementPayout`, there are a variety of possible
outcomes as the settlement can be an Asset, the cash value of an Index, or
a TransferableProduct.  Therefore, the choice data type `Underlier` has
been defined and is used as the underlier attribute in this payout.

Financial option products allow for an even greater range of outcomes, so
the choice data type `OptionUnderlier` provides for both Observables and Products
(itself also a choice data type) to be used in an `OptionPayout`.

``` Haskell
choice Underlier:
    Observable
        [metadata address "pointsTo"=PriceQuantity->observable]
    Product

choice Product:
    TransferableProduct
    NonTransferableProduct
```

**Use of underliers in payouts**

The following table summarises the use of underliers for each of the main payout data types.

| **Payout**    | **Underlier Definition** | **Rationale** |
| :-------- | :------- | :------- | 
| `AssetPayout` | `underlier Asset (1..1)` | Specifies the Purchased Asset, usually a Security
| `CommodityPayout` | `underlier Underlier (1..1)` | Identifies the underlying product that is referenced for pricing of the applicable leg in a swap.
| `OptionPayout` | `underlier Underlier (1..1)` | The underlier defines the exercise, which can be cash or physical, therefore it can be any of an Asset, Basket, Index or NonTransferableProduct
| `PerformancePayout` | `underlier Underlier (0..1)` | The underlier is a pricing mechanism, ie an Observable
| `SettlementPayout` | `underlier Underlier (1..1)` | The underlier that is settled and can be an Asset, Index or TransferableProduct

#### SettlementPayout

A `SettlementPayout` is a specialised choice of payout introduced to represent the
buying or selling of an underlying asset or product, which then needs to be settled.

:::tip Definition: SettlementPayout

A SettlementPayout can represent a spot or forward settling payout. The `underlier` 
attribute captures the underlying product or asset, which is settled according to the 
`settlementTerms` attribute (which is part of `PayoutBase`).

:::

Conditions on the definition of `SettlementPayout` ensure the following are true
for the underlier:
- If it is a `Product`, it must not be a `NonTransferableProduct`.
- If it is a `Basket`, then all of the constituents of the basket must be assets.
- If it is an `Index`, then it must be cash settled.

`SettlementPayout` can be used for foreign exchange trades, either spot- (cash) or 
forward-dated. In this case, the underlier specifying the asset to be settled must
be of `Cash` type. The price defined in the `PriceQuantity` represents the exchange
rate in the purchase currency. Non-deliverable forwards can be represented using the
cash-settlement option.

## Tradable Product {#tradable-product}

A tradable product represents a financial product that is ready to be
traded, meaning that there is an agreed financial product, price,
quantity, and other details necessary to complete the execution of a
negotiated contract between two counterparties. Tradable products
are represented by the `TradableProduct` data type.

:::tip Definition: TradableProduct

Definition of a financial product as ready to be traded, i.e. included in 
an execution or contract, by associating a specific price and quantity to
the product definition, plus optional conditions for any potential future
quantity adjustment. A tradable product does not define the trade date,
execution details or other information specific to the execution event
but allows for multiple trade lots, quantities and prices, between the 
same two counterparties.

:::

[![](/img/ARTtrade.png)](img/ART-trade.png)

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
type are ones that are shared by all trades. Every trade is based on a
financial product and has a price, a quantity (treated jointly as a
trade lot), and a pair of counterparties. In some cases, there are ancillary
parties, or an allowable adjustment to the notional quantity. All of the
other attributes required to describe the trade's economic terms are defined
in the `NonTransferableProduct` data type.

There are cases when the object of a trade is a _transferable_ product
whose economic terms are already set: for instance when buying or selling
a fungible instrument like a security or a loan. In those cases, the terms
of that trade still need to be contractually agreed betwwen the parties.
This contract's terms would be defined in a `Payout` and embedded in a
`NonTransferableProduct` which is not transferable, even though
the underlying product may be.

In its simplest form, that trade's terms will specify the settlement date
in addition to the price and quantity and can be represented using the
[`SettlementPayout`](#SettlementPayout).

A `TradableProduct` also provides a mechanism to trade indices that
otherwise cannot be directly transfered. The `Payout` would define how
the index is meant to be observed and the resulting cashflows between
the parties based on that observed value.

This example shows the structure for a foreign exchange trade which is composed
of:
- a `Trade` and a `TradableProduct`
- a `NonTransferableProduct` composed using a single `SettlementPayout`.  This in
  turn has a `Cash` underlier which specifies the currency of the payout.
- a `TradeLot` containing a `PriceQuantity`, which defines the price of the underlier,
  expressed as a quantity in the second currency, and an exchange rate.

[![](/img/ARTsettlement.png)](img/ART-settlement.png)

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
further detailed in the Rune DSL documentation.

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
    [metadata location]
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
Interest Rate Derivatives, Equity Derivatives, Foreign Exchange, Security
Lending, and
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
