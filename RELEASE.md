
# _Product Model - OptionPayout Refactoring_

_Background_

In order to reduce redundancy and overcomplexity in the CDM, a refactoring of the `OptionPayout` structure is required. The information contained in the fields inside the `optionStyle` (`americanExercise`, `europeanExercise`, and `bermudaExercise`) can be unified under a new type `ExerciseTerms`. This will reduce the redundancy of having the same information repeated under the three styles and improve the simplicity of the model. To distinguish whether the option is American, European, or Bermuda, a new `style` enumeration is added to the model inside `ExerciseTerms`, with the values "american", "european", and "bermuda". Additionally, the `strike` field, previously under `exerciseTerms`, is moved outside and located directly under `OptionPayout`, given that it does not convey any information about the exercise terms of an option.

_What is being released?_

- The `optionStyle` is removed from the model, along with the `americanExercise`, `europeanExercise`, and `bermudaExercise` fields.
- A new `ExerciseTerms` structure is added to `OptionPayout`, containing all _distinct_ fields found previously under the three exercise styles.
- A new `style` enumeration is added under `ExerciseTerms` to distinguish the style of the option. This enumeration is made **optional** to account the exercise terms of a `CancelableProvision`, `ExtendibleProvision`, or `OptionalEarlyTermination`, where the style is often derived from the structure itself.
- The structures `CancelableProvision`, `ExtendibleProvision`, and `OptionalEarlyTermination` have been modified to use `ExerciseTerms` instead of the old `americanExercise`, `europeanExercise`, and `bermudaExercise` fields.
- **Synonym mappings** have been modified to reflect these changes.

_Data types_

- Removed `OptionExercise` type.
- Removed `OptionStyle` type.
- Removed `AmericanExercise`, `EuropeanExercise`, and `BermudaExercise` types.
- `strike` attribute of type `OptionStrike` added to `OptionPayout` type.
- `exerciseTerms` attribute of type `ExerciseTerms` added to `OptionPayout` type.
- `exerciseTerms` attribute of type `OptionExercise` removed from `OptionPayout` type.
- `americanExercise`, `europeanExercise`, and `bermudaExercise` attributes of types `AmericanExercise`, `EuropeanExercise`, and `BermudaExercise` respectively removed from `CancelableProvision`, `ExtendibleProvision`, and `OptionalEarlyTermination` types.
- `exerciseTerms` attribute of type `ExerciseTerms` added to `CancelableProvision`, `ExtendibleProvision`, and `OptionalEarlyTermination` types.
-  Added new `ExerciseTerms` type with all of the distinct fields present before in `AmericanExercise`, `EuropeanExercise`, and `BermudaExercise` types.

_Enumerations_

- Added new `OptionExerciseStyleEnum` enumeration.

- _Backward Incompatible Changes_


- _Motivation for Change_

This change modifies the `OptionPayout` structure to simplify it and reduce the redundancy and over-complexity of the model.

