# *CDM Model: Currency Enum*

_What is being released_

Adding two currency code enumerated sets to the file base-staticdata-asset-commons-enum:
 - enum `ISOCurrencyCodeEnum`
 - enum `CurrencyCodeEnum` extends `ISOCurrencyCodeEnum`

The `CurrencyCodeEnum` will be used in place of string as the date type for all currency fields. The work of updating the data type will be completed in one or more subsequent releases.
The new `CurrencyCodeEnum` is materialized version of the `FpML` external scheme for currency codes and includes an applicable synonym.

_Review Directions_

In the Textual Browser, review `ISOCurrencyCodeEnum` and `CurrencyCodeEnum`. 
