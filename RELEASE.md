# LegalAgreement - High level refactoring of CSA and CTA Elections

_Background_

This contribution enhances the Legal Agreement part of CDM. Members of the Legal Agreement WG have approved the changes below as it streamlines this part of the model and reduces validation errors while improving data integrity and enforcement of conditions and cardinalities.

_What is being released?_

1. Refactoring `CreditSupportAgreementElections` into `CreditSupportAgreementElectionsBase`, `CreditSupportAgreementInitialMarginElections`, `CreditSupportAgreementVariationMarginElections` and `CreditSupportAgreementLegacyElections`. IM, VM and Legacy extend base.
2. Refactoring `CreditSupportObligations` into `CreditSupportObligationsBase`, `CreditSupportObligationsInitialMargin`, `CreditSupportObligationsVariationMargin`, `CreditSupportObligationsCollateralTransferAgreement` and `CreditSupportObligationsLegacy`. IM, VM, CTA and Legacy extend base.
3. Refactoring `CalculationandTiming` into `CalculationandTimingBase`, `CalculationAndTimingInitialMargin`, `CalculationAndTimingVariationMargin`, `CalculationAndTimingCollateralTransferAgreement` and `CalculationandTimingLegacy`. IM, VM, CTA and Legacy extend base.
5. Some miscellaneous updates to cardinalities and descriptions.

_Review Directions_

Changes can be reviewed in PR: [#4215] (https://github.com/finos/common-domain-model/pull/4215)
