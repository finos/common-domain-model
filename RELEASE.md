# Product Taxonomy Model - Adding "CFTC" value in TaxonomySourceEnum

_Background_

A gap has been identified in the model when capturing taxonomy values for commodity underlyer assets as defined by CFTC regulation. Introducing `CFTC` as a taxonomy source is necessary to properly map these values within the model and to support population of the **"Commodity Underlyer ID"** fields in DRR.

_What is being released?_

The contribution is the addition of a new `CFTC` value to the `TaxonomySourceEnum` in order to represent the Commodity Futures Trading Commission as a taxonomy source, enabling support for **Commodity Underlyer ID** rules under CFTC jurisdiction in DRR.

_Review Directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/4112