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

