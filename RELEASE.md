# *CDM Version 4.0*

CDM 4.0, a production release, is the culmination of Common Domain Model (CDM) development releases from the first half of 2023 since CDM was made an open-source project at FINOS. This release sees the contribution of the Repo model and separation of legal content in CDM, which was completed in development in preparation for open sourcing now released in production. Notable other additions to the CDM project are a new Python Distribution of CDM and associated code generator, enhancements to the product qualifications and an eligible collateral specification model. There are also several technical changes since version 3.0 related to bug fixes, dependencies and synonym mappings.

Below are some of the high-level contributions that CDM 4.0 includes, more detail and additional contribution release notes can be found in the releases section of the CDM documentation:
 - CDM Distribution - Python Code Generation
 - CDM Distribution - Java artifacts published to Maven Central
 - Infrastructure - Dependency Updates   
 - Product Model - Currency Enums (automated generation of ISO 4217 enum lists)
 - Product Model – Qualification (composable derivatives product qualification based on ISDA taxonomy v2)
 - Product Model - Inflation Swaps - calculationMethod and calculationStyle  (added to Common Domain Model)
 - Renaming of a root data type from EligibleCollateralSchedule to EligibleCollateralSpecification to indicate more general utility of model
 - Legal Documentation & Collateral – Eligible Collateral Schedule Builder Function
 - Legal Documentation & Collateral – Document-agnostic Cash Collateral Interest model harmonised with existing Floating Rate model
 - Workflow Model - Representation of approvals for a Workflowstep  (added to Common Domain Model)
