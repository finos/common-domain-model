# *Product Model - Performance and Fixed Price Payouts*

_Background_

Variance, volatility, correlation, and dividend products are currently not supported by CDM Core. This release intends to allow for the representation of this group of products. In order to avoid the proliferation of new payouts, a new Performance Payout has been created to encompass all products formerly represented through the Equity Payout (that is, price and total return swaps) plus the new aforementioned products. The underlying logic in the Performance Payout is to create a common structure for all products sharing the following characteristics:

* Require an observation of an underlier to be made.
* The return is derived from the observed value of the underlier, either directly (as in price or total return swaps) or by applying a specific function to the observed value (as in variance, volatility, or correlation).

The intention is to include all such products in a single payout type, which then allows to specify the particular metric observed and the particular function applied by the means of different return terms, each of them featuring both shared and return-terms-endemic components. 

The new performance payout is not equity-exclusive, but also allows representing other performance based products such as foreign exchange variance and volatility swaps, belonging to the Foreign Exchange asset class.

Taking into account the scope of the changes, it has been deemed prudent to keep temporarily the previous model along with the new performance payout, rather than replacing it entirely. In this line, all components affected by the change have been duplicated, thus effectively enabling the user to represent equity products in the two ways (except for the new variance, volatility and correlation, which were not included in the equity payout).

As this coexistence of both equity and performance payouts is devised as a temporary measure until the latter is fully tested, components associated with the older model have been marked as deprecated.

Change summary:
  
* New performance payout, intended to:
  * Substitute the equity payout (price, total return products)
  * Represent variance, volatility, correlation and partially dividend 
* Added specific components for variance, volatility, correlation and dividend products.
* Renamed FixedForwardPayout to FixedPricePayout to better reflect its content and usage.
* Restructured date system for equity products. The attribute calculationPeriodDates replaced by valuationDates and observationDates for all performance products.
* Duplicated components to allow the coexistence of old and new representation of equity products (equityPayout/performancePayout and calculationPeriodDates/valuationDates and observationDates).

_What is being released?_

The summary below is divided into sections for Data Types, Enumerations, Functions and Translate.

*Data Types*

`event-common-type`:

Modified

* `SettlementOrigin` – Added performancePayout. fixedForwardPayout replaced by fixedPricePayout.
* `Lineage` – References to equityPayout deprecated. Equivalent references to performancePayoutAdded

`observable-asset-type`:

New

* `EquityValuationDates` – With respect to EquityValuation, removed price attribute.
* `EquityObservation` –  Defines how and when a performance type option or performance type swap is to be observed. Structure analogous to that of EquityValuation.
* `DividendApplicability` – The parameters which define whether dividends are applicable. Equivalent to the FpML Dividends.model.

Deprecated

* `EquityValuation`

`product-asset-type`:

New

* `PerformanceDividendReturnTerms` –  Several components removed because they are represented in dividendReturnTerms -> dividendPeriod. Added attribute performance as a placeholder.
* `DividendPeriod` – Time bounded dividend payment periods, each with a dividend payment date per period.
* `PerformancePriceReturnTerms` – valuationPriceInterim replaced by valuationPriceInitial. Absorbed returnType to distinguish if the payout is part of a total return product, or it is price only.  Absorbed elements formerly exclusive to commodity return terms (no longer present in the performance payout), but potentially applicable to equity products as well (rounding, conversionFactor, spread, rollFeature). Added attribute performance as a placeholder.
* `ReturnTermsBase` – Contains all common components in variance, volatility and correlation return Terms.
* `VarianceReturnTerms`  – Contains all specific components required to represent a variance product and, through inheritance from ReturnTermsBase, those common to variance, volatility and correlation.
* `VolatilityReturnTerms` – Contains all specific components required to represent a volatility product and, through inheritance from ReturnTermsBase, those common to variance, volatility and correlation.
* `CorrelationReturnTerms` – Contains all specific components required to represent a correlation product and, through inheritance from ReturnTermsBase, those common to variance, volatility and correlation.
* `Valuation` – Contains all non-date valuation information.
* `EquityUnderlierProvisions`  – Contains Equity underlier provisions regarding jurisdiction and fallbacks.
* `VarianceCapFloor`  – Contains possible barriers for variance products, both variance-based and underlier price based.
* `BoundedVariance` – Contains conditions which bound variance when the contract specifies one or more boundary levels. These levels are expressed as prices for confirmation purposes.
* `VolatilityCapFloor` – Contains containing volatility-based barriers.
* `BoundedCorrelation` –  Describes correlation bounds, which form a cap and a floor on the realized correlation.

