# Credit Support Document Concept of "catch all" - Added CreditDocumentSupportTypes and relevant validation

_Background_

A CSA should not be limited to needing to either say "there are no Credit Support Documents", "All documents are acceptable" or "Only these documents are acceptable" with the requirement that a full CDM representation of those documents is created. There is a chance that the support document is known but not in CDM format, or is not to hand to create a CDM representation of. A catch all that covers the "These documents are the support documents but they do not have CDM representation here" should be added.

_What is being released?_

New type added as explained in the summary, and validation. This has already been approved once, but was corrupted when attempted to be sorted with merge conflicts.

_Review Directions_

Changes can be reviewed in PR: [#4269](https://github.com/finos/common-domain-model/pull/4269)