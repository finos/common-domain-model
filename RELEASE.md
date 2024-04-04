# _Product Model - FpML Mappings - Contractual Party_

_Background_

Currently, `ContractualParty` is not being mapped in the `LegalAgreement` element. The `contractualParty` is a **(2..2) mandatory** element and should be represented accordingly to avoid validation failures. For more information see issue [#2788](https://github.com/finos/common-domain-model/issues/2788) .

_What is being released?_

- Updated `DocumentationHelper` which allows mappers to extract the value from `TradableProduct->counterparty` and map it to `LegalAgreement->contractualParty`.

_Review Directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

The changes can be reviewed in PR: [#2832](https://github.com/finos/common-domain-model/pull/2832)

