# *CDM Model - Equity Products*

_Background_

This release contains modifications required to accomodate Equity and Exotic Products under individual Asset Classes

_What is being released?_

This release creates following modifications:
- a new qualification function `Qualify_Equity_OtherOption` to add qualification for Exotic Options. The function uses `nonStandardisedTerms` attribute to differentiate between `Exotic Options` and Regular Options. For the existing option qualifications functions, `nonStandardisedTerms` is negatively tested to prevent multiple qualification


- FpML conditions `FpML_ird_9` and `FpML_ird_29` are relaxed when `compoundingMethod` is not populated 

- relaxation of cardinality rule for `expirationTime`. The attribute is now optional.
- attribute `expirationTimeType` is now mandatory.
- addition of validation condition to establish the correlation between `expirationTime` and `expirationTimeType`. The change is made backward incompatible and all affected samples are modified too. (See Example: [Euro Option](https://github.com/finos/common-domain-model/blob/37256934de2c54e5eafc7b6a9ab76ca1bc56de5b/rosetta-source/src/main/resources/result-json-files/fpml-5-10/products/fx/fx-ex09-euro-opt.json)))

_Review Directions_

Please inspect the changes identified above in the Rosetta file:

[Product-Asset-Type](https://github.com/finos/common-domain-model/blob/fffc7bf11574076e53bb62951a1d8b59bb53aebc/rosetta-source/src/main/rosetta/product-asset-type.rosetta)

[Product-Qualification-Func](https://github.com/finos/common-domain-model/blob/fffc7bf11574076e53bb62951a1d8b59bb53aebc/rosetta-source/src/main/rosetta/product-qualification-func.rosetta)

[Product-Template-Type](https://github.com/finos/common-domain-model/blob/fffc7bf11574076e53bb62951a1d8b59bb53aebc/rosetta-source/src/main/rosetta/product-template-type.rosetta)

The changes can also be reviewed in PR: [#3278](https://github.com/finos/common-domain-model/pull/3278).
