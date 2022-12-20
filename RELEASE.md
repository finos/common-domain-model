# *Core CDM Contribution to FINOS - Repositioning of Collateral & ISDA Legal Documentation components in Preparation for Open Sourcing*

_What is being released?_

As part of the CDM transition to Open Source at FINOS, a code contribution has been constructed, the "Core CDM", that will be transferred to a FINOS managed repository. In this Core CDM, all ISDA Legal Documentation components have been removed, these components will continue to be managed in a repository and under a license at ISDA. Similarly, in the Core CDM, collateral components (non legal) which have been developed by the CDM Collateral Working Group have been moved to a new namespace that is more appropriate for their further development through a new CDM Collateral working group at FINOS.

More details on this release:
- The type _Agreement_ in the namespace cdm.legaldocumentation.contract still contains 4 types and the structure remains in place for existing and other legal agreement components to be added. However the 4 existing types are now hollowed out to be empty types as the content of them should continue to be managed at ISDA; namely:
	  _creditSupportAgreementElections CreditSupportAgreementElections (0..1) <"Elections to specify a Credit Support Annex or Credit Support Deed for Intial or Variation Margin.">
	  collateralTransferAgreementElections CollateralTransferAgreementElections (0..1) <"Elections to specify a Collateral Transfer Agreement.">
	  securityAgreementElections SecurityAgreementElections (0..1) <"Elections to specify a Security agreement.">
	  masterAgreementSchedule MasterAgreementSchedule (0..1) <"Elections to specify a Master Agreement Schedule.">_
-  The type _Collateral_ and associated components have been moved to a new namespace cdm.product.collateral for further development outside the legaldocumentation namespace which was no longer appropriate.
-  Additional content related to ISDA FLoating Rate Indices meta and reference data has also been removed from cdm.observable.asset.fro as it is proprietary ISDA IP and should be managed separately.
-  Associated Synonym file and in line mappings to types that have been removed and other related content have also been removed so that the contribution is functionally complete and stand alone without any errors or omissions.
-  A model containing the content above which has been excluded from the Core CDM contribution to FINOS will be maintained by ISDA and can be integrated with the FINOS Core CDM for use cases where this content is applicable and on CDM development platforms where users have access to the content (e.g., for ISDA members).
