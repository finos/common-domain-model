# _Instrument - Adding DepositoryReceipt to InstrumentTypeEnum and creating DepositoryReceipt enum_

_Background_

Depository receipts are not currently in CDM but are used in collateral schedules. Collateral in particular would benefit from the addition of DR to the criteria, and this issue proposes its addition to the security type under as a type of equity.

Depositary Receipts can be of types American, Global, and Indian

_What is being released?_

- Created a new type `EquityType` with 2 attributes: `EquityTypeEnum` and `DepositoryReceiptTypeEnum`
- Create a new enum: `DepositoryReceiptTypeEnum`
- Add `DepositoryReceipt` to `EquityTypeEnum` (Enum already contains Ordinary & NonConvertiblePreference)
- Add a condition that `DepositoryReceiptEnum` is absent if DepositoryReceipt is not selected as the equity type

_Review directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/3882

# _Product Model - AveragingFeature to Support Asian Options_

_Background_

Asian options were previously not covered in the model. The FpML mappings were pointing to the Asian type, which, however, was an orphan type and not referenced anywhere in the model.

To address this, it has been proposed to create a new `AveragingFeature` type that encapsulates `AveragingCalculation` and incorporates the attributes mapped from FpML. This new type is now referenced within `OptionFeature`.

_What is being released?_

The following type has been added to the model:

- `AveragingFeature`

The following type has been updated:

- `OptionFeature`
- `OptionPayout` 

The following FpML Mapping has been updated:

- `AveragingFeature` (formerly called `Asian`)

_Review Directions_

Changes can be reviewed in PR: [#3827](https://github.com/finos/common-domain-model/pull/3827)

# _Infrastructure - Release Process updates following Maven Central Migration_

_Background_

The Maven Central infrastructure is undergoing a major migration, from `oss.sonatype.org` to `central.sonatype.com`. This impacts the release process for all projects hosting artifacts using Maven Central.

Further information is provided by Sonatype:
https://central.sonatype.org/faq/what-is-different-between-central-portal-and-legacy-ossrh/#self-service-migration

_What is being released?_

The release process has been updated to build and deploy to `central.sonatype.com` instead of `oss.sonatype.org`.

_Review Directions_

No changes made to model.

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/3830
