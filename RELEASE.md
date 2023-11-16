# **CDM Version 5.0**

CDM 5.0, a production release, is the culmination of Common Domain Model (CDM) development releases from the second and third quarter of 2023 since CDM was made an open-source project at FINOS. There are also several technical changes since version 4.0 related to bug fixes, dependencies and synonym mappings.

## _What is being released_

Below are some of the high-level contributions that CDM 5.0 includes, more detail and additional contribution release notes can be found in the releases section of the CDM documentation:

- CDM Distribution - Excel Code Generation
- Infrastructure
  - Dependency updates
  - The order of JSON attributes now follows the order of the Rosetta model.
  - Enum values in JSON expectations now respect the naming convention of the Rosetta model.
- Product Model
  - Refactoring of Extraordinary Events and Substitution Provisions
  - Qualification - Bond Forwards
  - Commodity Payout - Commodity classification and Delivery features for Forwards and Options
  - Exchange Traded Derivatives – Extended SecurityTypeEnum and ForwardPayout
  - Price Components
  - Reference Index Information
  - Tri-party Agent Enumeration
  - Commodity Classification
  - Floating Rate Index Reference Data model – Extended FloatingRateIndexDefinition
- Event Model
  - Representation of trade valuations
  - Enhancements for Party Role Enum - Service Provider
  - Trade Date Time
  - Exchange Traded Derivatives - Position model
- Legal Documentation & Collateral
  - Modelling of Master Agreements (GMSLA covering securities lending transaction)
  - Lender Availability and Borrower Locates for securities lending
  - Functional model for assessing collateral eligibility

## _Backward-incompatible changes_

Below is a list of changes that have a backwards incompatible impact and therefore need to be taken into account when migrating from the previous major version of CDM.

**Commodity Reference Framework**

_Motivation For Change_

- This change upgrades the `ProductTaxonomy` type to accommodate a more generic representation of commodity classifications compatible with any classification systems.
- The `productTaxonomy` attribute inherited from the `ProductBase` type with the `Commodity` type was deemed adequate to document the classification rather than duplicate the information within the `referenceFramework` of the `commodityProductDefinition` attribute.

_Pull Request_

- https://github.com/finos/common-domain-model/pull/2297

_Backward-Incompatible Changes_

- Removed `commodityBase` and `subCommodity` attributes from the type `CommodityReferenceFramework`.
- Instead, that information is meant to be mapped to the `className` and `value` attributes under `productTaxonomy->value->classification`, with an optional `ordinal` attribute to specify the classification level.

_Sample Impact_

There are no samples that cover this change. Contributors can view the type `CommodityReferenceFramework` in the `cdm.base.staticdata.asset.common` namespace to see where the attributes have been removed, and inspect the `TaxonomyClassification` type in the `cdm.base.staticdata.asset.common` namespace to see where the information should be mapped.

**Price Refactoring**

_Motivation For Change_

A price that consists of two additive components (e.g. clean + accrued) is represented in different ways depending on the use case. This change introduces a single pattern intended to simplify the model and facilitate re-usability and extensibility.

_Pull Requests_

- https://github.com/finos/common-domain-model/pull/2292
- https://github.com/finos/common-domain-model/pull/2329

_Backward-Incompatible Changes_

- Removed the enumeration value `MultiplierOfIndexValue` from `PriceTypeEnum`
- Removed the following enumerations
  - `GrossOrNetEnum:`
  - `CleanOrDirtyPriceEnum`
  - `CapFloorEnum`
  - `SpreadTypeEnum`
- Removed the type `PriceExpression`
- Removed the field `grossOrNet` from type `CashflowType`

_Sample Impact_

- `eqs-ex01-single-underlyer-execution-long-form.json`: `NET` has been removed from the asset price and `ArithmeticOperator -> Add` is used instead of `spreadType -> Spread` in the interest rate price

From:

