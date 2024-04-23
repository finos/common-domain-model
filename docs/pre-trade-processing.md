---
title: Pre-trade Processing
---

# Pre-trade Processing

## Introduction

Pre-trade processing is an important part of the trade lifecycle. It can involve
a large number of steps, including initiation tasks like order routing, risk 
management assessment and compliance checking, as well as the actual trade
negotiation, affirmation and confirmation.

## Pre-trade in Securities Lending

In securities lending a lender will broadcast their availability to lend 
specific securities to potential borrowers. This process allows borrowers to 
easily identify and select lenders who have the desired securities available 
for lending and can help facilitate trades more quickly and efficiently.

A borrower can also approach a lender directly in order to ascertain whether
they have a security available to borrow. This request could be sent to one or
more lenders, depending on the borrower's requirements.

Lenders regularly only share the bare minimum amount of detail when presenting 
their availability, often including only the security identifier. When 
responding to a request for a security from a borrower then the lender may 
provide a lot more detail, including interest rates and acceptable types of 
collateral. 

### Modelling the broadcast of available securities

A lender will distribute anywhere from one to several thousand availability 
records to the market or individual borrowers. The model represents this using 
two types: ```AvailableInventory``` and ```AvailableInventoryRecord```.

![AvailableInventory](https://github.com/finos/common-domain-model/assets/112546065/ed75afb0-5592-4833-a38c-1775fab74bcb)

```AvailableInventory``` is designed to hold a list of ```AvailableInventoryRecord``` 
items, with each item within ```AvailableInventoryRecord``` representing a specific 
piece of availability i.e. a security being held by a lender or custodian. Thus, 
for a lender to model a broadcast of say 10 different pieces of availability, 
the structure required would consist of one ```AvailableInventory``` with a list of 10
 ```AvailableInventoryRecord``` items within it.

#### AvailableInventory

As described above, ```AvailableInventory``` is used to hold the entire set of 
availability that the lender wants to distribute. This is where details that 
pertain to the overall availability and the records held within it should be 
specified. 

``` Haskell
type AvailableInventory: 
    availableInventoryType AvailableInventoryTypeEnum (1..1) 
    messageInformation MessageInformation (0..1) 
    party Party (0..*) 
    partyRole PartyRole (0..*) 
    availableInventoryRecord AvailableInventoryRecord (0..*) 
```

The attributes available are:

- The ```availableInventoryType``` is a mandatory enumeration which is used to 
describe the purpose of this ```AvailableInventory``` instance. For the lender 
availability use case this should be set to "_AvailableToLend_"

- The optional ```messageInformation``` type allows details related to a message 
containing the availability to be described if required. 

- The ```party``` attribute here describes all parties involved in this set of 
availability. For example, this could include the sender of the availability, 
the intended recipient, the beneficial owner(s), the lender (which may differ 
from the sender as the lender may have the same piece of availability going 
through multiple agents), an agent or a venue.

-  Each of the parties included in the ```party``` type can be assigned a ```partyRole```. 
The _ValidPartyRole_ condition within ```AvailableInventory``` restricts the types of 
role that can be assigned in this context. Valid roles are: "_AgentLender_", 
"_BeneficialOwner_", "_Borrower_", "_Custodian_" or "_Lender_"

-  The ```availableInventoryRecord``` record is where the securities that are to be 
shown as available should be listed.

#### AvailableInventoryRecord

The ```AvailableInventoryRecord``` type is an array that is used to hold the list of 
availability records that the lender wants to broadcast. Each availability 
record will need to include details of the security and any associated criteria 
e.g. the quantity of shares available, the rate at which the security is 
available to borrow at.

This type inherits additional mandatory types from the generic ```InventoryRecord``` type 
which it extends.

``` Haskell
type InventoryRecord:
    identifer AssignedIdentifier (1..1) 
    security Security (1..1) 
```

The ```identifier``` and ```security``` should be used as follows:

- The ```identifier``` type is mandatory and is used to assign a specific reference 
to this availability record. This allows the parties to uniquely identify any 
row of availability.

- The ```security``` type allows the key security information to be provided, 
including the ```securityType``` and identifier for the instrument. 

The ```AvailableInventoryRecord``` itself allows the most common datapoints shared 
when distributing availability to be specified. 

``` Haskell
type AvailableInventoryRecord extends InventoryRecord: 
    expirationDateTime zonedDateTime (0..1) 
    collateral CollateralProvisions (0..*) 
    partyRole PartyRole (0..*) 
    quantity Quantity (0..1) 
    interestRate Price (0..1) 
```

These attributes are all optional and should be used as follows:

- Where a time limit/restriction needs to be set against a piece of availability 
then the ```expirationDateTime``` attribute can be used to express it.

- The ```collateral``` type allows the lender to specify the type of collateral 
required for a specific piece of availability.

- In this context the ```partyRole``` is primarily used to reference parties that 
have already been defined in the ```party``` attribute of the top level 
```AvailableInventory``` type. It can also be used here to define the role of a 
party at the individual security level if necessary (which could be required if 
a security is held by multiple agents). There is a _ValidPartyRole_ condition 
within ```AvailableInventoryRecord``` that restricts the types of role that can 
be assigned in this context to: "_AgentLender_", "_BeneficialOwner_", "_Custodian_"
or "_Lender_".

- The ```quantity``` attribute can be used to specify the number of shares that are 
available. Note that this attribute is optional, the lender may want to only 
broadcast the fact that they have a security available to drum up interest.

- The ```interestRate``` attribute allows the lender to specify a rate that is required 
for a piece of availability. The _InterestRate_ condition within 
```AvailableInventoryRecord``` ensures that the ```interestRate -> priceType``` is set to 
"_InterestRate_" if ```interestRate``` is included here. 

#### Examples

In this section some examples of valid JSON describing availability are 
provided. Several different examples are provided to assist the user when 
attempting to model their own availability. 

##### Single security

In this very simple example a party is broadcasting their availability for a
single ISIN. Very limited details are provided implying that this is general
availability targeted at the entire market.

``` Javascript
{
	"availableInventoryType": "AvailableToLend",
	"availableInventoryRecord": [
		{
			"identifier": {
				"identifier": "00001"
			},
			"security": {
				"securityType": "Equity",
				"productIdentifier": {
					"identifier": "GB00000000012",
					"source": "ISIN"
				}
			},
			"quantity": {
				"value": 10000
			}
		}
	]
}
```

##### Multiple securities

Building upon the last example, in this example a party is broadcasting their
availability for two ISINs. 

This time the party has not included the number of shares that they have 
available for the first ISIN, "GB00000000012", forcing borrowers to request 
more details before a deal can be negotiated.  

For the second ISIN, "GB00000000013", they have included some specific lending 
criteria to help the borrowers decide if they want to proceed with a loan.

``` Javascript
{
	"availableInventoryType": "AvailableToLend",
	"availableInventoryRecord": [
		{
			"identifier": {
				"identifier": "00001"
			},
			"security": {
				"securityType": "Equity",
				"productIdentifier": {
					"identifier": "GB00000000012",
					"source": "ISIN"
				}
			}
		},
		{
			"identifier": {
				"identifier": "00002"
			},
			"security": {
				"securityType": "Equity",
				"productIdentifier": {
					"identifier": "GB00000000013",
					"source": "ISIN"
				}
			},
			"quantity": {
				"value": 1800000
			},
			"interestRate": {
				"priceType": "InterestRate",
				"value": 0.025,
				"unit": {
					"currency": "GBP"
				}
			}
		}
	]
}
```

##### Securities held by an agent lender

In this final example, an agent lender is broadcasting the availability that 
they have for a single security. They specify that they require other 
securities as collateral in a loan using this piece of availability (as they 
have set the ```collateral -> collateralType``` enumeration to "_NonCash_"). 

The details of the agent lender are also included so recipients of the 
availability will know that they are acting as an agent in this instance.

``` Javascript
{
	"availableInventoryType": "AvailableToLend",
	"party": [
		{
			"partyId": {
				"identifier": {
					"value": "AGENT1"
				}
			},
			"name": {
				"value": "Agency Services Limited"
			},
			"meta": {
				"globalKey": "x123rt",
				"externalKey": "agent1"
			}
		}
	],
	"partyRole": [
		{
			"partyReference": {
				"globalReference": "x123rt",
				"externalReference": "agent1"
			},
			"role": "AgentLender"
		}
				
	],
	"availableInventoryRecord": [
		{
			"identifier": {
				"identifier": "00001"
			},
			"security": {
				"securityType": "Equity",
				"productIdentifier": {
					"identifier": "GB00000000013",
					"source": "ISIN"
				}
			},
			"quantity": {
				"value": 20000
			},
			"collateral": [
				{
					"collateralType": "NonCash"
				}
			}
		}
	]
}
```

### Modelling the borrower request

When a borrower needs a security to cover a short position, or for some other 
trading strategy, they can approach specific lenders or the general market to 
locate the shares that they need. The model represents this using two types: 
```SecurityLocate``` and ```AvailableInventoryRecord```.

![SecurityLocate](https://github.com/finos/common-domain-model/assets/112546065/00c78aed-6db7-41cf-95d6-a1e85b4ac5d3)

```SecurityLocate``` is designed to hold a list of ```AvailableInventoryRecord``` 
items, with each item within ```AvailableInventoryRecord``` representing a specific 
security that the borrower is looking to locate. Thus, for a borrower to locate
10 different securities, the structure required would consist of one 
```SecurityLocate``` with a list of 10 ```AvailableInventoryRecord``` items 
within it.

#### SecurityLocate

As described above, ```SecurityLocate``` is used to hold the entire set of 
securities that the borrower wants to locate. This is where details that 
pertain to the overall request should be specified. 

``` Haskell
type SecurityLocate extends AvailableInventory:
```

_Note: ```SecurityLocate``` is an extension of ```AvailableInventory``` and 
thus inherits all the data points from within it._

``` Haskell
type AvailableInventory: 
    availableInventoryType AvailableInventoryTypeEnum (1..1) 
    messageInformation MessageInformation (0..1) 
    party Party (0..*) 
    partyRole PartyRole (0..*) 
    availableInventoryRecord AvailableInventoryRecord (0..*)
```

The attributes available in ```SecurityLocate``` are thus:

- The ```availableInventoryType``` is a mandatory enumeration which is used to 
describe the purpose of this ```SecurityLocate``` instance. For the borrower 
request use case this should be set to "_RequestToBorrow_"

- The optional ```messageInformation``` type allows details related to a message 
containing the request to be described if required. 

- The ```party``` attribute here describes all parties involved in this request. 
For example, this could include the sender of the request, the intended recipient, 
the beneficial owner(s), the lender, an agent or a venue.

-  Each of the parties included in the ```party``` type can be assigned a ```partyRole```. 
The _ValidPartyRole_ condition within ```SecurityLocate``` restricts the types of 
role that can be assigned in this context. Valid roles are: "_AgentLender_", 
"_BeneficialOwner_", "_Borrower_", "_Custodian_" or "_Lender_"

-  The ```availableInventoryRecord``` record is where the securities that are 
being requested should be listed. The _RequestOneSecurityMinimum_ condition 
enforces the presence of at least one ```availableInventoryRecord``` here, as a 
request must include at least one security.

#### AvailableInventoryRecord

The ```AvailableInventoryRecord``` type is an array that is used to hold the list of 
securities that the borrower wants to locate. Each record will need to include 
details of the security and optionally include any associated criteria e.g. the 
quantity of shares the borrower requires.

This type inherits additional mandatory types from the generic ```InventoryRecord``` type 
which it extends.

``` Haskell
type InventoryRecord:
    identifer AssignedIdentifier (1..1) 
    security Security (1..1) 
```

The ```identifier``` and ```security``` should be used as follows:

- The ```identifier``` type is mandatory and is used to assign a specific reference 
to this request. This allows the parties to uniquely identify any request.

- The ```security``` type allows the key security information to be provided, 
including the ```securityType``` and identifier for the instrument. 

The ```AvailableInventoryRecord``` itself allows the most common datapoints shared 
when requesting securities to be specified. Note that in the context of a 
borrower request, a lot of these datapoints will not be required.

``` Haskell
type AvailableInventoryRecord extends InventoryRecord: 
    expirationDateTime zonedDateTime (0..1) 
    collateral CollateralProvisions (0..*) 
    partyRole PartyRole (0..*) 
    quantity Quantity (0..1) 
    interestRate Price (0..1) 
```

These attributes are all optional and should be used as follows:

- Where a time limit/restriction needs to be set against a request for a security 
then the ```expirationDateTime``` attribute can be used to express it.

- The ```collateral``` type allows the borrower to specify the type of collateral 
that they are looking to use for a loan against this security.

- In this context the ```partyRole``` is primarily used to reference parties that 
have already been defined in the ```party``` attribute of the top level 
```SecurityLocate``` type. It can also be used here to define the role of a 
party at the individual security level if necessary. There is a _ValidPartyRole_ 
condition within ```AvailableInventoryRecord``` that restricts the types of role 
that can be assigned in this context to: "_AgentLender_", "_BeneficialOwner_", 
"_Custodian_" or "_Lender_".

- The ```quantity``` attribute can be used to specify the number of shares that  
the borrower requires. 

- The ```interestRate``` attribute allows the borrower to specify the rate that 
they are looking to pay for a loan against this security. The _InterestRate_ 
condition within ```AvailableInventoryRecord``` ensures that the 
```interestRate -> priceType``` is set to "_InterestRate_" if ```interestRate``` 
is included here. 

#### Examples

In this section some examples of valid JSON describing borrower requests are 
provided. Several different examples are provided to assist the user when 
attempting to model their own requests. 

##### Single security

In this very simple example a borrower is requesting if any lenders have a
single ISIN available for them to borrow. No parties are specified implying that 
this is a general request targeted at the entire market.

Note that no quantity is specified either, the borrower is first seeing who has 
any availability for this security.

``` Javascript
{
	"availableInventoryType": "RequestToBorrow",
	"availableInventoryRecord": [
		{
			"identifier": {
				"identifier": "00001"
			},
			"security": {
				"securityType": "Equity",
				"productIdentifier": {
					"identifier": "GB00000000012",
					"source": "ISIN"
				}
			}
		}
	]
}
```

##### Single security from a particular lender

In this example, rather than the borrower sending a request to the entire market, 
they have specified the agent lender that they want to borrow this ISIN from. 
They have also included their own party details, setting themselves as the
borrower.

This time the party has also included the number of shares that they would like 
to borrow.

``` Javascript
{
	"availableInventoryType": "RequestToBorrow",
	"party": [
		{
			"partyId": {
				"identifier": {
					"value": "BORROWER1"
				}
			},
			"name": {
				"value": "ACME Bank"
			},
			"meta": {
				"globalKey": "pe4h12",
				"externalKey": "borrower1"
			}
		},
		{
			"partyId": {
				"identifier": {
					"value": "AGENT1"
				}
			},
			"name": {
				"value": "Agency Services Limited"
			},
			"meta": {
				"globalKey": "x123rt",
				"externalKey": "agent1"
			}
		}
	],
	"partyRole": [
		{
			"partyReference": {
				"globalReference": "pe4h12",
				"externalReference": "borrower1"
			},
			"role": "Borrower"
		},
		{
			"partyReference": {
				"globalReference": "x123rt",
				"externalReference": "agent1"
			},
			"role": "AgentLender"
		}		
	],
	"availableInventoryRecord": [
		{
			"identifier": {
				"identifier": "00001"
			},
			"security": {
				"securityType": "Equity",
				"productIdentifier": {
					"identifier": "GB00000000012",
					"source": "ISIN"
				}
			},
			"quantity": {
				"value": 1800000
			},
		}
	]
}
```

##### Multiple securities from multiple lenders

In this final example the borrower is now sending a request for two 
securities to two agent lenders. Once again they have included their party 
details too.

This time the party has included the number of shares that they would like 
to borrow for one of the securities, but not for the other.

``` Javascript
{
	"availableInventoryType": "RequestToBorrow",
	"party": [
		{
			"partyId": {
				"identifier": {
					"value": "BORROWER1"
				}
			},
			"name": {
				"value": "ACME Bank"
			},
			"meta": {
				"globalKey": "pe4h12",
				"externalKey": "borrower1"
			}
		},
		{
			"partyId": {
				"identifier": {
					"value": "AGENT1"
				}
			},
			"name": {
				"value": "Agency Services Limited"
			},
			"meta": {
				"globalKey": "x123rt",
				"externalKey": "agent1"
			}
		},
		{
			"partyId": {
				"identifier": {
					"value": "AGENT2"
				}
			},
			"name": {
				"value": "GTR Custody"
			},
			"meta": {
				"globalKey": "9wq8rg",
				"externalKey": "agent2"
			}
		}
	],
	"partyRole": [
		{
			"partyReference": {
				"globalReference": "pe4h12",
				"externalReference": "borrower1"
			},
			"role": "Borrower"
		},
		{
			"partyReference": {
				"globalReference": "x123rt",
				"externalReference": "agent1"
			},
			"role": "AgentLender"
		},		
		{
			"partyReference": {
				"globalReference": "9wq8rg",
				"externalReference": "agent2"
			},
			"role": "AgentLender"
		}
	],
	"availableInventoryRecord": [
		{
			"identifier": {
				"identifier": "00001"
			},
			"security": {
				"securityType": "Equity",
				"productIdentifier": {
					"identifier": "GB00000000012",
					"source": "ISIN"
				}
			},
			"quantity": {
				"value": 1800000
			},
		},
		{
			"identifier": {
				"identifier": "00002"
			},
			"security": {
				"securityType": "Equity",
				"productIdentifier": {
					"identifier": "GB00000000010",
					"source": "ISIN"
				}
			}
		}
	]
}
```


