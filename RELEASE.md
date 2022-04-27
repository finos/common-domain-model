# *Product Model - Correlation and Dividend Swaps*

_Background_

The `PerformancePayout` was recently introduced to allow for representation of a wider variety of equity products including Price and Total Return Swaps, plus a new range of Equity Return Swaps, namely variance (including its dispersion variant), volatility, correlation and dividend swaps. The first release included mapping coverage for variance and volatility, while the present one adds coverage for correlation and dividend swaps.

_What is being released_

- Mapping coverage for correlation swaps
- Mapping coverage for dividend swaps
- Minor adjustments to equity qualification functions
- Fixes for variance and volatility mappings

_Functions_

**product-common-func**

Added `only exists` replacing `exists` for conditions specifying the type of `underlier` or the type of `returnTerms` required in variance, volatility, correlation and dividend qualification functions. 

Updated functions: 
- `Qualify_EquitySwap_ParameterReturnVariance_SingleName`
- `Qualify_EquitySwap_ParameterReturnVariance_Index`
- `Qualify_EquitySwap_ParameterReturnVariance_Basket`
- `Qualify_EquitySwap_ParameterReturnVolatility_SingleName`
- `Qualify_EquitySwap_ParameterReturnVolatility_Index`
- `Qualify_EquitySwap_ParameterReturnVolatility_Basket`
- `Qualify_EquitySwap_ParameterReturnCorrelation_Basket`
- `Qualify_EquitySwap_ParameterReturnDividend_SingleName`
- `Qualify_EquitySwap_ParameterReturnDividend_Index`
- `Qualify_EquitySwap_ParameterReturnDividend_Basket`
- `Qualify_ForeignExchange_ParameterReturnVariance`
- `Qualify_ForeignExchange_ParameterReturnVolatility`
- `Qualify_ForeignExchange_ParameterReturnCorrelation`

Removed `fixedPricePayout count = 1` condition for dividend swaps, thus allowing for multiple period dividend swaps to be qualified. 

Updated functions:
- `Qualify_EquitySwap_ParameterReturnDividend_SingleName`
- `Qualify_EquitySwap_ParameterReturnDividend_Index`
- `Qualify_EquitySwap_ParameterReturnDividend_Basket`

_Translate_
 
**synonym-cdm-fpml**

Fixed volatility and dispersion mapping issues.

Added mapping coverage for correlation swaps and dividend swaps. Correlation swaps use `PerformancePayout` alone, while dividend swaps use the `PerformancePayout` structure in combination with one or several `FixedPricePayout`.

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

In the CDM Portal, select Ingestion and review the following samples:

fpml-5-10/incomplete-products/variance-swaps
- eqvs-ex04-dispersion-variance-swap.xml
- eqvs-ex05-dispersion-variance-swap-transaction-supplement.xml

fpml-5-10/incomplete-products/volatility-swaps
- eqvls-ex01-volatility-swap-index-matrix.xml
- eqvls-ex02-volatility-swap-index-mca.xml

fpml-5-10/incomplete-products/correlation-swaps
- eqcs-ex01-correlation-swap.xml
- eqcs-ex02-correlation-swap.xml
- eqcs-ex03-correlation-swap.xml
- eqcs-ex04-correlation-swap.xml

fpml-5-10/incomplete-products/dividend-swaps
- div-ex01-dividend-swap.xml
- div-ex02-dividend-swap-collateral.xml
- div-ex03-dividend-swap-short-form-japanese-underlyer.xml

# *User Documentation - Development Guidelines*

_What is being released_

This release consolidates all the development guidelines into a single section in the CDM user documentation, and adds a specific sub-section regarding the agile development approach. It also includes some minor fixes and clean-ups.

In the CDM user documentation, there is now an entire section called "Development Guidelines". This section contains the following sub-sections:

- Governance (previously part of the "Overview" section)
- Design Principles (previously part of the "Overview" section)
- Agile Development Approach (new sub-section)
- How to Contribute (was previously a stand-alone section)
- Documentation Style Guide (was previously a stand-alone section)

In the "Overview" section, the governance and design principles parts have been replaced by references with links to that new "Development Guidelines" section of the user documentation.

_Review Directions_

In the [CDM user documentation](https://cdm.docs.rosetta-technology.io/index.html), navigate to the "Development Guidelines" section shown in the left-hand side panel.
