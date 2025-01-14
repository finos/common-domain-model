# CDM Version 6.0

CDM 6.0, a production release, corresponds to developments made to the CDM throughout 2024 and previously released as CDM 6-dev. These developments include items that featured in the 2024 CDM roadmap:

- Option Payout Refactoring
- Asset Refactoring
- Standardized IM Schedule

as well as several additional model changes, bug fixes, dependencies updates and synonym mappings since the latest version 5.

## _What is being released_

Below are some of the high-level changes included in CDM 6.0, with links to their corresponding development release tags containing more detailed release notes.

- **Asset refactoring**
  - New Data Types: [6.0.0-dev.46](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.46)
  - Remove AssetPool and deprecated data types: **Backward incompatible changes** [6.0.0-dev.47](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.47)
  - Asset, Index, Identifier: **Backward incompatible changes** [6.0.0-dev.58](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.58)
  - Basket, Index, Observable, Foreign Exchange: **Backward incompatible changes** [6.0.0-dev.60](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.60)
  - Product, SettlementPayout, Underliers: **Backward incompatible changes** [6.0.0-dev.72](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.72)
  - Underlier in Corporate Action: [6.0.0-dev.77](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.77)
  - Payout as a Choice: **Backward incompatible changes** [6.0.0-dev.79](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.79)
  - ETD Product Qualification: [6.0.0-dev.79](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.79)
  - AssetCriteria: **Backward incompatible changes** [6.0.0-dev.81](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.81)
  - Settlement Payout Price: **Backward incompatible changes** [6.0.0-dev.84](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.84).
    - Note: this change is only backward-incompatible because it reverts the Add Price to Payouts change in [6.0.0-dev.77](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.77). The two changes are backward-compatible in aggregate.
  - FloatingRateIndex and InterestRateIndex: **Backward incompatible changes** [6.0.0-dev.87](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.87)
  - Cashflow Generation for Settlement Payout : [6.0.0-dev.89](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.89)
  - Commodity Payout Underlier: [6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90)

- **Option Payout refactoring**
  - Option Payout Refactoring: **Backward incompatible changes** [6.0.0-dev.24](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.24)
  - Modification of AmericanExercise Condition in ExerciseTerms: [6.0.0-dev.41](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.41)

- **Standardized IM Schedule**
  - Implementation of the Standardized Schedule Method for Initial Margin Calculation: [6.0.0-dev.69](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.69)
  - Enhancement and Optimization of the Standardized Schedule Method: [6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90)

- **Misc. Product Model changes**
  - Day Count Fraction: RBA_Bond_Basis: **Backward incompatible changes** [6.0.0-dev.22](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.22)
  - Trigger type refactoring: [6.0.0-dev.42](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.42)
  - Principal Amount Conditions: [6.0.0-dev.43](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.43)
  - Portfolio Return Terms: **Backward incompatible changes** [6.0.0-dev.55](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.55)
  - Security Finance trade types: **Backward incompatible changes** [6.0.0-dev.86](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.86)
  - Exotic Equity Products and Exercise Terms validation conditions: **Backward incompatible changes** [6.0.0-dev.88](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.88)
  - Qualification functions:
    - Zero Coupon Swaps: [6.0.0-dev.13](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.13)
    - Bond Option and Forwards: [6.0.0-dev.32](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.32)
    
- **Misc. Event Model changes**
  - Addition of new enumeration to AvailableInventory: [6.0.0-dev.36](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.36)

- **Misc. Eligible Collateral Model Changes**
  - Determination of the Party Roles: [6.0.0-dev.4](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.4)
  - CheckEligibilityResult cardinality fix: [6.0.0-dev.10](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.10)
  - CreditNotationMismatchResolutionEnum update: [6.0.0-dev.26](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.26)
  - New Attributes: [6.0.0-dev.48](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.48)
  - Collateral Criteria AND/OR Logic: [6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90)

