# _Product Model - Day Count Fraction: RBA_Bond_Basis_

_Background_

The codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` and `RBA_BOND_BASIS_ANNUAL` in the CDM enum `DayCountFractionEnum` have been found redundant by definition. The solution to this issue is to merge them into one single code: `RBA_BOND_BASIS`. This also aligns with the FpML representation.

_What is being released?_

   - Deprecated the codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` and `RBA_BOND_BASIS_ANNUAL` and added the code `RBA_BOND_BASIS` in the CDM enum `DayCountFractionEnum`. 
   - Mapping added to populate the new code with the FpML code RBA.

_Review directions_

In Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

PR: https://github.com/finos/common-domain-model/pull/2726

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `rosetta-bundle` dependencies.

Version updates include:
- `rosetta-bundle` 10.13.4: FpML Coding schema updated.

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2708
