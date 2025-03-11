# _CDM Model - Addition of Exchange attributes_

_Background_

In accordance with the CDM roadmap, the model is being expanded and updated to ensure robust support for evolving regulatory requirements.

_What is being released?_

This release includes the addition of the attributes _exchange_ and _relatedExchange_ for both _commodity_ and _security_ types to maintain consistency with CDM 6. To achieve this, an intermediate type called _Listing_ was created to prevent contamination in other types. The rationale behind this change is that a field in the CSA jurisdiction requires extracting the platform on which the underlying asset is traded. The discussion on this matter can be found in issue https://github.com/finos/common-domain-model/issues/3338.
    
_Review directions_

In Rosetta, select the Textual View and inspect each of the changes identified above.
In Rosetta, go to the following path cdm.base.staticdata.asset.common and review the expectations for the fields listed above.

The changes can be reviewed in PR: [3501](https://github.com/finos/common-domain-model/pull/3501)