- **Misc. Base Model changes**
  - Natural Person and NaturalPersonRole circular reference: **Backward incompatible changes** [6.0.0-dev.3](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.3)
  - RoundToPrecision Function: [6.0.0-dev.40](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.40) & [6.0.0-dev.74](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.74)
  - TaxonomySourceEnum: [6.0.0-dev.85](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.85)

- **FpML mappings**
  - Synonym mappings for BusinessCenterEnum: [6.0.0-dev.33](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.33)
  - Related party role mapper: [6.0.0-dev.81](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.81)

- **Infrastructure**
  - Misc. Dependency updates
    - [6.0.0-dev.66](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.66)
       - DSL 9.16.1: support for ingestion tabulators. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.16.1
    - [6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90)
      - DSL 9.25.0: improve type errors and cardinality errors. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.25.0
      - DSL 9.24.0: add a feature to override attributes in extended types. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.24.0
    - [6.0.0-dev.92](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.92)
      - DSL 9.27.0: addresses a bug where the switch operator could sometimes break the model. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.27.0
  - Bug fix: Qualification and Cardinality Fixes [6.0.0-dev.91](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.91) 

## _Backward-incompatible changes_

- **Asset refactoring**
  - To be completed...

- **Option Payout refactoring**
   
  _Data types_
  - Removed `OptionExercise` type.
  - Removed `OptionStyle` type.
  - Removed `AmericanExercise`, `EuropeanExercise`, and `BermudaExercise` types.
  - Added new `OptionExerciseStyleEnum` enumeration with values `American`, `European` and `Bermuda`.
  - Added new `ExerciseTerms` type, containing:
    - all of the distinct fields present before in `AmericanExercise`, `EuropeanExercise`, and `BermudaExercise` types,
    - a `style` attribute of type `OptionExerciseStyleEnum`,
    - and the `exerciseProcedure` attribute of type `ExerciseProcedure` that was previously contained in `OptionExercise`.
    - Switched `exerciseTerms` attribute in `OptionPayout` type to use the new `ExerciseTerms` type instead of the removed `OptionExercise` type.
    - Moved the `strike` attribute previously contained in `OptionExercise` type to `OptionPayout` type.
    - Removed `americanExercise`, `europeanExercise`, and `bermudaExercise` attributes from `CancelableProvision`, `ExtendibleProvision`, and `OptionalEarlyTermination` types
    - Replaced by a single `exerciseTerms` attribute of type `ExerciseTerms` instead.
  
  _Enumerations_
  - Added new `OptionExerciseStyleEnum` enumeration, with values `American`, `European`, and `Bermuda`.
    - The type `OptionExercise` is removed from the model and replaced by the `ExerciseTerms` type. This new type is used instead for the `exerciseTerms` attribute in `OptionPayout`.
    - The `OptionStyle` type is removed from the model along with the three option exercise types contained inside it: `AmericanExercise`, `EuropeanExercise`, `BermudaExercise`.
    - Instead in `ExerciseTerms`, it is replaced by the distinct group of attributes required to represent any type of option style, plus the `exerciseProcedure` attribute previously in `OptionExercise`.
    - The `style` enumeration is incorporated into the `ExerciseTerms` type to differentiate between American, European, and Bermuda styles.
    - Finally, the `strike`, previously under `OptionStyle`, has been moved outside of `ExerciseTerms` and is located directly under `OptionPayout`.

  _Sample Impact_

  There are many samples impacted by this change, namely all the samples utilizing the `OptionPayout` structure. The impact on the three following samples (one European, one American, one Bermuda) is shown to visualize how the refactoring of the `OptionPayout` affects the structure of the CDM trades:

- `eqd ex04 european call index long form`: the `OptionStyle` has been removed in favor of the the `style` = "European", and the relevant fields previously under `europeanExercise` (`expirationDate` and `expirationTimeType`). Additionally, the `strike` is moved from inside `exerciseTerms` to outside.