```JSON
"priceQuantity" : [ {
    "price" : [ {
        "value" : 37.44,
        "priceExpression" : {
            "grossOrNet" : "NET",
            "priceType" : "ASSET_PRICE"
        }
        ...
    } ]
    ...
} ],
"priceQuantity" : [ {
    "price" : [ {
        "value" : 0.0020,
        "priceExpression" : {
            "priceType" : "INTEREST_RATE",
            "spreadType" : "SPREAD"
        }
        ...
    } ]
    ...
} ]

```

To this:

```JSON
"priceQuantity" : [ {
    "price" : [ {
        "value" : 37.44,
        "priceType" : "ASSET_PRICE"
        ...
    } ]
    ...
} ],
"priceQuantity" : [ {
    "price" : [ {
        "value" : 0.0020,
        "arithmeticOperator" : "ADD",
        "priceType" : "INTEREST_RATE"
        ...
    } ]
    ...
} ]

```

- `ird-ex22-cap.json`: uses `ArithmeticOperator -> Min` instead of `CapFloor -> Cap`

From:

```JSON

"price" : [ {
    "value" : 0.06
    "priceExpression" : {
        "capFloor" : "CAP",
        "priceType" : "INTEREST_RATE"
        }
    ...
} ]

```

To this:

```JSON

"price" : [ {
    "value" : 0.06,
    "arithmeticOperator" : "MIN",
    "priceType" : "INTEREST_RATE"
    ...
} ]

```

- `ird-ex27-inverse-floater.json`: there are now 2 price objects, that use respectively `ArithmeticOperator -> Add` instead of `SpreadType -> Spread` and `ArithmeticOperator -> Multiply` instead of `PriceType -> MultiplierOfIndexValue`

From:

```JSON

"tradableProduct" : {
    "product" : {
        "contractualProduct" : {
            "economicTerms" : {
                "payout" : {
                    "interestRatePayout" : [ {
                        "rateSpecification" : {
                            "floatingRate" : {
                                ...
                                "floatingRateMultiplierSchedule" : {
                                    "price" : {
                                        "address" : {
                                        "scope" : "DOCUMENT",
                                        "value" : "price-1"
                                        }
                                    }
                                }
                            }
                        }
                    } ]
                }
            }
        }
    }
},
"tradeLot" : [ {
    "priceQuantity" : [ {
        "price" : [ {
            "location" : [ {
                "scope" : "DOCUMENT",
                "value" : "price-1"
            } ],
            "value" : {
                "unit" : {
                    "currency" : {
                        "value" : "USD"
                    }
                },
                "value" : -1.0,
                "priceExpression" : {
                    "spreadType" : "SPREAD",
                    "priceType" : "MULTIPLIER_OF_INDEX_VALUE"
                }
            }
        } ]
    } ]
} ]


```

To this:

```JSON
"tradableProduct" : {
    "product" : {
        "contractualProduct" : {
            "economicTerms" : {
                "payout" : {
                    "interestRatePayout" : [ {
                        "rateSpecification" : {
                            "floatingRate" : {
                                ...
                                "spreadSchedule" : {
                                    "price" : {
                                        "address" : {
                                            "scope" : "DOCUMENT",
                                            "value" : "price-1"
                                        }
                                    }
                                },
                                "floatingRateMultiplierSchedule" : {
                                    "price" : {
                                        "address" : {
                                            "scope" : "DOCUMENT",
                                            "value" : "price-2"
                                        }
                                    }
                                }
                            }
                        }
                    } ]
                }
            }
        }
    }
},
"tradeLot" : [ {
    "priceQuantity" : [ {
        "price" : [ {
            "location" : [ {
                "scope" : "DOCUMENT",
                "value" : "price-1"
            } ],
            "value" : {
                "unit" : {
                    "currency" : {
                        "value" : "USD"
                    }
                },
                "value" : 0.085,
                "arithmeticOperator" : "ADD",
                "perUnitOf" : {
                    "currency" : {
                        "value" : "USD"
                    }
                },
                "priceType" : "INTEREST_RATE"
            }
        }, {
            "location" : [ {
                "scope" : "DOCUMENT",
                "value" : "price-2"
            } ],
            "value" : {
                "unit" : {
                    "currency" : {
                        "value" : "USD"
                    }
                },
                "value" : -1.0,
                "arithmeticOperator" : "MULTIPLY",
                "perUnitOf" : {
                    "currency" : {
                        "value" : "USD"
                    }
                },
                "priceType" : "INTEREST_RATE"
            }
        } ]
    } ]
} ]


```

