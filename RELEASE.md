# _Product Model - FpML Mappings - Contractual Party_

_Background_

This release fixes the mapping of `LegalAgreement>contractualParty` as described in issue [#2788](https://github.com/finos/common-domain-model/issues/2788).

_What is being released?_

- Updated `DocumentationHelper` that is used by mappers to extracts the value from `TradableProduct->counterparty` and map it to `LegalAgreement->contractualParty`

_Review directions_

In Rosetta, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: [#2806](https://github.com/finos/common-domain-model/pull/2806)