Deprecated

* `DividendPayoutRatio` – underlier component deprecated. Following migration to `PerformancePayout`, underlier can be removed from as it is already present in dividendPeriod, where it can be established for every period.
* `DividendReturnTerms`
* `PriceReturnTerms`

`product-common-schedule-type`:

New

* `DateRelativeToValuationDates` – Version of the function `DateRelativeToCalculationRates` using `EquityValuationDates` as an input instead of `CalculationPeriodDates`.

`product-common-settlement-type`:

New

* `PerformancePayoutBase` –  Removed calculationPeriodDates and replaced by valuationDates and observationDates. Used in the new `PerformancePayout`.

Modified

* `FxFixingDate` –  Version of the function `ResetDates` using `EquityValuationDates` as an input instead of `CalculationPeriodDates`.

`product-template-type`:

New

* `ReturnTerms` – Specifies the type of return of a performance payout: price, dividend, variance, volatility or correlation.
* `PerformancePayout`  – New performance payout. Contains the necessary specifications for all performance payouts, encompassing equity return, dividend, variance, volatility and correlation products.

Deprecated

* `EquityPayout` - Note that despite being deprecated, it is still used in the current version to allow for representation of equity products already present in the model through both the existing `EquityPayout` and the new `PerformancePayout`. 

Modified

* `EconomicTerms` – condition ExtraordinaryEvents adapted to include performancePayout.
* `Payout` – fixedForwardPayout attribute replaced by fixedPricePayout (name change). Added performancePayout attribute. Corresponding conditions added or modified.
* `TradableProduct` – Conditions duplicated or modified to match the new performance structure.
* `FixedForwardPayout`  – Renamed as `FixedPricePayout`.

*Enumerations*

`base-math-enum`:

* `FinancialUnitEnum` – Added `Variance` and `Volatility` values.

`observable-asset-enum`:

* `PriceTypeEnum` – Added `Variance` and `Volatility` values.

`product-asset-enum`:

* `RealisedVarianceMethodEnum` – The contract specifies which price must satisfy the boundary condition.
* `FPVFinalPriceElectionFallbackEnum` – Specifies the fallback provisions in respect to the applicable Futures Price Valuation.

Deprecated

* `ReturnTypeEnum` – Values `Variance`, `Volatility` and `Dividend` deprecated, since they now can be identified by their returnTerms. Only `Price` and `Total` values left as non deprecated.

*Functions*

`event-common-func`:

Duplicates (changed references)

* `ResolvePerformanceValuationDate` – References to equityPayout replaced by references to performancePayout.
* `ResolvePerformanceValuationTime` – References to equityPayout replaced by references to performancePayout.
* `ResolvePerformanceReset` – References to equityPayout replaced by references to performancePayout.

Modified

* `NewEquitySwapProduct`  – Reference to fixedForwardPayout replaced by reference to fixedPricePayout.

`product-asset-func`:

New

* `ResolvePerformancePeriodStartPrice` – References to equityPayout replaced by references to performancePayout.  References to calculationPeriodDates replaced by references to valuationDates.
* `ResolvePerformancePeriodEndPric`e – References to equityPayout replaced by references to performancePayout.  References to calculationPeriodDates replaced by references to valuationDates.

Deprecated

* `ResolveEquityPeriodStartPrice`
* `ResolveEquityPeriodEndPrice`

`product-common-func`:

New

