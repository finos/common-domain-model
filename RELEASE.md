# _A Collateral Specification can identify which of the parties is the Payer and Receiver_

_Background_

Previously, `CounterpartyRoleEnum` was added to `EligibleCollateralCriteria` to enable the Party1/Party2 label to be applied to eligibility specifics.  This supports an eligible collateral list, such as a ‘schedule’, to be created with specified parties without the existence of a legal agreement such as a CSA.  Members have raised the additional requirement to model the roles that the parties are playing on the collateral schedule, for example Payer and Receiver of collateral.

_What is being released?_

The existing design to identify the roles of parties to trades is being reused through the use of the `PartyRole` data type and the `PartyRoleEnum` enumerator.  Specifically, the `PartyRole` attribute has been added to the `EligibleCollateralSpecification` data type.

_Backward-Incompatible Changes_

None

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the change identified above.

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2585
