# _Product Model - Qualification of Total Return Swaps (TRS) with a Debt Underlier_

_Background_

Following ESMA Guidelines, Total Return Swaps with a debt instrument as their underlier (bond, loan, etc) must report field 2.11 - `Asset Class` as 'CRDT', while TRS on an equity index or a basket of equities should report `Asset Class` as 'EQUI'. Currently in the CDM, a Total Return Swap with a debt underlier is not classified correctly, and thus is being reported incorrectly as well. This release aims at fixing the `Qualify_AssetClass_Credit` function such that Total Return Swaps on a bond or a loan report AssetClass as 'CRDT'.

_What is being released?_

- The function `Qualify_AssetClass_Credit` is increasing its coverage to include Total Return Swaps with an underlier of a `loan` or a `securityType` of `debt`.

_Functions_

- Updated `Qualify_AssetClass_Credit` function to support Total Return Swap products, defined as having an `interestRatePayout` and a `performancePayout`. The function checks the `performancePayout` that `underlier -> loan` is present or that `underlier -> security -> securityType = Debt`.

_Review directions_

In Rosetta, select the Textual View and inspect the change identified above

The changes can be reviewed in PR: [#2855](https://github.com/finos/common-domain-model/pull/2855)

# _Product Model - Qualification of Foreign Exchange NDS_

_Background_

Currently, Foreign Exchange Non-Deliverable Swaps are not supported in the Common Domain Model. This release adds qualification support for this kind of product.

_What is being released?_

- Added the function `Qualify_ForeignExchange_NDS` that qualifies as true if a product has two forward payouts with an FX underlier and the `cashSettlementTerms` populated.
_Review directions_

In the Rosetta platform, select the Textual View and inspect each of the changes identified above.

PR: [#2866](https://github.com/finos/common-domain-model/pull/2866)

# _Addition of new enumeration to AvailableInventory_

_Background_

The ```AvailableInventory``` type supports two major uses cases: 

1. Where a lender wants to distribute the details of the securities that they have available to lend, and 
2. Where a borrower wants to locate specific securities that they want to borrow. 

For the second use case, the ```SecurityLocate``` type was created, which extends ```AvailableInventory``` but has no additional attributes within it.

When using these two types, there is currently no way to differentiate between when the user is intending to implement use case 1 (i.e. use the ```AvailableInventory``` type) or use case 2 (i.e. use the ```SecurityLocate``` type).

As an example, the following valid JSON could represent a lender saying they have 10000 shares available of security GB00000000012, or a borrower requesting 10000 shares of the security.

```javascript
{
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
      "availableQuantity": {
        "value": 10000
      }
    }
  ]
}
``` 

_What is being released?_

A new ```AvailableInventoryTypeEnum``` enumeration has been added with two options:
 - _AvailableToLend_ - for where a party wants to expose the securities that they have available (i.e. use case 1)
 - _RequestToBorrow_ - for where a party wants to request specific securities from another party (i.e. use case 2)

The new enumeration has been added to the ```AvailableInventory``` type and named ```availableInventoryType```. It has been set with _(1..1)_ cardinality, making it mandatory. 
 
The ```availableQuantity``` attribute within ```AvailableInventoryRecord``` has also been renamed to just ```quantity``` to make it more generic and thus applicable to more use cases.

_Review directions_

The changes can be reviewed in PR: [#2810](https://github.com/finos/common-domain-model/pull/2810)

# _Compatibility_
This change introduces a new mandatory enumeration to an existing type. By definition this means that this is a breaking change, as any pre-existing implementation of the ```AvailableInventory``` or ```SecurityLocate``` type will no longer work. 

# _Python Generator v2_

_What is being released?_
This release uses the new version of the Python generator (v2) which includes the following changes:

- Migration to Pydantic 2.x
- More comprehensive support for Rosetta's operators
- Resolves the defect exposed by [PR 2766](https://github.com/finos/common-domain-model/pull/2766)
- Includes an update to the Python Rosetta runtime library used to encapsulate the Pydantic support (now version 2.0.0)