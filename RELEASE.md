
# *Event Model - PartyRoleEnum including PTRRServiceProvider role*

_Background_

In order to report under EMIR, a party needs to be identified as a portfolio compression or a portfolio rebalancing service provider. These roles can be unified in a more generic role: PTRR Service Provider. The current CompressionServiceProvider code will be replaced by PTRR Service Provider.

_What is being released?_

- CDM enum `PartyRoleEnum` has been modified in the following way: code `CompressionServiceProvider` has been eliminated and replaced by a more generic code `PTRRServiceProvider`.
- Synonym mappings have been added to reflect this change.

_Review directions_

- In the CDM Portal, select the Textual Browser and inspect the change identified above.

The changes can be reviewed in PR [#2653](https://github.com/finos/common-domain-model/pull/2653)

# _Product Model - FpML Mappings - Bond Forwards_

_What is being released?_

This release fixes FpML mapping issues related to bond forward samples.

_Review directions_

In Rosetta, open the Common Domain Model workspace, select the Translate tab and review the following samples:

* fpml-5-10 > products > rates > bond-fwd-generic-ex01.xml
* fpml-5-10 > products > rates > bond-fwd-generic-ex02.xml

Changes can be reviewed in PR [#2657](https://github.com/finos/common-domain-model/pull/2657)
