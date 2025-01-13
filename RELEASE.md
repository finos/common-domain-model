
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
  - RoundToPrecision Function: [6.0.0-dev.74](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.74) & [6.0.0-dev.40](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.40)
  - RoundToSignificantFigures Function: [6.0.0-dev.73](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.73)
  - ISO Country Code Enum Update: [6.0.0-dev.69](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.69)
  - Update ISOCurrencyCodeEnum: **Backward incompatible changes** [6.0.0-dev.54](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.54) & [6.0.0-dev.53](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.53)
  - Date Time Functions: [6.0.0-dev.19](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.19)
  - Updates to Zero Coupon Swaps Qualification Functions: [6.0.0-dev.13](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.13)
  - Natural Person and NaturalPersonRole circular reference: **Backward incompatible changes** [6.0.0-dev.3](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.3)


- **FpML Mappings**
  - InterestRateForwardDebtPriceMappingProcessor updated to handle 'Percentage' quoteUnits: [6.0.0-dev.81](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.81)
  - Related party role mapper: [6.0.0-dev.81](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.81)
  - Floating Rate Index Mappings: [6.0.0-dev.69](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.69)
  - Synonym mappings for BusinessCenterEnum: [6.0.0-dev.69](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.69)
  - Post Trade Risk Reduction Mapping Update: [6.0.0-dev.69](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.69)
  - Mapping Updates: [6.0.0-dev.50](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.50) & [6.0.0-dev.45](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.45)
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
    - [6.0.0-dev.92](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.92)
      - DSL 9.27.0: addresses a bug where the switch operator could sometimes break the model. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.27.0
    - [6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90)
      - DSL 9.25.0: improve type errors and cardinality errors. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.25.0
      - DSL 9.24.0: add a feature to override attributes in extended types. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.24.0
    - [6.0.0-dev.83](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.83)
      - DSL 9.22.0: handle null for min and max operations. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.22.0
      - FpML Coding Scheme 11.25.1: support for latest version (v2.20).
    - [6.0.0-dev.80](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.80)
      - Updates the rune dependencies to version 11.24.2. This update includes support for visualising the Choice Type elements in the Rosetta User Interface.
    - [6.0.0-dev.76](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.76)
      - DSL 9.20.0: support for passing metadata to functions and highlighting fixes. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.20.0
    - [6.0.0-dev.71](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.71)
      - DSL 9.19.0: support for switch operation on choice types. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.19.0
    - [6.0.0-dev.68](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.68) 
      - DSL 9.18.0: new syntax features. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.18.0
      - DSL 9.18.1: memory improvements. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.18.1
    - [6.0.0-dev.67](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.67)
      - DSL 9.16.2: support for ingestion tabulators. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.2
    - [6.0.0-dev.66](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.66)
       - DSL 9.16.1: support for ingestion tabulators. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.1
    - [6.0.0-dev.65](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.65)
      - ingest-test-framework 11.17.1: Add support for address/location references to be used on nested model types.
    - [6.0.0-dev.64](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.64)
      - DSL 9.15.0: patch for supporting tabulation of types with circular dependencies. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.0
      - DSL 9.15.1: patch for missing generated Java files. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.1
      - DSL 9.15.2: patch for missing Java meta classes. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.15.2
    - [6.0.0-dev.63](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.63)
      - DSL 9.14.1: Support for defining metadata annotations on choice options. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.14.1
    - [6.0.0-dev.62](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.62)
      - This release removes unused projects and folders to avoid the maintenance of legacy code.
        - /rosetta-project - legacy Java PoC project related to translating CDM to FpML
        - /rosetta-source/src/main/resources/available-samples - folder contains samples that are duplicates of ingestion samples
    - [6.0.0-dev.61](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.61)
      - DSL 9.14.0: Support for accessing meta features after a deep feature call. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.14.0
    - [6.0.0-dev.56](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.56)
      - DSL 9.12.0: this release fixes an issue where the only exists operator behaved unexpectedly when subtyping was involved. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.0.
      - DSL 9.12.1: this patch fixes null pointers in the Java runtime of the only exists operator. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.1.
      - DSL 9.12.2: this patch fixes a code generation bug in the Java generator. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.2.
      - DSL 9.12.3: this patch fixes an issue where the code generator could freeze Rosetta. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.3.
      - DSL 9.12.4: this patch fixed an issue with only exists on multi-cardinality inputs. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.12.4.
      - Jackson 2.17.1: this release updates the library used to serialise/deserialise JSON.
    - [6.0.0-dev.52](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.52)
      - ingest-test-framework 11.10.3: Translate bug fix for long XML files
      - DSL 9.11.2: Fix syntax validation issue. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.11.2
    - [6.0.0-dev.44](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.44) `IN 5.0`
      - rosetta-bundle 11.6.0: Dependencies migrated to Maven Central
      - rosetta-bundle 11.6.2: FpML coding scheme infrastructure update to support configurable coding scheme matching for Prod and Dev versions
      - rosetta-bundle 11.7.0: Java compilation performance improvements
      - DSL 9.8.5: Java compilation performance improvements. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.8.5
    - [6.0.0-dev.38](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.38) 
      - rosetta-bundle 10.17.1:
        - FpML coding scheme updated to version 2.19. FloatingRateIndexEnum updated to match coding scheme.
        - Bug fix for zonedDateTime serialisation issue #2895.
    - [6.0.0-dev.37](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.37)
      - rosetta-bundle 10.16.0: FpML Coding schema updated.
      - rosetta-dsl 9.8.0: this release features three new operations - to-date, to-date-time and to-zoned-date-time - to convert a string into a date, dateTime or zonedDateTime respectively. It also adds support to convert these three types into a string using the to-string operation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.8.0.
    - [6.0.0-dev.35](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.35)
      - rosetta-bundle 10.15.7: Translate bug fix to handle enum name clashes.
    - [6.0.0-dev.27](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.27)
      - rosetta-dsl 9.7.0: DSL validation and performance enhancements. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.7.0.
    - [6.0.0-dev.21](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.21)
      - rosetta-bundle 10.13.4: FpML Coding schema updated.
      - rosetta-dsl 9.6.1: DSL bug fix for handing null values. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.6.1.
    - [6.0.0-dev.19](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.19)
      - rosetta-dsl 9.5.0: Adds support for tabulating projection data. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.5.0.
      - rosetta-dsl 9.6.0: DSL build and compile performance improvements. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.6.0.
      - rosetta-bundle 10.12.0: Adds JSON schema code generator.
    - [6.0.0-dev.18](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.18)
      - rosetta-bundle 10.9.3: this release adds mapping support for XSD substitution groups, which fixes the issue related to the mapping of FpML oilPhysicalLeg xml elements.
    - [6.0.0-dev.9](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.9)
      - rosetta-bundle 10.0.0: Ingestion performance improvements related to the loading of xml schema files
    - [6.0.0-dev.8](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.9)
      - rosetta-dsl 9.2.0: this release moves deployment of DSL artifacts to Maven Central. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.2.0.
      - rosetta-dsl 9.3.0: this release contains syntax highlighting improvements. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.3.0.
    - [6.0.0-dev.2](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.2)
      - rosetta-dsl 9.1.2: this release fixes DSL issues #670 and #653. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.1.2.
      - rosetta-dsl 9.1.3: this release fixes an issue related to the generated Java process method. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.1.3.
  - Bug fix: Qualification and Cardinality Fixes [6.0.0-dev.91](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.91) 
  - CDM Distribution - Python Code Generation: [6.0.0-dev.51](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.51)
  - Python Generator V2: [6.0.0-dev.37](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.37)
