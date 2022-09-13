# *Product Model - Performance Payout*

_Background_ 

After the release of the Performance Payout, an in-depth review of its structures and components was carried out in which several regression issues were identified, particularly regarding cardinality, former commodity-related attributes, and the dividend structure. This release addresses these issues, and adds minor mapping improvements for performance and commodity payouts.

_What is being released?_

- Updated cardinality for several attributes in `PerformancePayout`.
- Removed commodity-endemic attributes from `PerformancePayout`.
- Renamed several `PerformancePayout` attributes.
- Added model support for basket component-specific `DividendPayoutRatio`.
- Minor mapping improvements.

_Data types_

- The attribute `primaryRateSource` in type `FxSpotRateSource`renamed as `primarySource`.
- The attribute `secondaryRateSource` in type `FxSpotRateSource` renamed as `secondarySource`.
- The attribute `dividendPayoutRatio` in type `DividendPayout` (now `DividendPayoutRatio`) renamed as `totalRatio`.
- The attribute `dividendPayout` in type `DividendReturnTerms` renamed as `dividendPayoutRatio`.
- The attribute `dividendPayoutRatioCash` in type `DividendPayout` (now `DividendPayoutRatio`) renamed as `cashRatio`.
- The attribute `dividendPayoutRatioNonCash` in type `DividendPayout` (now `DividendPayoutRatio`) renamed as `nonCashRatio`.

- Removed `rounding` attribute from type `PriceReturnTerms`.
- Removed `spread` attribute from type `PriceReturnTerms`.
- Removed `rollFeature` attribute from type `PriceReturnTerms`.
- Removed `averagingMethod` attribute from type `PerformancePayout`.

- Cardinality for `fxFeature` changed to `(0..*)` in type `PerformancePayout`.
- Cardinality for `pricingTime` changed to `(0..1)` in type `ObservationTerms`.
- Condition `PricingTime` added to type `ObsevationTerms`

_Translate_

- Mappings adapted to changes introduced in this contribution.
- Minor mapping improvements for `PerformancePayout` and `CommodityPayout`.

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

In the CDM Portal, select Ingestion and review the following samples:

- fpml-5-10/products/equity
  - eqs-ex01-single-underlyer-execution-long-form
  - eqs-ex01-single-underlyer-execution-long-form-other-party
  - eqs-ex06-single-index-long-form
  - eqs-ex09-compounding-swap
  - eqs-ex10-short-form-interestLeg-driving-schedule-dates
  - eqs-ex11-on-european-single-stock-underlyer-short-form
  - eqs-ex12-on-european-index-underlyer-short-form
  - eqs-ex13-pan-asia-interdealer-share-swap-short-form

- fpml-5-10/incomplete-products/equity-swaps
  - eqs-ex08-composite-basket-long-form-separate-spreads
  - trs-ex01-equity-basket