# CDM Version 6.0

CDM 6.0, a production release, corresponds to developments made to the CDM throughout 2024 and previously released as CDM 6-dev. These developments include items that featured in the 2024 CDM roadmap:

- Option Payout Refactoring
- Asset Refactoring
- Standardized IM Schedule

as well as several additional model changes, bug fixes, dependencies updates and synonym mappings since the latest version 5.

## _What is being released_

Below are some of the high-level changes included in CDM 6.0, with links to their corresponding development release tags containing more detailed release notes.

### _Asset refactoring_

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

### _Option Payout refactoring_
  
- Option Payout Refactoring: **Backward incompatible changes** [6.0.0-dev.24](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.24)
- Modification of AmericanExercise Condition in ExerciseTerms: [6.0.0-dev.41](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.41)

### _Standardized IM Schedule_

- Implementation of the Standardized Schedule Method for Initial Margin Calculation: [6.0.0-dev.69](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.69)
- Enhancement and Optimization of the Standardized Schedule Method: [6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90)

### _Misc. model changes_

- Product model
  - Day Count Fraction: RBA_Bond_Basis: **Backward incompatible changes** [6.0.0-dev.22](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.22)
  - Trigger type refactoring: [6.0.0-dev.42](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.42)
  - Principal Amount Conditions: [6.0.0-dev.43](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.43)
  - Portfolio Return Terms: **Backward incompatible changes** [6.0.0-dev.55](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.55)
  - Security Finance trade types: **Backward incompatible changes** [6.0.0-dev.86](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.86)
  - Exotic Equity Products and Exercise Terms validation conditions: **Backward incompatible changes** [6.0.0-dev.88](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.88)
  - Qualification functions:
    - Zero Coupon Swaps: [6.0.0-dev.13](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.13)
    - Bond Option and Forwards: [6.0.0-dev.32](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.32)
- Event model
  - Addition of new enumeration to AvailableInventory: [6.0.0-dev.36](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.36)
- Eligible Collateral model
  - Determination of the Party Roles: [6.0.0-dev.4](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.4)
  - CheckEligibilityResult cardinality fix: [6.0.0-dev.10](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.10)
  - CreditNotationMismatchResolutionEnum update: [6.0.0-dev.26](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.26)
  - New Attributes: [6.0.0-dev.48](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.48)
  - Collateral Criteria AND/OR Logic: [6.0.0-dev.90](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.90)
- Base model
  - Natural Person and NaturalPersonRole circular reference: **Backward incompatible changes** [6.0.0-dev.3](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.3)
  - RoundToPrecision Function: [6.0.0-dev.40](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.40) & [6.0.0-dev.74](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.74)
  - TaxonomySourceEnum: [6.0.0-dev.85](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.85)

### _FpML mappings_
  
- Synonym mappings for BusinessCenterEnum: [6.0.0-dev.33](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.33)
- Related party role mapper: [6.0.0-dev.81](https://github.com/finos/common-domain-model/releases/tag/6.0.0-dev.81)

### _Infrastructure_

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

### _Asset refactoring_
  - To be completed...

### _Option Payout refactoring_
   
_Data types and enumeration changes_

- Removed `OptionStyle` type and the three option exercise types contained inside it: `AmericanExercise`, `EuropeanExercise`, `BermudaExercise`.
- Replaced by new `OptionExerciseStyleEnum` enumeration with values `American`, `European` and `Bermuda`.
- Removed `OptionExercise` type.
- Replaced by new `ExerciseTerms` type, containing:
  - all of the distinct attributes present before in `AmericanExercise`, `EuropeanExercise`, and `BermudaExercise` types,
  - a `style` attribute of type `OptionExerciseStyleEnum`,
  - and the `exerciseProcedure` attribute of type `ExerciseProcedure` that was previously contained in `OptionExercise`.
- Switched `exerciseTerms` attribute in `OptionPayout` type to use the new `ExerciseTerms` type instead of the removed `OptionExercise` type.
- Moved the `strike` attribute previously contained in `OptionExercise` type to `OptionPayout` type.
- Removed `americanExercise`, `europeanExercise`, and `bermudaExercise` attributes from `CancelableProvision`, `ExtendibleProvision`, and `OptionalEarlyTermination` types.

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

### _Misc. renaming or deletion_

- RBA Bond Basis
  - Replaced the codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` and `RBA_BOND_BASIS_ANNUAL` with the code `RBA_BOND_BASIS` in `DayCountFractionEnum`.
  - Mapping added to populate the new code from the FpML code `RBA`.
- Portfolio Return Terms
  - Removed `[deprecated]` attributes below from type `PriceReturnTerms`:
    - `valuationPriceInitial`
    - `valuationPriceFinal`
  - Replaced by existing attributes in `PerformancePayout`:
    - `initialValuationPrice`
    - `finalValuationPrice`
  - Renamed attributes in `ValuationDates`:
    - `initialValuationDate` instead of `valuationDatesInitial`
    - `interimValuationDate` instead of `valuationDatesInterim`
    - `finalValuationDate` instead of `valuationDatesFinal`
  - Updated `Basket` type:
    - Renamed `portfolioBasketConstituent` of type `BasketConstituent` as simply `basketConstituent`.
    - Removed `[deprecated]` `basketConstituent` attribute, previously of type `Product`.
- Security Finance trade types
  - Renamed `RepoTypeEnum` to `AssetPayoutTradeTypeEnum`.
  - Renamed `repoType` to `tradeType` in `AssetPayout`.
- Natural person
    - Removed the `personRole` attribute of type `NaturalPersonRole` from `NaturalPerson`.
    - Replaced by existing `personRole` attribute in `Party`.
  
### _Misc. data validation changes_
  
- Exotic Equity Products and Exercise Terms validation conditions
  - Attribute `expirationTime` relaxed to be optional (previously mandatory).
  - Attribute `expirationTimeType` tightened to be mandatory (previously optional).
  - Addition of validation condition `ExpirationTimeChoice` to establish the correlation between `expirationTime` and `expirationTimeType`.
  - Affected samples have been updated to ensure that `expirationTimeType` is populated as `SpecificTime` when the `expirationTime` attribute is populated. See for example: [`fpml-5-13 > fx-ex09-euro-opt`](https://github.com/finos/common-domain-model/blob/master/rosetta-source/src/main/resources/cdm-sample-files/fpml-5-13/products/fx-derivatives/fx-ex09-euro-opt.xml).
