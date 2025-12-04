# Legal Agreements - High-level refactoring of CSA and CTA Elections

_Background_

This contribution enhances the Legal Agreements in CDM. Members of the Legal Agreement Working Group have approved the changes below as it streamlines this part of the model and reduces validation errors while improving data integrity and enforcement of conditions and cardinality.

_What is being released?_

1. Refactoring `CreditSupportAgreementElections` into `CreditSupportAgreementElectionsBase`, `CreditSupportAgreementInitialMarginElections`, `CreditSupportAgreementVariationMarginElections` and `CreditSupportAgreementLegacyElections`. These new types extend the base type.
2. Refactoring `CreditSupportObligations` into `CreditSupportObligationsBase`, `CreditSupportObligationsInitialMargin`, `CreditSupportObligationsVariationMargin`, `CreditSupportObligationsCollateralTransferAgreement` and `CreditSupportObligationsLegacy`. These new types extend the base type.
3. Refactoring `CalculationandTiming` into `CalculationandTimingBase`, `CalculationAndTimingInitialMargin`, `CalculationAndTimingVariationMargin`, `CalculationAndTimingCollateralTransferAgreement` and `CalculationandTimingLegacy`. These new types extend the base type.
5. Updates to cardinality and descriptions.

_Review Directions_

Changes can be reviewed in PR: [#4215](https://github.com/finos/common-domain-model/pull/4215)
