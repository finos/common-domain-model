# **Modelling Lender Availability and Borrower Locates**
_Background_
In securities lending a lender will broadcast their availability to lend specific securities to potential borrowers. This process allows borrowers to easily identify and select lenders who have the desired securities available for lending and can help facilitate trades more quickly and efficiently. 

Borrowers will also send out requests to lenders looking to locate specific securities. This allows borrowers to confirm that they can acquire the security before proceeding with a short sale of that security.

As part of the ongoing work into standardising pre-trade processes by the ISLA Trading Working Group, enhancements are being introduced to the model to support the representation of both lender availability and borrower locates.

_Model Changes_

The structures of lender availability and borrower locates are extremely similar. It has also been suggested that the structure of collateral inventory could be represented the same way. To this end new base types have been added that can be extended to support multiple use cases.

The `InventoryBase` type can be used to hold a list of securities. This base type has been extended into the `Inventory` type. The term “Inventory” in the financial markets refers to the securities that a party has on hand and thus works well as a base class name.  

The details of the securities held in the inventory are described in the `InventoryRecordBase` type. This base type has been extended into the `InventoryRecord` type. 

The similarity between the lender availability and borrower locate use cases led onto the creation of two new types, `AvailableInventory` and `AvailableInventoryRecord`.

The `AvailableInventoryRecord` type extends `InventoryRecord` which allows it to inherit the security details. Additional criteria that are applicable to both availability and locate processing are held here too, including `collateralProvisions`, `expirationDateTime`, `partyRole` (to allow cross referencing back to parties in the main `AvailableInventory` type described below) and `priceQuantity` which is where the quantity of securities associated to this available inventory record can be specified.

The `AvailableInventory` type is where the `AvailableInventoryRecord` is used, and describes a set of inventory that is held by a party. The `AvailableInventoryRecord` array is where the individual rows of available inventory are held. In addition to the available securities this type also contains the `party` and `partyRole` types. The `party` here should define all the parties that are held across all the elements in the `AvailableInventoryRecord` array. The `partyRole` can also be used at this level to define the role each party has in relation to the inventory. 

For the lender availability use case, the `SecurityAvailability` type is a new type that will hold a list of all the securities that a lender has available. Each available security is then held in an element in the `availableInventoryRecord` array, which can hold details of the security, the quantity of shares available and some optional additional criteria that is often used by the borrower to determine if they want to pursue a new trade with the lender for a specific security.

For the borrower locates use case, the `SecurityLocate` type is a new type that will hold a list of all the securities that a borrower is looking to locate from a lender. Each requested security is then held in an element in the `availableInventoryRecord` array, which can hold details of the security, the quantity of shares required and some optional additional criteria that is often used by the lender to determine if they want to lend the shares to the borrower.

The `SecurityLocate` type also contains a condition to ensure that the `availableInventoryRecord` item exists, as a locate must always request availability for at least one security.

_Data Types_
- Added `InventoryBase` type:
  - Includes new `inventoryRecord` type
- Added `Inventory` type:
  - Extends `InventoryBase`
  - There is nothing else in this type at the moment, it has been created for consistency and further extension in the future
- Added `InventoryRecordBase` type:
  - Includes `identifier` and `security`
- Added `InventoryRecord` type:
  - Extends `InventoryRecordBase`
  - There is nothing else in this type at the moment, it has been created for consistency and further extension
- Added `AvailableInventory` type:
  - Includes `party`, `partyRole` and new type `availableInventoryRecord`
- Added `AvailableInventoryRecord` type:
  - Extends `InventoryRecord` and thus inherits `identifier` and `security` from it
  - Additionally includes `expirationDateTime`, `collateral`, `partyRole` and `priceQuantity`
- Added `SecurityAvailability` type:
  - Extends `AvailableInventory` and thus inherits `party`, `partyRole` and `availableInventoryRecord` from it
  - Additionally includes `messageInformation`
- Added `SecurityLocate` type:
  - Extends `AvailableInventory` and thus inherits `party`, `partyRole` and `availableInventoryRecord` from it
  - Additionally includes `messageInformation`
  - There is a condition `RequestOneSecurityMinimum` which ensures `availableInventoryRecord` is populated

_Review directions_

- Review the changes in the model
- Inspect the Pull Request: https://github.com/finos/common-domain-model/pull/2296