From:
```
"optionPayout": [
    {
        ...
        "exerciseTerms": {
            "optionStyle": {
                "europeanExercise": {
                    "expirationDate": [
                        {
                            "adjustableDate": {
                                "unadjustedDate": "2004-12-19",
                                "dateAdjustments": {
                                    "businessDayConvention": "NONE"
                                }
                            }
                        }
                    ],
                    "expirationTimeType": "OSP"
                }
            },
            "strike": {
                "strikePrice": {
                    "value": 8700,
                    "unit": {
                        "currency": {"value": "CHF"}
                    },
                    "perUnitOf": {"financialUnit": "IndexUnit"},
                    "priceType": "AssetPrice"
                }
            },
            "exerciseProcedure": {
                "automaticExercise": {"isApplicable": true}
            }
        },
        ...
    }
]
```

To this:
```
"optionPayout": [
    {
        ...
        "exerciseTerms": {
            "style": "European",
            "expirationDate": [
                {
                    "adjustableDate": {
                        "unadjustedDate": "2004-12-19",
                        "dateAdjustments": {
                            "businessDayConvention": "NONE"
                        }
                    }
                }
            ],
            "expirationTimeType": "OSP",
            "exerciseProcedure": {
                "automaticExercise": {"isApplicable": true}
            }
        },
        "strike": {
            "strikePrice": {
                "value": 8700,
                "unit": {
                    "currency": {"value": "CHF"}
                },
                "perUnitOf": {"financialUnit": "IndexUnit"},
                "priceType": "AssetPrice"
            }
        }
    }
]
```

- `eqd ex01 american call stock long form`: the `OptionStyle` has been removed in favor of the the `style` = "American", and the relevant fields previously under `americanExercise` (`commencementDate`, `expirationDate`, `latestExerciseTime`, `expirationTimeType`, and `multipleExercise`). Additionally, the `strike` is moved from inside `exerciseTerms` to outside.

From:
```
"optionPayout": [
    {
        ...
        "exerciseTerms": {
            "optionStyle": {
                "americanExercise": {
                    "commencementDate": {
                        "adjustableDate": {
                            "unadjustedDate": "2001-07-13",
                            "dateAdjustments": {
                                "businessDayConvention": "NONE"
                            }
                        }
                    },
                    "expirationDate": {
                        "adjustableDate": {
                            "unadjustedDate": "2005-09-27",
                            "dateAdjustments": {
                                "businessDayConvention": "NONE"
                            }
                        }
                    },
                    "latestExerciseTime": {
                        "hourMinuteTime": "17:15:00",
                        "businessCenter": {"value": "GBLO"}
                    },
                    "expirationTimeType": "Close",
                    "multipleExercise": {
                        "integralMultipleAmount": 1,
                        "minimumNumberOfOptions": 1,
                        "maximumNumberOfOptions": 150000
                    }
                }
            },
            "strike": {
                "strikePrice": {
                    "value": 32.00,
                    "unit": {
                        "currency": {"value": "EUR"}
                    },
                    "perUnitOf": {"financialUnit": "Share"},
                    "priceType": "AssetPrice"
                }
            },
            "exerciseProcedure": {
                "automaticExercise": {"isApplicable": true}
            }
        }
        ...
    }
]
```

