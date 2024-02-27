# _Product Model - Day Count Fraction: RBA_Bond_Basis_

_Background_

The codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` and `RBA_BOND_BASIS_ANNUAL` in the CDM enum `DayCountFractionEnum` have been found redundant by definition. The solution to this issue is to merge them into one single code: `RBA_BOND_BASIS`. This also aligns with the FpML representation.

_What is being released?_

   - Replaced the codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` and `RBA_BOND_BASIS_ANNUAL` with the code `RBA_BOND_BASIS` in the CDM enum `DayCountFractionEnum`.
   - Mapping added to populate the new code with the FpML code `RBA`.

_Backward incompatible changes_

This release contains backward-incomplatible changes. Anywhere the codes `RBA_BOND_BASIS_QUARTER`, `RBA_BOND_BASIS_SEMI_ANNUAL` or `RBA_BOND_BASIS_ANNUAL` are found, this code must be replaced by the new one `RBA_BOND_BASIS`.

_Review directions_

In Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

PR: https://github.com/finos/common-domain-model/pull/2727
