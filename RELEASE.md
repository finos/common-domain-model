# *DSL Syntax - Filter and Map Keywords for Extracting or Modifying the Items of a List*

_What is being released?_

_List Item Filtering_

The `filter` keyword has been introduced to filter the items of a list based on a `boolean` expression.  For each list item, a `boolean` expression is evaluated to determine whether to include or exclude the item.  The resulting list is assigned to output parameter.  Note that filtering a list does not change the item type, e.g. when filtering a list of `PartyRole`, the output list type must also be of `PartyRole`.

_Syntax_

```
 set outputList:
     inputList
         filter [ Boolean expression – true to include, false to exclude ]
```

_Example_

```
 func FilterPartyRole:
     inputs:
         partyRoles PartyRole (0..*)
         partyRoleEnum PartyRoleEnum (1..1)
     output:
         filteredPartyRoles PartyRole (0..*)

     set filteredPartyRoles:
         partyRoles
             filter [ item -> role = partyRoleEnum ]
```

_List Item Processing_

The `map` keyword has been added to modify the items of a list based on an expression.  For each list item, an expression is invoked to modify the item.  The resulting list is assigned to output parameter.  The `map` keyword was chosen as it is the most widely used terms for this use-case, used in languages such as Java, Python, Scala, Perl, Clojure, Erlang, F#, Haskell, Javascript, PHP, and Ruby.

_Syntax_

```
 set outputList:
     inputList
         map [ Expression to modify item ]

```

_Example_

```
 func ExtractPriceType: 
     inputs:
         prices Price (0..*)
     output:
         priceTypeEnums PriceTypeEnum (0..*)

     set priceTypeEnums:
         prices 
             map [ item -> priceType ]
```

_Review Directions_

In CDM Portal Textual Browser search the following functions.

`Party`/`Counterparty` functions:

- `ExtractCounterpartyByRole`
- `ExtractAncillaryPartyByRole`
- `ReplaceParty`
- `FilterPartyRole`

`PriceQuantity` functions:

- `FilterPrice`
- `FilterQuantityByCurrency`
- `FilterQuantityByCurrencyExists`
- `FilterQuantityByFinancialUnit`
- `MergeTradeLot`

`Transfer` functions:

- `TransfersForDate`
- `FilterCashTransfers`
- `FilterSecurityTransfers`

Miscellaneous functions:

- `Create_BillingRecords`
- `Create_RelatedAgreementsWithPartyReference`
- `ExtractFixedLeg`
