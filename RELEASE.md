# Product Model - Enum and FpML coding scheme update

_Background_

Rosetta has a feature for its enumerations that enables, if a certain enumeration is directly related to an FpML coding scheme, to label that enumeration with the corresponding coding scheme canonical URI, so every time that coding scheme is updated, the enumeration will be automatically updated (and so will do its mappings). That has been done for `CreditEventTypeEnum` and `InformationProviderEnum`. For `CapacityUnitEnum`, since it is not uniquely related to an FpML coding scheme, the changes have been done  manually.

_What is being released?_

* Enum `CapacityUnitEnum` has had the following modifications: the codes `GBBSH`, `GBBTU`, `GBMBTU`, `GBMMBTU`, `GBTHM`, `HOGB`, `ISOBTU`, `ISOMBTU`, `ISOMMBTU`, `ISOTHM`, `KWD`, `KWM`, `KWMIN`, `KWY`, `MWD`, `MWM`, `MWMIN`, `MWY`, `SGB`, `USBSH`, `USBTU`, `USMBTU`, `USMMBTU` and `USTHM` have been added, while `BSH`, `BTU`, `DTH`, `INGOT`, `KWDC`, `KWHC`, `KWMC`, `KWMINC`, `KWYC`, `MMBTU`, `MWDC`, `MWHC`, `MWMC`, `MWMINC`, `MWYC` and `THERM` have been removed. All the synonym mappings for the new codes have been added and all the synonym mappings for old codes have been removed, too

* Enum `CreditEventTypeEnum` has been annotated with FpML coding scheme `CreditEventTypeScheme`

* Enum `InformationProviderEnum` has been annotated with FpML coding scheme `InformationProviderScheme`

* The following samples from the CDM test pack have been modified so that they do not contain the deprecated FpML code `MMBTU`: 

  * `com-ex13-physical-gas-us-tw-west-texas-pool-floating-price-4-days`
  * `com-ex22-physical-gas-option-multiple-expiration`
  * `com-ex34-gas-put-option-european-floating-strike`
  * `com-ex36-gas-call-option-european-spread-negative-premium-floating-strike`
  * `com-ex01-gas-swap-daily-delivery-prices-last`
  * `com-ex02-gas-swap-prices-first-day`
  * `com-ex03-gas-swap-prices-last-three-days`
  * `com-ex05-gas-v-electricity-spark-spread`
  * `com-ex06-gas-call-option`
  * `com-ex07-gas-put-option`
  * `com-ex28-gas-swap-daily-delivery-prices-option-last`
  * `com-ex46-simple-financial-put-option`
  * `com-ex1-gas-swap-daily-delivery-prices-last`
  * `com-ex02-energy-nat-gas-cash`
  * `com-ex5-gas-v-electricity-spark-spread`

* The following sample from the CDM test pack has been modified so that it does not contain the deprecated FpML code `THERM`: `com-ex14-physical-gas-europe-ttf-fixed-price`

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