Specifically:
- It removes the `OptionStyle` type from the `exerciseTerms` field, along with the three option exercise types contained inside it (`AmericanExercise`, `EuropeanExercise`, `BermudaExercise`), and replaces it with the distinct group of fields required to represent any type of option style.
- The type `OptionExercise` is removed from the model, and instead the type `ExerciseTerms` is used for the field `exerciseTerms`.
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
                                    "businessDayConvention": "NONE",
                                    "meta": {"globalKey": "24a738"}
                                },
                                "meta": {"globalKey": "eb46d18b"}
                            },
                            "meta": {"globalKey": "eb46d18b"}
                        }
                    ],
                    "expirationTimeType": "OSP",
                    "meta": {"globalKey": "7d9492c1"}
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
                            "businessDayConvention": "NONE",
                            "meta": {"globalKey": "24a738"}
                        },
                        "meta": {"globalKey": "eb46d18b"}
                    },
                    "meta": {"globalKey": "eb46d18b"}
                }
            ],
            "expirationTimeType": "OSP",
            "exerciseProcedure": {
                "automaticExercise": {"isApplicable": true}
            },
            "meta": {"globalKey": "e3d825ff"}
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
        "meta": {"globalKey": "dbfc720b"}
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
                                "businessDayConvention": "NONE",
                                "meta": {"globalKey": "24a738"}
                            },
                            "meta": {"globalKey": "eae7f1c5"}
                        },
                        "meta": {"globalKey": "eae7f1c5"}
                    },
                    "expirationDate": {
                        "adjustableDate": {
                            "unadjustedDate": "2005-09-27",
                            "dateAdjustments": {
                                "businessDayConvention": "NONE",
                                "meta": {"globalKey": "24a738"}
                            },
                            "meta": {"globalKey": "eb6226d3"}
                        },
                        "meta": {"globalKey": "eb6226d3"}
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
                    },
                    "meta": {"globalKey": "221944d2"}
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
                        "businessDayConvention": "NONE",
                        "meta": {"globalKey": "24a738"}
                    },
                    "meta": {"globalKey": "eae7f1c5"}
                },
                "meta": {"globalKey": "eae7f1c5"}
            },
            "expirationDate": [
                {
                    "adjustableDate": {
                        "unadjustedDate": "2005-09-27",
                        "dateAdjustments": {
                            "businessDayConvention": "NONE",
                            "meta": {"globalKey": "24a738"}
                        },
                        "meta": {"globalKey": "eb6226d3"}
                    },
                    "meta": {"globalKey": "eb6226d3"}
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
            },
            "meta": {"globalKey": "aedc32dd"}
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
        "meta": {"globalKey": "37ca535d"}
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
                            "meta": {"globalKey": "c6c3b1ab"},
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
                    "meta": {"globalKey": "c6c3b1ab"},
                    "dayType": "Business",
                    "businessDayConvention": "NONE",
                    "businessCenters": {
                        "businessCenter": [
                            {"value": "EUTA"},
                            {"value": "GBLO"}
                        ],
                        "meta": {"globalKey": "4158421"}
                    },
                    "dateRelativeTo": {
                        "globalReference": "5c12e2ce",
                        "externalReference": "bermudaExercise0"
                    }
                },
                "meta": {"globalKey": "c6c3b1ab"}
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

_Review directions_

In Rosetta platform, select the Textual View and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2716

# _Product Model - Qualifying functions: Enhanced support for ETDs_

_Background_

In CDM there is no support for Exchange-traded derivatives (ETDs) in some qualifying functions. This contribution enhances the support for ETD in `Qualify_AssetClass_InterestRate` and `Qualify_AssetClass_Commodity` when an optionUnderlier exists in the sample at "security" level.

_What is being released?_

- Added support for optionUnderlier when is ETD for `Qualify_AssetClass_InterestRate`.
- Added support for optionUnderlier when is ETD for Com Opt in `Qualify_AssetClass_Commodity`.

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2732

# _Legal Agreements - Master Agreement Type enumeration - ISDAIIFM_TMA code_

_Background_

In CDM there are not any available codes for Islamic Master Agreements. This contribution adds a new `ISDAIIFM-TMA` code to the `MasterAgreementTypeEnum` to cover the Islamic Derivative Master Agreements in CDM.

_What is being released?_

- Added a new `ISDAIIFM_TMA` code to the MasterAgreementTypeEnum.
- Mapping coverage for this new `ISDAIIFM_TMA` code.

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2724

# _Mapping - Settlement Type Mapping Fix_

_Background_

It had been observed that some of the ingested FpML samples in the Rosetta Platform were not correctly translating the settlement type of the trade. Specifically, the field `settlementTerms->settlementType` in CDM was not being populated, despite the incoming FpML samples containing information about the settlement type. This release aims at correcting this mismatch between FpML and CDM. In turn, this will improve the reporting capabilities of the DRR field `DeliveryType`, given that several samples which were previously not reporting this field will now contain the necessary information to do so.

_What is being released?_

- Updates in the synonym mappings have been incorporated to populate the `settlementType` field in CDM whenever FpML contains the fields `cashSettlement`, `amount->cashSettlement`, `tradeHeader->productSummary->settlementType`, or `genericProduct->settlementType`.

_Translate_