To this:
```
"optionPayout": [
    {
        ...
        "exerciseTerms": {
            "style": "American",
            "commencementDate": {
                "adjustableDate": {
                    "unadjustedDate": "2001-07-13",
                    "dateAdjustments": {
                        "businessDayConvention": "NONE"
                    }
                }
            },
            "expirationDate": [
                {
                    "adjustableDate": {
                        "unadjustedDate": "2005-09-27",
                        "dateAdjustments": {
                            "businessDayConvention": "NONE"
                        }
                    }
                }
            ],
            "latestExerciseTime": {
                "hourMinuteTime": "17:15:00",
                "businessCenter": {"value": "GBLO"}
            },
            "expirationTimeType": "Close",
            "multipleExercise": {
                "integralMultipleAmount": 1,
                "minimumNumberOfOptions": 1,
                "maximumNumberOfOptions": 150000
            },
            "exerciseProcedure": {
                "automaticExercise": {"isApplicable": true}
            }
        },
        "strike": {
            "strikePrice": {
                "value": 32.00,
                "unit": {
                    "currency": {"value": "EUR"}
                },
                "perUnitOf": {"financialUnit": "Share"},
                "priceType": "AssetPrice"
            }
        }
    }
]
```

- `ird ex14 berm swaption`: the `OptionStyle` has been removed in favor of the the `style` = "Bermuda", and the relevant fields previously under `bermudaExercise` (`bermudaExerciseDates`, `relevantUnderlyingDate`, `earliestExerciseTime`, and `expirationTime`).

From:
```
"optionPayout": [
    {        
        ...    
        "exerciseTerms": {
            "optionStyle": {
                "bermudaExercise": {
                    "bermudaExerciseDates": {
                        "adjustableDates": {
                            "unadjustedDate": [
                                "2000-12-28",
                                "2001-04-28",
                                "2001-08-28"
                            ],
                            "dateAdjustments": {
                                "businessDayConvention": "FOLLOWING",
                                "businessCenters": {
                                    "businessCenter": [
                                        {"value": "EUTA"},
                                        {"value": "GBLO"}
                                    ]
                                }
                            }
                        }
                    },
                    "relevantUnderlyingDate": {
                        "relativeDates": {
                            "periodMultiplier": 2,
                            "period": "D",
                            "dayType": "Business",
                            "businessDayConvention": "NONE",
                            "businessCenters": {
                                "businessCenter": [
                                    {"value": "EUTA"},
                                    {"value": "GBLO"}
                                ]
                            },
                            "dateRelativeTo": {
                                "globalReference": "32e3a858",
                                "externalReference": "bermudaExercise0"
                            }
                        }
                    },
                    "earliestExerciseTime": {
                        "hourMinuteTime": "09:00:00",
                        "businessCenter": {"value": "BEBR"}
                    },
                    "expirationTime": {
                        "hourMinuteTime": "11:00:00",
                        "businessCenter": {"value": "BEBR"}
                    },
                    "meta": {
                        "globalKey": "32e3a858",
                        "externalKey": "bermudaExercise0"
                    }
                }
            },
            "exerciseProcedure": {
                "manualExercise": {
                    "exerciseNotice": {
                        "exerciseNoticeGiver": "Seller",
                        "businessCenter": {"value": "GBLO"}
                    }
                },
                "followUpConfirmation": true
            }
        }
        ...        
    }
]
```

To this:
```
"optionPayout": [
    {
        ...
        "exerciseTerms": {
            "style": "Bermuda",
            "exerciseDates": {
                "adjustableDates": {
                    "unadjustedDate": [
                        "2000-12-28",
                        "2001-04-28",
                        "2001-08-28"
                    ],
                    "dateAdjustments": {
                        "businessDayConvention": "FOLLOWING",
                        "businessCenters": {
                            "businessCenter": [
                                {"value": "EUTA"},
                                {"value": "GBLO"}
                            ]
                        }
                    }
                }
            },
            "relevantUnderlyingDate": {
                "relativeDates": {
                    "periodMultiplier": 2,
                    "period": "D",
                    "dayType": "Business",
                    "businessDayConvention": "NONE",
                    "businessCenters": {
                        "businessCenter": [
                            {"value": "EUTA"},
                            {"value": "GBLO"}
                        ]
                    },
                    "dateRelativeTo": {
                        "globalReference": "5c12e2ce",
                        "externalReference": "bermudaExercise0"
                    }
                }
            },
            "earliestExerciseTime": {
                "hourMinuteTime": "09:00:00",
                "businessCenter": {"value": "BEBR"}
            },
            "expirationTime": {
                "hourMinuteTime": "11:00:00",
                "businessCenter": {"value": "BEBR"}
            },
            "exerciseProcedure": {
                "manualExercise": {
                    "exerciseNotice": {
                        "exerciseNoticeGiver": "Seller",
                        "businessCenter": {"value": "GBLO"}
                    }
                },
                "followUpConfirmation": true
            },
            "meta": {
                "globalKey": "5c12e2ce",
                "externalKey": "bermudaExercise0"
            }
        }
    }
]
```

