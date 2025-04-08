# _Legal Agreements - Added skeleton framework for GMSLA, GMRA and ISDA Master Agreements into Agreement Namespace_

_Background_

While the CDM is leading the path in making a universal language to discuss legal agreements, a huge hurdle to get over is that the legal agreements themselves are written inherently differently. While there is overlap, there is NOT enough overlap to allow for e.g. an ISDA master agreement to be represented easily and readily into the system.

This is something the collateral working groups are moving towards with the ISDA foundations model and I believe a similar, heavily typed approach would work for the CDM for an ISDA master agreement, as well as a GMRA and GMSLA

_What is being released?_

As per issue [3206](https://github.com/finos/common-domain-model/issues/3206) on Github, we are implementing step one in this contribution. This entails adding the MasterAgreementBase to the master namespace type which is then extended for GMRA, GMSLA and IsdaMasterAgreement respectively. These are all currently empty and will be populated provisionally and initially into the next development release.

MasterAgreementSchedule is remaining as is for the time being to ensure backwards compatability with production, but will be deprecated and changed in a future release PR.

_Review Directions_

The changes can be reviewed in PR: [3624](https://github.com/finos/common-domain-model/pull/3624)
