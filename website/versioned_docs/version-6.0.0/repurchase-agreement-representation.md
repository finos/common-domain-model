---
title: Repurchase Transaction Representation in the CDM
---

# Background

The FINOS CDM for Repo and Bonds initiative is intended to support the
digitalization of the repo market through the adoption of a standardized
domain model and lifecycle events based on industry best practices and
standards.

The ICMA promotes adoption of CDM by organizing working groups and
workshops on proof-of-concept projects and integration.

The repo elements of the model were designed with participation and
contribution of the FINOS CDM for Repo and Bonds Steering Committee.

# Introduction

The repo product model in the CDM does not follow any pre-defined
taxonomy such as FpML or any regulatory model such as SFTR. Instead the
repo model is agnostic to external models, messaging standards and
technology. The fundamental concepts in the repo model are based on
industry definitions of financial products, the GMRA and the ERCC Best
Practices Guide.

Repurchase transactions are represented in the CDM as contractual
products, which are products with custom payout structures and assets.
The types of repo products that can be defined in the CDM is almost
unlimited using the `InterestRatePayout` and `AssetPayout` structure. At
a minimum the model supports basic repo transactions, fixed term repo,
open repo, fixed rate and floating rate with optionality for early
termination, evergreen and extendable. The model also support
buy/sell-back transactions.

Repo lifecycle events are supported through a set of functions that
accept a small set of inputs to auto-generate primitive instructions
needed to execute business events. Repo lifecycle events include,
rolling, re-rating, interim payments, pair-off and shaping.

# Benefits

The benefits of using the CDM for repo transactions is that it provides
a standard digital representation of the data required to define a repo
product and a standardized set of lifecycle events.

Examples of where the CDM can be used in the repo market:

-   Post-trade matching using a single digital object.
-   Lifecycle event processing across counterparties and settlement
    services.
-   Internal and external reporting.
-   Capturing event history.
-   Market standard taxonomy and mapping interface to other platforms.

ICMA Contacts: 

| Contact | Role | Email | Phone
| :--- | :--- | :--- | :--- | 
| Gabriel Callsen | Director | [gabriel.callsen@icmagroup.org](mailto:gabriel.callsen@icmagroup.org) | +44 (0)20 7213 0334 |
| Tom Healey | FINXIS LLC, Consultant | [tom.healey@icmagroup.org](mailto:tom.healey@icmagroup.org) |

# Scope

The scope of the CDM Repo initiative was intended to define:

-   Consistent definition of buyer-seller entities based on LEI data
-   Collateral, margin and haircut attributes.
-   Flexible interest rate payout model to support complex structures.
-   Standardized product taxonomy.
-   Predefine lifecycle event processing and event history.

# Repo Product Model

Building upon the CDM, the Repo CDM added new data types,
attributes and events needed to create fixed term, open and floating
rate repos, and execute various lifecycle events.

A repo product is composed as a contracted financial product in the 
normal manner (see the [Product Model](/docs/product-model) section for more details).
The payout structure for a repo is constructed using an
`InterestRatePayout` for the cash loan and `AssetPayout` for the
collateral.

## Payer and Receiver

The `InterestRatePayout` object must also define the payer and receiver.
They payer and receiver are linked to the trade object that defines the
counterparty and partyrole. In a repo transaction, the seller
(collateral giver - borrower) will be the payer and the buyer(collateral
taker -- lender) will be receiver. The payer and receiver are extensions
from the PayoutBase.

## Collateral

Collateral on a repo transaction is defined using `AssetPayout`.
Security identification is set in the `securityInformation` attribute,
which is a `Security` Asset. Collateral may also be
defined using parameters such as currency, country, maturity and other
attributes available in `CollateralProvisions` to classify.

## Purchase Date and Repurchase Date

In `economicTerms`, the `effectiveDate` attribute represents the repo
purchase date and the `terminationDate` is the repurchase date. For an
open repo the `terminationDate` is not set until the repo terminates.
The external and global key references should include "PurchaseDate" and
"RepurchaseDate":

