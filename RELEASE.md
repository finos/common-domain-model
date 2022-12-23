# *Core CDM Contribution to FINOS - Repositioning of Collateral & Removal of ISDA Legal Documentation components in Preparation for Open Sourcing*

_Background_

As part of the CDM transition to the Finance Open Source Foundation (FINOS), a new "Core CDM" has been constructed and will be transferred to FINOS. 

All ISDA Legal Documentation components have been removed from this Core CDM. These will be managed as extensions of the model and positioned in a separate repository under a license at ISDA. 

Collateral components (non legal) which have been developed by the CDM Collateral Working Group have been moved to a new namespace that is more appropriate for their further development through a new CDM Collateral working group at FINOS.

_What is being released?_

- The `Agreement` type in the namespace cdm.legaldocumentation.contract still contains 4 attributes and their respective type definition remains in place for existing and other legal agreement components to be added. However, the structural details of these definition are now hollowed out as empty types. Their content will continue to be managed at ISDA separately
	-  `creditSupportAgreementElections CreditSupportAgreementElections (0..1) <"Elections to specify a Credit Support Annex or Credit Support Deed for Initial or Variation Margin.">`
	-  `collateralTransferAgreementElections CollateralTransferAgreementElections (0..1) <"Elections to specify a Collateral Transfer Agreement.">`
	-  `securityAgreementElections SecurityAgreementElections (0..1) <"Elections to specify a Security agreement.">`
	-  `masterAgreementSchedule MasterAgreementSchedule (0..1) <"Elections to specify a Master Agreement Schedule.">`

-  The  `Collateral` type and associated components have been moved to a new namespace cdm.product.collateral for further development outside the legaldocumentation namespace which was no longer appropriate.

-  Content related to ISDA FLoating Rate Indices meta and reference data has also been removed from cdm.observable.asset.fro as it is proprietary ISDA IP.

-  Associated Synonym file and in line mappings to the types mentioned afore and other related content have also been removed so that the contribution is functionally complete and stands alone without any errors or omissions.

-  A model containing the content mentioned will be published in an upcoming release and will be maintained by ISDA. Implementer will be able to integrated this model extension with the FINOS Core CDM for applicable use cases.

_Review Directions_

In the CDM Portal, select the textual representation of the model and inspect the different model components mentioned above.
