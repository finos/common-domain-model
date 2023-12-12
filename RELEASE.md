# _A Collateral Specification can identify which of the parties is the Payer and Receiver_

_Background_

Subject matter experts for Collateral trade management have identified that a self-contained representation of eligible collateral criteria requires the association of each involved party with the various roles mentioned in the criteria.  For example, to identify which parties are the Payer and Receiver of collateral.

_What is being released?_

A new `PartyRole` attribute has been added to the `EligibleCollateralSpecification` data type.

_Backward-Incompatible Changes_

None

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the change identified above.

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2585