``` Javascript
"effectiveDate": {
    "adjustableDate": {
        "dateAdjustments": {
            "businessCenters": {
                "businessCenter": [
                    {
                        "value": "GBLO"
                    }
                ]
            },
            "businessDayConvention": "NONE"
        },
        "unadjustedDate": "2023-06-16"
    },
    "meta": {
        "externalKey": "PurchaseDate",
        "globalKey": "PurchaseDate"
    }
}
```

``` Javascript
"terminationDate": {
     "adjustableDate": {
        "dateAdjustments": {
            "businessDayConvention": "NONE",
                "meta": {
                    "externalKey": "RepurchaseDate",
                    "globalKey": "RepurchaseDate"
                }
            },
        "unadjustedDate": "2023-06-17"
    }
}
```

Repurchase transactions should also include tags to identify the
purchase price and repurchase price. In the `interestRatePayout` and
purchase price is set on the `priceQuantity` and the `initialPayment`:

``` Javascript
"priceQuantity": {
    "meta": {
        "externalKey": "PurchasePrice"
    },
    "quantitySchedule": {
        "value": {
            "unit": {
                "currency": {
                    "value": "GBP"
                }
            },
            "value": 9879046.8
        }
    },
    "resolvedQuantity": {
        "unit": {
            "currency": {
                "value": "GBP"
            }
        },
        "value": 9879046.8
    }
}
```

Repurchase transactions should also include the a legal agreement object
with reference to the GMRA or other private agreement by adding the
legalAgreementIdentification object:

``` Javascript
"contractDetails": {
    "documentation": [
         {
            "legalAgreementIdentification": {
                "agreementName": {
                    "masterAgreementType": {
                        "value": "GMRA"
                    }
                },
                "governingLaw": "GBEN",
                "publisher": "ICMA",
                "vintage": 2011
            }
        }
    ]
}
```

Collateral is defined in `assetPayout->securityInformation`:

``` Javascript
"securityInformation": {
    "security": {
        "identifier": [
            {
               "identifier": {
                   "value": "GB00B24FF097"
               },
               "identifierType": "ISIN"
                }],
        "securityType": "DEBT"
        }
    }
}
```

Repurchase transactions are classified as a `MoneyMarket` types using
`AssetClassEnum`, but this attribute is optional and is not required to
qualify a trade.

## Haircut and Margin

Most repo trades include a haircut or margin adjustment to the
collateral value that affords the collateral holder a level of risk
protection. Haircuts and margin adjustments are set on the
`collateralProvision` attribute under
`economicTerms->collateral->collateralProvisions`.

Haircuts in json format appear as:

``` Javascript
"collateralProvisions": {
    "eligibleCollateral": [
        {
            "criteria": [
                {
                    "treatment": {
                        "valuationTreatment": {
                            "haircutPercentage": 2
                            }
                    }
                }
            ]
        }
    ]
}
```

## Repurchase Transaction Example

Example: Fixed Term, Fixed Rate bi-lateral repo

A fixed term, fixed rate bilateral repo transaction is a transaction
between two counterparties to exchange cash for collateral at an agreed
interest rate for an agreed fixed term. On the effective date the seller
delivers collateral to the buyer and receives cash. On the termination
date the buyer returns collateral to the seller and receives the cash
principal plus interest.

As previously described, to build a repo product the following
components are needed:

-   Purchase Date
-   Repurchase Date
-   Buyer
-   Seller
-   Repo Rate
-   Collateral
-   Haircut
-   Purchase Price
-   Repurchase Price
-   Legal Agreement

A fixed term, fixed rate repo example json structure can be found here:

