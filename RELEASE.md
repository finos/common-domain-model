# *Add CSA components from ISDA Foundations to CDM - Added enum, func, and types to legaldocumentation.csa namespace*

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM.

Any “hard-coded” legal provision are removed from the model description

All ISDA legal IP has been scrubbed from components and hidden behind a docReference tag, with the path to legal definitions & descriptions clearly identified and listed.

_What is being released?_

A CDM user has access to all the components that were previously in ISDA Foundations

Added CSA components & namespaces to legaldocumentation

- isda.legaldocumentation.csa.enum
- isda.legaldocumentation.csa.func
- isda.legaldocumentation.csa.type

- These will have minimal impact as only the csa.type namespace currently exists in CDM, and consists of 3 empty types.

_Review Directions_

The change can be reviewed in PR: [3652](https://github.com/finos/common-domain-model/pull/3652)