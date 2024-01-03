# *Product Model - Qualification - Bond Forwards*

_Background_

This release fixes issue [#2601](https://github.com/finos/common-domain-model/issues/2601) which breaks bond forward qualification.

_What is being released?_

* Function `cdm.product.qualification.Qualify_AssetClass_InterestRate` has been updated to work for forward payouts
* Bond forward FpML samples, and corresponding FpML synonym mappings, have been added

_Review directions_

In Rosetta, open the Common Domain Model workspace, select the Translate tab and review the following samples:

* fpml-5-10 > products > rates > bond-fwd-generic-ex01.xml
* fpml-5-10 > products > rates > bond-fwd-generic-ex02.xml

Changes can be reviewed in PR [#2602](https://github.com/finos/common-domain-model/pull/2602)