[Fixed-Term,Fixed-Rate Repo Product](https://github.com/finos/common-domain-model/blob/master/docs/code-snippets/fixed-term-fixed-rate-repo-product.json)

The `priceQuantity` object is used to define the collateral value and
repo rate.

The repo rate is defined as a price with a `priceTypeEnum` value of
"INTEREST_RATE".

``` Javascript
"price": [
    {
    "meta": {},
        "value": {
            "unit": {
                "currency": {
                    "value": "GBP"
                }
            },
            "value": 0.004,
            "perUnitOf": {
            "currency": {
                "value": "GBP"
            }
        },
        "priceExpression": {
            "priceType": "INTEREST_RATE"
        }
}}]
```

The `priceQuantity` object is also used to define the collateral price
and value:

``` Javascript
"quantity": [
    {
        "meta": {},
        "value": {
            "unit": {
                "currency": {
                    "value": "GBP"
                }
            },
            "value": 9974250
}}]
```

Collateral amount is defined in terms of the nominal par amount:

``` Javascript
"quantity": [
    {
        "meta": {},
        "value": {
            "unit": {
                "currency": {
                    "value": "GBP"
                }},
            "value": 10000000
        }}]
```

The collateral price can be defined as either Clean or Dirty price:

``` Javascript
"price": [
    {
        "meta": {},
        "value": {
            "unit": {
                "currency": {
                    "value": "GBP"
                }
            },
            "value": 1.0075,
            "perUnitOf": {
            "currency": {
                "value": "GBP"
            }
        },
        "priceExpression": {
            "cleanOrDirty": "DIRTY",
                "priceExpression": "PERCENTAGE_OF_NOTIONAL",
            "priceType": "ASSET_PRICE"
        }}}]
```

Counterparties are defined in the counterparty object and need to define
the role attribute as PARTY_1 or PARTY_2 as it relates to the
counterparty being the buyer or seller.

``` Javascript
{"partyReference": {
    "value": {
        "meta": {
            "externalKey": "UkBank",
            "globalKey": "1ef4886d"
        },
        "name": {
        "value": "UK Bank plc"
        }
    }
},
"role": "PARTY_2"
}]
{"partyReference": {
    "value": {
        "meta": {
           "externalKey": "UkBank",
        "globalKey": "1ef4886d"
        },
        "name": {
        "value": "UK Bank plc"
        }}},
"role": "PARTY_2"
}]
```

## partyRoles

PartyRoles are necessary to define the buyer (cash lender) and seller
(collateral giver). A reference global key is used to link the party
role to the party defined in the party object:

``` Javascript
"partyRoles": [{
"partyReference": {
        "externalReference": "GlobalBank",
            "globalReference": "296093b7"
        },
    "role": "SELLER"
    },
{"partyReference": {
        "externalReference": "UkBank",
        "globalReference": "1ef4886d"
    },
    "role": "BUYER"
}]
```

## Trade Date

Trade Date is a simple date string:

``` Javascript
"tradeDate": "2021-03-18"
```

## Executing Business Event

Executing events in the CDM is performed by calling
`Create_BusinessEvent` using one or more valid Instructions:

To represent the repurchase agreement using the CFI taxonomy the json
would look like:

``` Javascript
"productTaxonomy": [
   {
       "source": "CFI",
       "value": {
           "name": {
               "value": "LRSTXD"
           }
       }
   },
   {
"productQualifier": "Repurchase Agreement",
   "taxonomySource": "CFI"
}]
```

# Lifecycle Events

Lifecycle events are actions that modify or close transactions. These
actions may be taken by a counterparty or automatically generated due to
events such as rate changes, contract changes, extensions, or
terminations. Repo lifecycle events are important to the daily
functioning of the market but also a source of errors and failures
caused by different methodologies implemented by systems for the same
event or mismatches in data, workflow and calculations. The CDM provides
a software implementation based on industry practice and ERCC Best
Practices industry intended to reduce mismatches in workflow and data.

In the CDM a lifecycle event results in a state transition. State
changes are trade specific and are automatically linked in the CDM.

All repo events follow the CDM Event Model design and process.
Initiating a repo event requires the creation of an event primitive
instruction followed by a call to Create_BusinessEvent. Using an
instantiated repo trade that was created with a repo execution will
result in a TradeState object and the state is positionState=Executed.

Repo events currently supported in the CDM include:

-   Execution
-   Roll
-   Re-Rate
-   Early Termination
-   Pair-off
-   Shaping
-   On Demand Interest Payment