- `fx-ex03-fx-fwd.json`: uses the new `PriceComposite` component with `ArithmeticOperator -> Add`, `OperandType -> ForwardPoint` and the `baseValue` and `operand` attributes for the values previously associated with `SpreadType -> Base` and `SpreadType -> Spread`, respectively.

From:

```JSON

"price" : [ {
    "unit" : {
        "currency" : {
            "value" : "USD"
        }
    },
    "value" : 0.9175,
    "perUnitOf" : {
        "currency" : {
            "value" : "EUR"
        }
    },
    "priceExpression" : {
        "priceType" : "EXCHANGE_RATE"
    }
}, {
    "unit" : {
        "currency" : {
            "value" : "USD"
        }
    },
    "value" : 0.9130,
    "perUnitOf" : {
        "currency" : {
            "value" : "EUR"
        }
    },
    "priceExpression" : {
        "priceType" : "EXCHANGE_RATE",
        "spreadType" : "BASE"
    }
}, {
    "unit" : {
        "currency" : {
            "value" : "USD"
        }
    },
    "value" : 0.0045,
    "perUnitOf" : {
        "currency" : {
            "value" : "EUR"
        }
    },
    "priceExpression" : {
        "priceType" : "EXCHANGE_RATE",
        "spreadType" : "SPREAD"
    }
} ]

```

To this:

```JSON

"price" : [ {
    "unit" : {
        "currency" : {
            "value" : "USD"
        }
    },
    "value" : 0.9175,
    "composite" : {
        "arithmeticOperator" : "ADD",
        "baseValue" : 0.9130,
        "operand" : 0.0045,
        "operandType" : "FORWARD_POINT"
    },
    "perUnitOf" : {
        "currency" : {
            "value" : "EUR"
        }
    },
    "priceType" : "EXCHANGE_RATE"
} ]

```

**Extraordinary Events**

_Motivation For Change_

The description and determination of extraordinary events and substitution provisions do not pertain to economic terms of financial products. They should be positioned within the legal agreement that conditions the performance of the transaction.

_Pull Requests_

- https://github.com/finos/common-domain-model/pull/2435

_Backward-Incompatible Changes_

- Removed enumeration `NationalizationOrInsolvencyOrDelistingEventEnum`
- Removed the following types:
  - `AdditionalDisruptionEvents`
  - `ExtraordinaryEvents`
  - `IndexAdjustmentEvents`
  - `EquityCorporateEvents`
  - `Representations`

_Sample Impact_

- `eqs-ex01-single-underlyer-execution-long-form-other-party`: see impact on the `economicTerms` attribute .

From:

```JSON

"economicTerms" : {
    "effectiveDate" : {...},
    "terminationDate" : {...},
    "payout" : {...},
    "extraordinaryEvents" : {
        "mergerEvents" : {
            "shareForShare" : "ModifiedCalculationAgent",
            "shareForOther" : "ModifiedCalculationAgent",
            "shareForCombined" : "ModifiedCalculationAgent"
        },
        "tenderOfferEvents" : {
            "shareForShare" : "ModifiedCalculationAgent",
            "shareForOther" : "ModifiedCalculationAgent",
            "shareForCombined" : "ModifiedCalculationAgent"
        },
        "compositionOfCombinedConsideration" : true,
        "additionalDisruptionEvents" : {
            "changeInLaw" : true,
            "failureToDeliver" : true,
            "insolvencyFiling" : false,
            "hedgingDisruption" : true,
            "increasedCostOfHedging" : false,
            "lossOfStockBorrow" : true,
            "increasedCostOfStockBorrow" : false,
            "determiningParty" : "DisruptionEventsDeterminingParty"
        },
        "representations" : {
            "nonReliance" : true,
            "agreementsRegardingHedging" : true,
            "additionalAcknowledgements" : true
        },
        "nationalizationOrInsolvency" : "CancellationAndPayment"
        },
    "calculationAgent" : {
        "calculationAgentParty" : "CalculationAgentIndependent"
    }
}

```

