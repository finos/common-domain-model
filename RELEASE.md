# _Product Taxonomy Model - Adding "CSA" value in TaxonomySourceEnum_

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM.

All ISDA Foundations components have since been migrated to CDM as part of issue [#3348](https://github.com/finos/common-domain-model/issues/3348), with all ISDA legal IP scrubbed from components and hidden behind a docReference tag. Following this migration, the ISDA Foundations project can be removed and all references in the CDM documentation should be updated.

_What is being released?_

This release removes all reference to ISDA Foundations from the CDM documentation, specifically in the Legal Agreements and Working Groups sections. The Legal Agreements section has also been updated to refer to the new legal agreement structure for master agreements.

_Review Directions_

The changes can be reviewed in PR: [#3773](https://github.com/finos/common-domain-model/pull/3773)

