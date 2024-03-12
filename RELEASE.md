# Eligible Collateral Representation - CreditNotationMismatchResolutionEnum update

_Background_

The existing enum CreditNotationMismatchResolutionEnum contains enumeration values for specifying credit notation in the case where several are listed. The values include "highest", "lowest" and other levels of credit notation as well as sourced from a defined rating agency. There is currently no option for a credit notation where bespoke language represents the label characteristics of the rating.

_What is being released?_

* Added enum value: `Other` to cover for the case where credit notation is based on bespoke language.

_Review directions_

* In the Rosetta platform, select the Textual Browser and inspect the change identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2748
