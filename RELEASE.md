# _Legal Documentation & Collateral – Simulated IM/VM Collateral Exposure for an Execution_

_Background_

This change allows collateral requirements to be associated to trade details at the pre-execution stage of the trade i.e. when necessary legal agreements may not be signed. The key limitation currently is that the CDM makes the agreement date a mandatory attribute of any legal documentation/agreement object. As a result the CDM is limited to only represent signed (i.e. executed) agreements.

In addition, the Independent Amount attribute of the `Collateral` type has been incorrectly made mandatory. It is not always necessary, so this has been corrected.

_What is being released?_

This release includes the following 
1.	Relax the cardinality of `agreementDate` to optional within `LegalAgreement` type and introduce logical conditions to enforce the presence of this data attribute when the legal document is supposed to have been agreed (signed) (e.g. for the `contractFormationInstruction` or associated functions).
2.	Add `Collateral` details as an optional data input for an `ExecutionInstruction` and augment the corresponding `create_Execution` function to link these details to the execution.
3.	Relax the cardinality of the `independentAmount` attribute in the Collateral type so as to be optional and add the relevant condition to enforce that at least one  component between `independentAmount`, `portfolioIdentifier` and `collateralPortfolio` is present.

_Review Directions_

In the CDM Portal, select the Textual Browser, navigate to types mentioned above and inspect their structure definitions and associated data conditions.