```JSON

"ancillaryParty" : [ {
    "role" : "DisruptionEventsDeterminingParty",
    "partyReference" : [ {
        "globalReference" : "33f59569",
        "externalReference" : "party3"
        } ]
    }, {
    "role" : "ExtraordinaryDividendsParty",
    "partyReference" : [ {
        "globalReference" : "33f59569",
        "externalReference" : "party3"
    } ]
    }, {
    "role" : "CalculationAgentIndependent",
    "partyReference" : [ {
        "globalReference" : "33f59569",
        "externalReference" : "party3"
    } ]
} ]

```

To this:

```JSON

"economicTerms" : {
    "effectiveDate" : {...},
    "terminationDate" : {...},
    "payout" : {...},
    "calculationAgent" : {
        "calculationAgentParty" : "CalculationAgentIndependent"
    }
}

```

```JSON

"ancillaryParty" : [ {
    "role" : "ExtraordinaryDividendsParty",
    "partyReference" : [ {
        "globalReference" : "33f59569",
        "externalReference" : "party3"
    } ]
    }, {
    "role" : "CalculationAgentIndependent",
    "partyReference" : [ {
        "globalReference" : "33f59569",
        "externalReference" : "party3"
    } ]
} ]

```

**Miscellaneous: Type Renaming**

The following types were renamed:

- `FloatingRateIndexIndentification` to `FloatingRateIndexIdentification` (fixing a typo)
- `IndexReferenceInformation` to `CreditIndexReferenceInformation`, while `CreditIndexReferenceInformation` extends the non-credit specific `IndexReferenceInformation`
- `Valuation` to `ValuationTerms`

**Miscellaneous: Attribute Type or Cardinality Change**

The following attributes had their type or cardinality values changed:

- For type `AggregationParameters` the attribute `productQualifier productType (0..*)` was changed to `productQualifier string (0..*)`
- For type `CollateralProvisions` the attribute `eligibleCollateral EligibleCollateralSpecification (0..*)` was changed to `eligibleCollateral EligibleCollateralCriteria (0..*)`

**Miscellaneous: Enum Value Name Change**

- All enumerated values use the model's native naming convention when serialised into JSON (using the `displayName` when it exists). For instance, the `CounterpartyRoleEnum` value `Party1` is serialised as "Party1" instead of "PARTY_1"
- The following list of enumerations had values that were changed:
  - All of the enumeration values for `ContractualDefinitionsEnum` were renamed, for example `ISDA1999Credit` was renamed to `ISDA1999CreditDerivatives`

Changing the following condition from:

```Haskell

if contractDetails -> documentation -> legalAgreementIdentification -> agreementName
    -> contractualDefinitionsType any = ContractualDefinitionsEnum -> ISDA1999Credit

```

To this:

```Haskell

if contractDetails -> documentation -> legalAgreementIdentification -> agreementName
    -> contractualDefinitionsType any = ContractualDefinitionsEnum -> ISDA1999CreditDerivatives

```

**Function Removal**

Several creation functions were removed as they were no longer required after support for instance creation was added natively to the Rosetta DSL.

_Pull Requests_

- https://github.com/finos/common-domain-model/pull/2381

The list of removed functions is as follows:

- `Create_NonNegativeQuantity`
- `Create_NonNegativeQuantitySchedule`
- `Create_UnitType`
- `Create_Counterparty`
- `Create_PartyRole`
- `Create_PayerReceiver`
- `Create_ExecutionInstruction`
- `Create_QuantityChangeInstruction`
- `Create_CashflowType`
- `Create_ClosedState`
- `Create_PrimitiveInstruction`
- `Create_LegalAgreementWithPartyReference`
- `Create_Price`
- `Create_PriceSchedule`
- `Create_PriceQuantity`
- `Create_ResolvablePriceQuantity`
- `Create_TradeLot`
- `Create_TradableProduct`