- Added `[hint "productSummary"]` to the `tradeLot` in the `TradableProduct` synonym.
- Added `[value "creditDefaultSwap" , "creditDefaultSwap" path "creditDefaultSwapOption" , "creditDefaultSwapOption", "tradeHeader"]` to the `tradeLot` in the `TradableProduct` synonym.
- Added `[hint "settlementType"]` to the `settlementTerms` in the `PriceQuantity` synonym.
- Added `[set to SettlementTypeEnum -> Cash when "cashSettlement" exists]` to the `settlementType` in the `SettlementBase` synonym.
- Added `[set to SettlementTypeEnum -> Cash when "settlementType" = "Cash"]` to the `settlementType` in the `SettlementBase` synonym.
- Added `[set to SettlementTypeEnum -> Cash when "amount->cashSettlement" = True]` to the `settlementType` in the `SettlementBase` synonym.
- Added `[set to SettlementTypeEnum -> Physical when "settlementType" = "Physical"]` to the `settlementType` in the `SettlementBase` synonym.

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2730

# _Mapping - Commodity Classification Coverage_

_Background_

Following the addition of commodity classification structures in the Rosetta Platform, there is now a need to incorporate mappings from the corresponding FpML fields to the new CDM fields related to the classification of commodities.

Specifically, to cover the CDM mappings to:
- `underlier->commodity->productTaxonomy->value->classification->value` and
- `underlier->commodity->productTaxonomy->value->classification->ordinal`

_What is being released?_

- Synonym mappings that map:
    - `commodityClassification->code` to `classification->value`.
    - `commodityClassification->code->commodityClassificationScheme` to `classification->ordinal`.

- The `ordinal` is mapped to values `1`, `2`, or `3` as follows:
    - If `commodityClassificationScheme` = _http://www.fpml.org/coding-scheme/esma-emir-refit-layer-1-commodity-classification_, then `ordinal` = 1
    - If `commodityClassificationScheme` = _http://www.fpml.org/coding-scheme/esma-emir-refit-layer-2-commodity-classification_, then `ordinal` = 2
    - If `commodityClassificationScheme` = _http://www.fpml.org/coding-scheme/esma-emir-refit-layer-3-commodity-classification_, then `ordinal` = 3

_Mappings_

- `[value "floatingLeg", "oilPhysicalLeg"]` is added to `priceQuantity` in the `TradeLot` synonym.
- `[hint "commodityClassification"]` is added to `commodity` in the `Observable` synonym.
- `[value "code"]` is added to `value` in the `TaxonomyClassification` synonym.
- `[hint "commodityClassification"]` is added to the `observable` in the `PriceQuantity` synonym.
- `[set to 1 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-1-commodity-classification"]` is added to `ordinal` in the `TaxonomyClassification` synonym.
- `[set to 2 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-2-commodity-classification"]` is added to `ordinal` in the `TaxonomyClassification` synonym.
- `[set to 3 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-3-commodity-classification"]` is added to `ordinal` in the `TaxonomyClassification` synonym.
- `[set to 1 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/isda-layer-1-commodity-classification"]` is added to `ordinal` in the `TaxonomyClassification` synonym.
- `[set to 2 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/isda-layer-2-commodity-classification"]` is added to `ordinal` in the `TaxonomyClassification` synonym.
- `[set to 3 when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/isda-layer-3-commodity-classification"]` is added to `ordinal` in the `TaxonomyClassification` synonym.
- `[set to TaxonomySourceEnum -> ISDA when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/isda-layer-1-commodity-classification"]` is added to `source` in the `ProductTaxonomy` synonym.
- `[set to TaxonomySourceEnum -> EMIR when "code->commodityClassificationScheme" = "http://www.fpml.org/coding-scheme/esma-emir-refit-layer-1-commodity-classification"]` is added to `source` in the `ProductTaxonomy` synonym.
- `[hint "primaryAssetClass"]`, `[hint "secondaryAssetClass"]`, `[hint "fra"]`, `[hint "creditDefaultSwapOption"]`, `[hint "bondOption"]`, `[hint "commoditySwaption"]`, `[hint "genericProduct"]`, `[hint "productType"]`, and `[value "commodityClassification"]` are added to the `productTaxonomy` in the `ProductBase` synonym.


_Review directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes listed above.

Changes can be reviewed in PR:https://github.com/finos/common-domain-model/pull/2735
