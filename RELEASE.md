# _Legal Agreement – Simulated IM/VM Collateral Exposure for an Execution_

_Background_

This change introduces the modelling of possible IM/VM collateral requirements associated to a set of trade details at the pre-execution stage  of the transaction i.e. when the legal agreements may not be signed. The key limitation currently is that the CDM imposes the agreement date attribute of any legal agreement to be mandatory to denote that it has been signed. As a result the CDM is limited to only represent signed (i.e. executed) agreements.

In addition, the independent amount attribute of the `Collateral` type has been incorrectly annotated as mandatory. It is not always necessary.

_What is being released?_

This release includes the following 
1.	Relax the cardinality of `agreementDate` to optional within `LegalAgreement` type and introduce logical conditions to enforce the presence of this data attribute when the legal document is supposed to have been agreed (signed) (e.g. for the `contractFormationInstruction` or associated functions) 
2.	Add collateral details as an optional data input for an execution Instruction and augment the corresponding `create_Execution` function to link these details to the execution.
3.	Relax the `independentAmount` attribute of the Collateral type to optional and add the relevant optional choice to enforce that at least one attribute component between `independentAmount`, `portfolioIdentifier` and `collateralPortfolio` is present.

_Review Directions_

In the CDM Portal, select the Textual Browser, navigate to type mentioned and inspect their structure definitions and associated data conditions.

