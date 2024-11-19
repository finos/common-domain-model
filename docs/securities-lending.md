---
title: Securities Lending
---

# Securities Lending

## Introduction

The CDM is a data model that provides a standard format for financial 
products and transactions in the capital markets industry. It is 
intended to improve data quality, increase efficiency, and reduce costs 
by creating a common language that enables automated trade processing 
and reduces the need for costly customisations.

The International Securities Lending Association ([ISLA](https://www.islaemea.org)) have been working 
with securities financing subject matter experts to model components in 
the CDM for the Securities Lending market. 

## Scope

A securities lending transaction involves the loan of securities by one party 
(the "*lender*") to another (the "*borrower*"), often facilitated by a 
brokerage firm or agent, with the borrower providing collateral and paying a 
fee.

Securities lending products, trades and transactions can all be represented 
using the types, structures and functions in the CDM. The legal agreements 
that govern the trades can also be described and attached to the trades 
themselves.

The model supports open or term cash and non-cash collateralised loans, using 
either fixed or floating rates, on a principal or agency basis. Core lifecycle 
events including trade execution, settlement, returns, allocation and 
reallocation are supported, with a basic billing function also provided. 

## Core elements in Securities Lending

There are several types, attributes and functions that should be used to 
describe securities lending products and lifecycle events. The core elements 
required are described in this section, with examples of their usage.

### Cash Collateral

In a cash loan, the lender lends the borrower the requested securities, 
and the borrower provides cash as collateral against the securities that they 
are borrowing. 

The security that is being lent is modelled in the main trade `economicTerms` 
using a single `AssetPayout`, which includes an `underlier` which identifies
the `Security` explicitly within the data type `Instrument`.

Loans against cash collateral are held under `economicTerms->collateral`
with a `collateralPosition` containing one or more `product` data types, each of
which is `TransferableProduct` which defines the asset being used as collateral
(ie `Cash`) with the addition of `economicTerms` which define the rates and
other terms payable using an `InterestRatePayout`.

The `collateralType` under `collateralProvisions` should be set to "*Cash*" to 
define this as a cash collateralised loan. These types can be found under the 
`economicTerms->collateral` type.

An example showing the location of the 
security being lent and the payouts for the cash collateral and is provided below:

``` Javascript
"economicTerms": {
  "payout": {
    "AssetPayout": [ {
      "underlier": {
        "Instrument": {
          "Security": {
            "identifier": {
             ...
	  } } } } } ]
  },
  ...
  "collateral": {
    "collateralProvisions": {
      "collateralType": "Cash"
    },
    "collateralPortfolio": [ {
      "collateralPosition": [ {
         "Product": {
           "TransferableProduct": {
              "Cash": {
                 ...
               } ,
             "economicTerms": {
               "payout": {
	         "InterestRatePayout": {
                    "rateSpecification": { ... } ,
                    "paymentDates: { ... } ,
                    ...
             } } }
   } } } ] } ] } ,
  ...
}
```

### Non-cash Collateral

In a non-cash loan, the lender lends the borrower the requested securities, 
and the borrower provides collateral in the form of other securities or 
products. 

The security that is being lent is modelled the same way as the cash case with
an `underlier` in the main `AssetPayout`.

The non-cash collateral is also still modelled with a one or more `TransferableProduct` 
in the `collateralPosition`.  Rather than the product containing `cash`, the actual
asset being used as collateral is specified. The details of the security should be entered
in the attributes held within the `Security->identifier` type, being the `identifier`,
`identifierType` and the `instrumentType`.  `economicTerms` can also be added
if these are required to fully reflect the specifics of the collateral.

The `collateralType` under `collateralProvisions` should be set to "*NonCash*" 
to define this as a non-cash collateralised loan. These types can be found 
under the `economicTerms->collateral` type.

An example showing the location of the payouts for the non-cash collateral and 
the security being lent is provided below:

``` Javascript
"economicTerms": {
  "payout": {
    "AssetPayout": [ {
      "underlier": {
        "Instrument": {
          "Security": {
            "identifier": {
             ...
	  } } } } } ]
  },
  ...
  "collateral": {
    "collateralProvisions": {
      "collateralType": "Cash"
    },
    "collateralPortfolio": [ {
      "collateralPosition": [ {
         "Product": {
           "TransferableProduct": {
             "Instrument": {
               "Security": {
                  "identifier": {
                  ...
             } } } },
             "economicTerms": {
               "payout": {
	         ...
             } } }
   } } } ] } ,
  ...
}
```

### Cash Pool
In a cash pool loan, the lender lends the borrower the requested securities, 
and takes cash as collateral from a cash pool held by the lender on behalf of 
the borrower. 

As seen in the cash collateral example above, the `collateralPosition`
can contain multiple products, so the cash pool can be modelled as a list
of cash products, with currencies, amounts and other terms defined.

The `collateralType` under `collateralProvisions` should be set to "*CashPool*" 
to define this as a cash pool collateralised loan. These types can be found 
under the `economicTerms->collateral` type.

Please see the preceding example for a securities lending trade collateralised 
using cash for an example of where the payouts can be found. 

### Defining Collateral

When non-cash collateral is used as collateral on a trade then it needs to 
be defined in the `AssetPayout`. This can be done using the 
`security` type under `securityInformation`. 

### Parties, Party Roles and Counterparties

The parties on a trade are defined in `Party` objects. These can be the 
lender and the borrower (i.e. the counterparties) or custodians or agency 
lenders.

Details of each party should be defined in its own `Party` object. The role
of each party can be defined in the `PartyRole` type. Using metadata keys and 
references each `Party` can then be associated to its `role`.

The counterparties on a trade can be defined using the `Counterparty` type. 
This abstracts the `role` of each counterparty away from the actual parties 
on the trade, allowing them to be set as either "*Party1*" or "*Party2*".  The 
counterparties must reference a `Party` object which can again be done using 
metadata to avoid duplication.

### Payer and Receiver

The `InterestRatePayout` and `AssetPayout` types must also define the payer 
and receiver under the `payerReceiver` type.

The `payer` and `receiver` must reference the counterparties on the `trade` 
using their roles on that trade i.e. either "*Party1*" or "*Party2*". The 
`counterparty` under the `tradableProduct` can be used to set which of the
`Party` objects is which party. 

In securities lending the borrower is considered the payer as they are 
providing the collateral (either cash or non-cash), and the lender is the 
receiver of the collateral.

### Interest Rate, Price, Quantity and Value

The key factors that affect the earnings on a securities lending trade are the 
interest rate, price, loan quantity and loan value. These are all held in the 
`priceQuantity` type, which is represented as a list, allowing multiple items
to be specified.

*Note: where a trade has multiple lots associated to it, each `tradeLot` can* 
*have a different `priceQuantity` associated to it, allowing each lot to have*
*its own factors defined.*

A securities lending trade will need to include a minimum of two items in the 
`priceQuantity` list - one to describe the asset price and the quantity of 
shares of that asset; and another to describe the interest rate and the value 
to which the rate is to be applied.

#### Price and Quantity

The `priceQuantity` will need to include a `price` that holds the asset price 
and a `quantity` that holds the number of shares on loan. It is also possible 
to define the security that the price and quantity are associated to in the 
`observable` type.

The `price` will need to have its `priceType` set to "*AssetPrice*". The 
simplest way to express the price is using the `value` attribute, an example 
of which would be as follows:

``` Javascript
"priceQuantity": [ {
  ...
  "price" : [ {
    "value" : {
      "value" : 25,
      "unit" : {
        "currency" : {
         "value" : "GBP"
        }
      },
      "perUnitOf" : {
       "financialUnit" : "Share"
      },
      "priceType" : "AssetPrice"
    }
  } ]
  ...
} ]
```

The `quantity` will need to define the number of shares in the `value` 
attribute:

``` Javascript
"priceQuantity": [ {
  ...
  "quantity" : [ {
    "value" : {
      "value" : 0,
      "unit" : {
        "financialUnit" : "Share"
      }
    }
  } ]
  ...
} ]
```		  

If the security on loan is to be defined here then an `observable` should be 
included in the `priceQuantity` too:

``` Javascript
"priceQuantity": [ {
  ...
  "observable" : {
    "productIdentifier" : [ {
      "value" : {
        "identifier" : {
          "value" : "GB00BDR05C01"
        },
        "source" : "ISIN"
      }
    } ]
  }
  ...
} ]
```

*Note: the security on loan can also be defined under the `product -> security`*
*details*

#### Interest Rate and Value

The `priceQuantity` will need to include a `price` that represents the interest 
rate and a `quantity` that holds the value that the interest rate is to be
applied to. 

The `price` will need to have its `priceType` set to "*InterestRate*". The 
simplest way to express the rate is using the `value` attribute, an example 
of which would be as follows:

``` Javascript
"priceQuantity": [ {
  ...
  "price" : [ {
    "value" : {
      "value" : 0.01,
      "unit" : {
        "currency" : {
         "value" : "GBP"
        }
      },
      "perUnitOf" : {
        "currency" : {
          "value" : "GBP"
        }
      },	  
      "priceType" : "InterestRate"
    }
  } ]
  ...
} ]
```

The `quantity` will need to define the value that the rate is applied to in the 
`value` attribute:

``` Javascript
"priceQuantity": [ {
  ...
  "quantity" : [ {
    "value" : {
      "value" : 10000000,
	  "unit" : {
        "currency" : {
         "value" : "GBP"
        }
      }
    }
  } ]
  ...
} ]
```		  

### Dates

There are several key dates through the lifecycle of a securities lending 
trade. The main ones that need to be defined are described in this section.

#### Effective Date

The effective date in a securities lending trade refers to the date when the 
agreement or transaction between the lender and the borrower becomes binding 
and takes effect. It signifies the point at which the terms and conditions 
of the lending arrangement are legally enforceable. 

The `effectiveDate` can be found under the product's `economicTerms` type.

#### Trade Date

The trade date is the specific day when the order to lend securities is 
executed or placed in the market. The trade date marks the initiation of the 
transaction, while the effective date represents the point at which the 
agreement becomes legally binding. 

Once the trade has been executed then the `tradeDate` under the `trade` type 
should be set to the date the trade was executed.

#### Settlement Dates

The settlement date is when the securities legally change hands. In a 
securities lending trade, there are potentially two dates related to 
settlement: the security settlement date, and for a trade against cash, the 
cash settlement date.

The security settlement date is when the securities on the trade are legally 
transferred from the lender to the borrower. It is the day when the borrower 
becomes the holder of record of the security.

For cash trades the cash settlement date is the date when the cash payment for 
the borrowed securities is made by the borrower to the lender. It represents 
the completion of the financial aspect of the securities lending transaction.

In both instances the settlement dates are defined under the payouts associated 
to the trade.

For a trade against non-cash there would be two `assetPayouts`, one for the 
non-cash collateral and another for the securities being lent. At a minimum 
the `settlementDate` under `settlementTerms` in the `assetPayout` for the 
security being lent should be populated. However, it is recommended that the 
`settlementDate` under both `assetPayout -> settlementTerms` types are set.

For a trade against cash there would be an `assetPayout` for the securities 
being lent and a `settlementPayout` for the cash being used as collateral. 
The security settlement date should be placed in the `settlementDate` under 
`settlementTerms` in the `assetPayout`, and the cash settlement date should 
be placed in the `settlementDate` under `settlementTerms` in the 
`settlementPayout`.

*Note: For DVP trades the cash and security settlement dates will be the same.*
*They should still be set under the `assetPayout->settlementTerms->settlementDate`*
*and `settlementPayout->settlementTerms->settlementDate` types as described*
*above.*

#### Termination Date

Where a trade has a termination date this can be set under the 
`terminationDate` of the product's `economicTerms` type. Additional details 
related to the termination of a trade can be placed within the 
`terminationProvision` type also under `economicTerms`.

### Legal Agreement

The agreement governing a trade should be referenced in the `contractDetails` 
type under `trade`. For securities lending trades the ISLA Global Master 
Securities Lending Agreement can be referenced by setting the `publisher` to 
"*ISLA*" and the `agreementName->masterAgreementType` to "*GMSLA*". The 
preprint version can also be set in the `vintage` attribute. 

These attributes are available under the `legalAgreementIdentification` type,
an example of which is provided below:

``` Javascript
"contractDetails": {
  "documentation": [ {
    "legalAgreementIdentification": {
      "agreementName": {
        "masterAgreementType": {
          "value": "GMSLA"
        }
      },
      "publisher": "ISLA",
      "vintage": 2010
    }
  } ]
}
```

*Note: further details of the master agreement can be modelled in the*
*`masterAgreementSchedule` which is also provided underneath the `documentation`*
*type.*

### Haircut and Margin

In a securities lending trade, a haircut is a reduction applied to the value 
of the collateral used for a loan and is generally expressed as a percentage.
A margin is the initial market value of the collateral expressed as a 
percentage of the market price. 
 
These concepts can be represented using attributes under `collateralProvisions`
under the product's `economicTerms`. 

*Note: these attributes are expressed as decimal numbers. Thus a 50% haircut*
*would be represented as "0.5", and a 110% margin would be represented as*
*"1.1".*

An example of a trade that has a 10% haircut and 105% margin would look as 
follows:

``` Javascript
"economicTerms": {
  "collateral": {
    "collateralProvisions": {
      "eligibleCollateral": [ {
        "treatment": [ {
          "valuationTreatment": {
            "haircutPercentage": 0.1,
			"marginPercentage": 1.05
          }
        } ]
      } ]
    }
  }
}
```