* `Qualify_EquitySwap_ParameterReturnVariance_SingleName` – Qualifies a product as an Equity Swap for which the performance is based on the variance changes on a single stock.
* `Qualify_EquitySwap_ParameterReturnVariance_Index` – Qualifies a product as an Equity Swap for which the performance is based on the variance changes on an index.
* `Qualify_EquitySwap_ParameterReturnVariance_Basket` – Qualifies a product as an Equity Swap for which the performance is based on the variance changes on a basket.
* `Qualify_EquitySwap_ParameterReturnDispersion` – Qualifies a product as an Equity Swap for which the performance is based on the variance changes in several legs.
* `Qualify_EquitySwap_ParameterReturnVolatility_SingleName` – Qualifies a product as an Equity Swap for which the performance is based on the volatility changes on a single stock.
* `Qualify_EquitySwap_ParameterReturnVolatility_Index` – Qualifies a product as an Equity Swap for which the performance is based on the volatility changes on an index.
* `Qualify_EquitySwap_ParameterReturnVolatility_Basket` – Qualifies a product as an Equity Swap for which the performance is based on the volatility changes on a basket.
* `Qualify_EquitySwap_ParameterReturnCorrelation_Basket` – Qualifies a product as an Equity Swap for which the performance is based on changes in the correlation between the constituents of a basket.
* `Qualify_EquitySwap_ParameterReturnDividend_SingleName` – Qualifies a product as an Equity Swap for which the performance is based on the dividend returns of a single stock.
* `Qualify_EquitySwap_ParameterReturnDividend_Index` – Qualifies a product as an Equity Swap for which the performance is based on the dividend returns of an index.
* `Qualify_EquitySwap_ParameterReturnDividend_Basket` – Qualifies a product as an Equity Swap for which the performance is based on the dividend returns of a basket.
* `Qualify_ForeignExchange_ParameterReturnVariance` – Qualifies a product as Foreign Exchange Swap for which the performance is based on the variance of a foreign exchange underlier.
* `Qualify_ForeignExchange_ParameterReturnVolatility` – Qualifies a product as Foreign Exchange Swap for which the performance is based on the volatility of a foreign exchange underlier.
* `Qualify_ForeignExchange_ParameterReturnCorrelation` – Qualifies a product as Foreign Exchange Swap for which the performance is based on the correlation changes between the constituents of a basket.

Modified

* `Qualify_AssetClass_Equity` – Added products with underlier equity baskets as Equity Asset Class products
* `Qualify_BaseProduct_EquitySwap` – Added asset class check using Qualify_AssetClass_Equity to replace the former equityPayout check. Payout check changed from equity Payout to several payout combinations (all containing at least a  performance payout) to reflect the payout structures of all the equity products now available.
* `Qualify_EquitySwap_PriceReturnBasicPerformance_SingleName.` – Qualification adapted to include performance payout. One interest and one performance payout is required.
* `Qualify_EquitySwap_TotalReturnBasicPerformance_SingleName` – Qualification adapted to include performance payout.  One performance (dividend), one performance (price) and one interest rate payout are required.
* `Qualify_EquitySwap_PriceReturnBasicPerformance_Index` – Qualification adapted to include performance payout.  One interest and one performance payout is required.
* `Qualify_EquitySwap_TotalReturnBasicPerformance_Index` – Qualification adapted to include performance payout.  One performance (dividend), one performance (price) and one interest rate payout are required.
* `Qualify_EquitySwap_PriceReturnBasicPerformance_Basket` – Qualification adapted to include performance payout. One interest and one performance payout is required.
* `Qualify_EquitySwap_TotalReturnBasicPerformance_Basket` – Qualification adapted to include performance payout. One performance (dividend), one performance (price) and one interest rate payout are required.
* `Qualify_Commodity_Swap_FixedFloat` – References to equityPayout replaced by references to performancePayout

`product-common-schedule-func`:

New

* `ValuationPeriod` – Version of the function `CalculationPeriod` using valuationDates as an input instead of calculationPeriodDates.  A Java implementation will be added as part of a future release.
* `ValuationPeriods` – Version of the function `CalculationPeriods` using valuationDates as an input instead of calculationPeriodDates.  A Java implementation will be added as part of a future release.

*Translate*

Added synonym coverage for variance and volatility swaps using the new performance payout. Other equity products not supported by the performance payout yet, but by the existing equity structure. Mappings and full coverage for equity products using the new performance payout are expected to be included in upcoming releases.

_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect each of the changes identified above.

In the CDM Portal, select Ingestion and review the following samples:

fpml-5-10/incomplete-products/variance-swaps
* eqvs-ex01-variance-swap-index.xml
* eqvs-ex02-variance-swap-single-stock.xml
* eqvs-ex03-conditional-variance-swap.xml
* eqvs-ex04-dispersion-variance-swap.xml
* eqvs-ex05-dispersion-variance-swap-transaction-supplement.xml
* eqvs-ex06-variance-option-transaction-supplement.xml
* eqvs-ex07-variance-option-transaction-supplement-pred-clearing.xml


fpml-5-10/incomplete-products/volatility-swaps
* eqvls-ex01-volatility-swap-index-matrix.xml
* eqvls-ex02-volatility-swap-index-mca.xml