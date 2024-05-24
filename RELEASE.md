# *Product Model - FpML Mapping - FRA Payment Frequency and Commodity Delivery Date Parameters*

_Background_

This release extends the FpML mapping coverage for FRAs and Commodity products.

_What is being released?_

- FpML synonyms and mapper updated to map FRA floating leg payment dates from the FpML index tenor
- FpML synonyms added to map Commodity delivery date parameter `deliveryNearby`

_Review Directions_

In Rosetta, select the Translate tab and review the following samples:

- fpml-5-10 > products > rates > ird-ex08-fra.xml
- fpml-5-10 > products > rates > ird-ex08-fra-no-discounting.xml
- fpml-5-10 > products > commodity > com-ex41-oil-asian-barrier-option-strip.xml

Changes can be reviewed in PR [#2935](https://github.com/finos/common-domain-model/pull/2935)
