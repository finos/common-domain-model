
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
  - Modification to product condition: [6.0.0-dev.57](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.57)
  - Portfolio Return Terms: **Backward incompatible changes** [6.0.0-dev.55](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.55)
  - Principal Amount Conditions: [6.0.0-dev.43](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.43)
  - Trigger type refactoring: [6.0.0-dev.42](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.42)
  - Synonym mappings for BusinessCenterEnum: [6.0.0-dev.33](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.33)
  - Commodity Physical Options: [6.0.0-dev.25](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.25)
  - Day Count Fraction: RBA_Bond_Basis: **Backward incompatible changes** [6.0.0-dev.22](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.22)
  - Commodity Forwards: [6.0.0-dev.20](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.20)
  - Qualification functions:
    - Total Return Swaps (TRS) with a Debt Underlier: [6.0.0-dev.36](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.36)
    - Foreign Exchange NDS: [6.0.0-dev.36](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.36)
    - AssetClass: [6.0.0-dev.34](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.34) & [6.0.0-dev.30](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.30)
    - Bond Forwards: [6.0.0-dev.32](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.32) & [6.0.0-dev.8](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.8)
    - Bond Option: [6.0.0-dev.32](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.32)
    - Enhanced support for ETDs: [6.0.0-dev.23](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.23)
    - Zero-coupon Swaps: [6.0.0-dev.2](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.2)

- **Misc. Event Model**
  - Quantity Change For Existing Trade Lot: [6.0.0-dev.49](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.49)
  - Addition of new enumeration to AvailableInventory: [6.0.0-dev.36](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.36)
  - PartyRoleEnum including PTRRServiceProvider role: [6.0.0-dev.15](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.15)
  - Trade Lot Identifier added to Execution Instruction: [6.0.0-dev.5](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.5)
  - Valuation Update: [6.0.0-dev.2](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.2)

- **Misc. Eligible Collateral Model**
  - Collateral Criteria AND/OR Logic: [6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90)
  - New Attributes: [6.0.0-dev.48](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.48)
  - CreditNotationMismatchResolutionEnum update: [6.0.0-dev.26](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.26)
  - CheckEligibilityResult cardinality fix: [6.0.0-dev.10](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.10)
  - Determination of the Party Roles: [6.0.0-dev.4](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.4)

- **Misc. Legal Documentation Model**
  - New `ContractualDefinitionsEnum` value to support the 2022 ISDA Verified Carbon Credit Transactions Definitions: [6.0.0-dev.70](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.70)
  - Master Agreement Type enumeration - ISDAIIFM_TMA code: [6.0.0-dev.23](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.23)

- **Misc. Base Model**
  - Equity Products: **Backward incompatible changes** [6.0.0-dev.88](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.88)
  - TaxonomySourceEnum: [6.0.0-dev.85](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.85)
  - CapacityUnit Enum: [6.0.0-dev.82](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.82)
  - RoundToPrecision Function: [6.0.0-dev.74](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.74)
  - RoundToSignificantFigures Function: [6.0.0-dev.73](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.73)
  - ISO Country Code Enum Update: [6.0.0-dev.69](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.69)
  - Update ISOCurrencyCodeEnum: **Backward incompatible changes** [6.0.0-dev.53](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.53)
  - Date Time Functions: [6.0.0-dev.19](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.19)
  - Updates to Zero Coupon Swaps Qualification Functions: [6.0.0-dev.13](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.13)
  - Natural Person and NaturalPersonRole circular reference: **Backward incompatible changes** [6.0.0-dev.3](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.3)


- **FpML Mappings**
  - InterestRateForwardDebtPriceMappingProcessor updated to handle 'Percentage' quoteUnits: [6.0.0-dev.81](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.81)
  - Related party role mapper: [6.0.0-dev.81](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.81)
  - Floating Rate Index Mappings: [6.0.0-dev.69](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.69)
  - Synonym mappings for BusinessCenterEnum: [6.0.0-dev.69](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.69)
  - Post Trade Risk Reduction Mapping Update: [6.0.0-dev.69](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.69)
  - Mapping Updates: [6.0.0-dev.50](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.50)
  - Commodity Swaps: [6.0.0-dev.31](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.31)
  - Contractual Party: [6.0.0-dev.29](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.29)
  - Commodity Forwards: [6.0.0-dev.28](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.28)
  - Settlement Type Mapping Fix: [6.0.0-dev.23](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.23)
  - Commodity Classification Coverage: [6.0.0-dev.23](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.23)
  - Bond Forwards: [6.0.0-dev.15](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.15)
  - KnownAmountSchedule: [6.0.0-dev.12](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.12)
  - FpML 5.13 Working Draft 3 Mapping Updates - Post Trade Risk Reduction Mapping Update: [6.0.0-dev.11](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.11)


- **Infrastructure**
  - Misc. Dependency updates
  - Bug fix: Qualification and Cardinality Fixes [6.0.0-dev.91](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.91)
  - CDM Distribution - Python Code Generation: [6.0.0-dev.51](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.51)
  - Python Generator V2: [6.0.0-dev.37](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.37)