- **Misc. renaming or deletion**
  - `RBA_Bond_Basis`
    - Replaced the codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` and `RBA_BOND_BASIS_ANNUAL` with the code `RBA_BOND_BASIS` in the CDM enum `DayCountFractionEnum`.
    - Mapping added to populate the new code with the FpML code `RBA`.
  - `Portfolio Return Terms`
    - removed [deprecated] attributes below from type **PriceReturnTerms** :
      - **valuationPriceInitial**
      - and **valuationPriceFinal**
      - and **finalValuationPrice**
    - added attributes below to type **PerformancePayout** : that is core release to Dev of the same components previously released in Prod today :
      - creation of new type **PorfolioReturnTerms** which extends **ReturnTerms**
        - with existing types **PayerReceiver**
        - also with existing type **PriceSchedule** to **PerformancePayout** used under three attribute names, and **NonNegativeQuantitySchedule** used for one, respectively : **initialValuationPrice**, **interimValuationPrice**, **finalValuationPrice** and **quantity**
      - added the above new type to **PerformancePayout**
      - added existing type **PriceSchedule** to **PerformancePayout** used under three attribute names : **initialValuationPrice**, **interimValuationPrice** and **finalValuationPrice** (that is to replace [**deprecated**] ones removed from **ReturnTerms**)
      - updated type Basket with below changes :
        - removed temporary **PorfolioBasketConsituent** of type **BasketConstituent**
        - removed as well [deprecated] **basketConstituent** of type **Product**
        - added instead new attribute **basketConstituent** of type **BasketConstituent**
      - renamed attributes below in **ValuationDates** :
        - **initialValuationDate** (instead of **valuationDatesInitial**)
        - **interimValuationDate** (instead of **valuationDatesInterim**)
        - **finalValuationDate** (instead of **valuationDatesFinal**)
  - `Security Finance trade types`
    - Rename the `repoType` to `tradeType` on `AssetPayout`.
    - Rename `RepoTypeEnum` to `AssetPayoutTradeTypeEnum`.
    - The two product qualification functions have been updated to use the new names:
      - `Qualify_RepurchaseAgreement`
      - `Qualify_buySellBack`.
  - `Natural person`
    - Removed the personRole attribute of type NaturalPersonRole from NaturalPerson.
  
- **Misc. data validation changes**
  - `Exotic Equity Products and Exercise Terms validation conditions`
    - The `ExerciseTerms` validation change is backward incompatible and all affected samples have been updated to ensure that `expirationTimeType` is populated as `SpecificTime` when the `expirationTime` attribute is populated.
      - Attribute `expirationTime` relaxed to be optional (previously mandatory).
      - Attribute `expirationTimeType` tightened to be mandatory (previously optional).
      - Addition of validation condition `ExpirationTimeChoice` to establish the correlation between `expirationTime` and `expirationTimeType`: `expirationTimeType` must be set to `SpecificTime` when `expirationTime` is specified (and conversely).
    See for example: [`fpml-5-13 > fx-ex09-euro-opt`](https://github.com/finos/common-domain-model/blob/master/rosetta-source/src/main/resources/cdm-sample-files/fpml-5-13/products/fx-derivatives/fx-ex09-euro-opt.xml)
