
# CDM Version 6.0

CDM 6.0, a production release, corresponds to developments made to the CDM throughout 2024 and previously released as CDM 6-dev. These developments include items that featured in the 2024 CDM roadmap:

- Option Payout Refactoring
- Asset Refactoring
- Standardized IM Schedule

as well as several additional model changes, bug fixes, dependencies updates and synonym mappings since version 5.0.

## _What is being released_

Below are some of the high-level changes included in CDM 6.0, with links to their corresponding development release tags containing more detailed release notes.

- **Asset Refactoring**
  - Commodity Payout Underlier: [6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90)
  - Cashflow Generation for Settlement Payout : [6.0.0-dev.89](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.89)
  - FloatingRateIndex and InterestRateIndex: **Backward incompatible changes** [6.0.0-dev.87](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.87)
  - Settlement Payout Price: **Backward incompatible changes** [6.0.0-dev.84](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.84)
  - AssetCriteria: **Backward incompatible changes** [6.0.0-dev.81](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.81)
  - ETD Product Qualification: [6.0.0-dev.79](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.79)
  - Payout as a Choice: **Backward incompatible changes** [6.0.0-dev.79](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.79)
  - Underlier in Corporate Action: [6.0.0-dev.77](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.77)
  - Product, SettlementPayout, Underliers: **Backward incompatible changes** [6.0.0-dev.72](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.72)
  - Basket, Index, Observable, Foreign Exchange: **Backward incompatible changes** [6.0.0-dev.60](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.60)
  - Asset, Index, Identifier: **Backward incompatible changes** [6.0.0-dev.58](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.58)
  - Remove AssetPool and deprecated data types: **Backward incompatible changes** [6.0.0-dev.47](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.47)
  - New Data Types: [6.0.0-dev.46](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.46)
  
- **Option Payout Refactoring**
  - Modification of AmericanExercise Condition in ExerciseTerms: [6.0.0-dev.41](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.41)
  - Option Payout Refactoring: **Backward incompatible changes** [6.0.0-dev.24](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.24)

- **Standardized IM Schedule**
  - Enhancement and Optimization of the Standardized Schedule Method: [6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90)
  - Implementation of the Standardized Schedule Method for Initial Margin Calculation: [6.0.0-dev.69](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.69)

- **Misc. Product Model**
  - Security Finance trade types: **Backward incompatible changes** [6.0.0-dev.86](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.86)
  - Add Price to Payouts: [6.0.0-dev.77](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.77)
  - Portfolio Return Terms: **Backward incompatible changes** [6.0.0-dev.55](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.55)
  - Principal Amount Conditions: [6.0.0-dev.43](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.43)
  - Trigger type refactoring: [6.0.0-dev.42](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.42)
  - Synonym mappings for BusinessCenterEnum: [6.0.0-dev.33](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.33)
  - Day Count Fraction: RBA_Bond_Basis: **Backward incompatible changes** [6.0.0-dev.22](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.22)
  - Qualification functions:
    - Bond Forwards: [6.0.0-dev.32](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.32
    - Bond Option: [6.0.0-dev.32](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.32)

- **Misc. Event Model**
  - Addition of new enumeration to AvailableInventory: [6.0.0-dev.36](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.36)

- **Misc. Eligible Collateral Model**
  - Collateral Criteria AND/OR Logic: [6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90)
  - New Attributes: [6.0.0-dev.48](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.48)
  - CreditNotationMismatchResolutionEnum update: [6.0.0-dev.26](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.26)
  - CheckEligibilityResult cardinality fix: [6.0.0-dev.10](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.10)
  - Determination of the Party Roles: [6.0.0-dev.4](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.4)

- **Misc. Base Model**
  - Equity Products: **Backward incompatible changes** [6.0.0-dev.88](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.88)
  - TaxonomySourceEnum: [6.0.0-dev.85](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.85)
  - RoundToPrecision Function: [6.0.0-dev.74](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.74) & [6.0.0-dev.40](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.40)
  - Update ISOCurrencyCodeEnum: **Backward incompatible changes** [6.0.0-dev.54](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.54) & [6.0.0-dev.53](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.53)
  - Updates to Zero Coupon Swaps Qualification Functions: [6.0.0-dev.13](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.13)
  - Natural Person and NaturalPersonRole circular reference: **Backward incompatible changes** [6.0.0-dev.3](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.3)


- **FpML Mappings**
  - Related party role mapper: [6.0.0-dev.81](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.81)
  - Synonym mappings for BusinessCenterEnum: [6.0.0-dev.69](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.69)
  - Post Trade Risk Reduction Mapping Update: [6.0.0-dev.69](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.69)

- **Infrastructure**
  - Misc. Dependency updates
    - [6.0.0-dev.92](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.92)
      - DSL 9.27.0: addresses a bug where the switch operator could sometimes break the model. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.27.0
    - [6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90)
      - DSL 9.25.0: improve type errors and cardinality errors. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.25.0
      - DSL 9.24.0: add a feature to override attributes in extended types. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.24.0
    - [6.0.0-dev.66](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.66)
       - DSL 9.16.1: support for ingestion tabulators. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.1
  - Bug fix: Qualification and Cardinality Fixes [6.0.0-dev.91](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.91) 
