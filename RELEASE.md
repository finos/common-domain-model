# _CDM Model - Addition of Exchange attributes_

_Background_

In accordance with the CDM roadmap, the model is being expanded and updated to ensure robust support for evolving regulatory requirements. Specifically, CSA jurisdiction requires a new reporting rule from CDE version 3, called _Underlying Asset Trading Platform Identifier_. This reporting rule requires extracting the platform on which the underlying asset is traded, thus this information is given by `exchange` and `relatedExchange` attributes. Since CSA is modelled in DRR 6, which operates using CDM 5, we need to add these attributes in CDM 5. Furthermore, the same field is going to be needed in the future modelling of CFTC version 3.3.

_What is being released?_

This release includes the addition of the attributes `exchange` and `relatedExchange` in the following types:
- `Commodity`
- `Security`

To achieve this, an intermediate type called `Listing` is created to prevent contamination in other types and to maintain consistency with CDM 6. The rationale behind this change is that a field in the CSA jurisdiction requires extracting the platform on which the underlying asset is traded which is given by `exchange` and `relatedExchange` attributes. The discussion on this matter can be found in issue [#3338](https://github.com/finos/common-domain-model/issues/3338).

_Review directions_

In Rosetta, select the Textual View and inspect each of the changes identified above.
In Rosetta, go to the following path _cdm.base.staticdata.asset.common_ and review the expectations for the fields listed above.
The changes can be reviewed in PR: [#3518](https://github.com/finos/common-domain-model/pull/3518)
