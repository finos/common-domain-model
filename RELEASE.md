# _Rename product qualification for Securities Lending and Repo_

_Background_

The current product qualification rules do not distinguish between securities lending and repurchase agreement product types.  A future enhancement will refactor how `product` works and also introduce additional capabilities to the qualification functions to use additional attributes to support finer grained qualification (see Issue #2365).

In the meantime, the current qualification rule for repurchase agreements - which is invoked also for securities lending products - will be renamed to be "SecuritiesFinancing" to more loosely differentiate both sets of products.

_What is being released?_

- An updated product qualification rule:  `Qualify_RepurchaseAgreement` is renamed to `Qualify_SecuritiesFinancing`.

_Data types_

No changes.

_Enumerations_

No changes.

_Sample Files_

The following JSON sample files have been updated to reflect the current modeling of securities lending products which is to have the collateral information inside the `Collateral` data type rather than inside `AssetPayout`.

- create-security-lending-invoice-func-input.json
- full-return-settlement-workflow-func-input.json
- new-settlement-workflow-func-input.json
- new-settlement-workflow-func-input.json
- allocation/allocation-sec-lending-func-input.json
- reallocation/reallocation-pre-settled-func-input.json

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2448
