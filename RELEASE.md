# _Mapping Update - InterestRateForwardDebtPriceMappingProcessor updated to handle 'Percentage' quoteUnits_

_Background_

The price of bond forwards is captured as a monetary value whereas it should be a decimal/percentage. Even if the value in FpML was 'Percentage', the CDM representation value did not accurately represent this, causing misinterpretations.

_What is being released?_

- An update to the **InterestRateForwardDebtPriceMappingProcessor** code to fix the described issue. This change, would correct the interpretation by dividing the current monetary value by 100, when the *quoteUnits* corresponds to the XML value '*Percentage*'.
- The **bond-fwd-generic-ex01.xml** and **bond-fwd-generic-ex02.xml** samples have been updated as the files were using the value 'Percent' but the correct value according to the enum should be 'Percentage'


_Review directions_

In Rosetta, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: [#3242](https://github.com/finos/common-domain-model/pull/3242)
