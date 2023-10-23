# _Eligible Collateral Model - Applicable Party Attributes_

_Background_

This release addresses Issue [#2451](https://github.com/finos/common-domain-model/issues/2451) - Enabling party to be specificed in eligible collateral criteria.

The CDM allows to define which party of a legal agreement a set of information may apply to (both parties or one party). This applies to certain outcomes or posting obligations such as eligible collateral. The parties are identified alongside other key details within the data type `LegalAgreementBase`. Further in the data type `AgreementTerms`, each counterparty is assigned the pseudonym of Party1 or Party2 and then referred to through its pseudonym when specifying party elections.

Presently, the option to apply eligible collateral criteria to both parties or to one party in an eligible collateral list (such as a ‘schedule’) is only possible if associated with a legal agreement such as a CSA.​ The CDM requires the ability to apply this alternatively under the root data type `EligibleCollateralSpecification` so it can be applied independently of a CSA, to eligible collateral only.

However, as other data types are currently linked to `EligibleCollateralSpecification`, there is a potential for duplicate party information.  Therefore, the other data types are being updated to link to `EligibleCollateralCriteria` rather than `EligibleCollateralSpecification` so the latter can be used, with defined parties, without duplication.

_What is being released?_

- As a root data type, `EligbileCollateralSpecification` will now include the designated parties.
- Existing data types `CollateralProvisions` and `PostingObligationsElection` (in the CSA model) will now connect to `EligibleCollateralCriteria` not `EligibleCollateralSpecification`.
- Updates to add the party designated on Collateral (ie `appliesTo` as an attribute on `CollateralCriteriaBase`) so that the specification of collateral can be linked to one or both of the parties.

_Data types_

`EligibleCollateralSpecification`:
- addition of attribute `party` optionally to define up to two parties to the specification
- addition of attribute `counterParty` to link the party to a enum "Party1/Party2" so that it can be referenced elsewhere in the model

`CollateralCriteriaBase`:
- addition of new attribute `appliesTo CounterpartyRoleEnum  (0..2)` which enables the specification of which of the two counterparties the criteria applies to.
- corrections to spellings in the comments in `CollateralCriteriaBase`.

`CollateralProvisions`:
- the data type of the `eligibleCollateral` attribute has been changed from `EligibleCollateralSpecification` to `EligibleCollateralCriteria`.

_Enumerations_

None.

_Functions_

`func SecurityFinanceCashSettlementAmount` and `func ResolveSecurityFinanceBillingAmount`
- as the hierarchy of data types has changed, these two functions have been updated to remove the reference to `criteria` (8 occurences).

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2459
