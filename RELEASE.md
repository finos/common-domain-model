# *CDM Product Model* - Underlier in Corporate Action

_Background_

In an earlier Asset Refactoring release, an unintending defect was introduced on the `CorporateAction` data type.
This release corrects that.

_What is being released?_

The data type of the `underlier` attribute within `CorporateAction` has been undated to be `Underlier` rather than
`Instrument`.  `Instrument` is too restrictive as a broader range of assets can be the subject of corporate actions
and this is best represented by the `Underlier` data type.

_Review Directions_

In Rosetta, select the Textual Browser and inspect the changes to the data type identified above.

Changes can be reviewed in PR [#3184](https://github.com/finos/common-domain-model/pull/3189)
