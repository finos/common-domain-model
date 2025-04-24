# _Legal Agreement Model - Skeleton framework for Trade Association Agreements_

_Background_

While the CDM is leading the path in developing a universal language to discuss legal agreements, a hurdle to overcome is that the legal agreements themselves are written inherently differently. While there is overlap, there is NOT enough overlap to allow for e.g. an ISDA Master Agreement to be represented easily and readily by the CDM.

This is something the collateral working groups are moving towards with the ISDA foundations model and I believe a similar, heavily typed approach would work for the CDM for an ISDA Master Agreement, as well as a GlobalMasterRepoAgreement (GMRA) and GlobalMasterSecuritiesLendingAgreement (GMSLA).

_What is being released?_

As per GitHub Issue [3206](https://github.com/finos/common-domain-model/issues/3206), step one is being implemented in this contribution. This entails adding the MasterAgreementBase to the master namespace type which is then extended for MasterAgreement, GlobalMasterRepoAgreement, and GlobalMasterSecuritiesLendingAgreement, respectively. These are all currently empty and will be populated provisionally and initially in the next development release.

The MasterAgreementSchedule is unaltered for the time being to ensure backward-compatibility with the production release, but will become deprecated in the future.

_Review Directions_

The changes can be reviewed in PR: [3629](https://github.com/finos/common-domain-model/pull/3629)
