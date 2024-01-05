# _Product model - Repo Functions_

_Background_

The CDM supports repo products and lifecycle events but integration with existing applications and systems
can be complicated due to the complex nature of the CDM trade and product structure. Accessing basic repo
data element like repo rate or haircut involves constructing data paths deep into the product. To address this
difficulty and make it easier for firms to adopt the CDM this enhancement includes a suite of custom repo functions
built using CDM modelling methods so that the functions are standard and consistent across programming languages and
platforms.

_What is being released?_

New files with the name product-api-repo- are being added that includes functions for repo dates, parties,
collateral, and tranactions. The file list includes:
- product-api-repo-common-func.rosetta
- product-api-repo-collateral-func.rosetta
- product-api-repo-datetime-func.rosetta
- product-api-repo-parties-func.rosetta
- product-api-repo-trade-func.rosetta
- product-api-repo-product-func.rosetta

Some of the new functions include:

func GetPurchaseDate
func GetRePurchaseDate
func GetTermDays
func GetBuyer
func GetSeller
func GetPartyIdentifier
func GetTradeUTI
func GetRepoRate
func GetPurchasePrice
func GetRePurchasePrice
...and function to set values and other related repo functions.

_Backward-Incompatible

- These new functions are fully backward compatible.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
