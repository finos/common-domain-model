# _Infrastructure - Dependency Update_

_Background_

The Rosetta platform has a feature for its enumerations that enables, if a certain enumeration is directly related to an FpML or ISO coding scheme, to label that enumeration with the corresponding coding scheme canonical URI, so every time that coding scheme is updated, the enumeration will be automatically updated.

_What is being released?_

This release also updates the FpML / ISO code scheme syncing configuration to exact matching allowing backwards incompatible changes, as per the [development version guidelines](https://cdm.finos.org/docs/contributing/#version-availability).
This release updates `ISOCurrencyCodeEnum` to keep it in sync with the latest ISO 4217 coding scheme.

* The following enum value have been
  * removed:
    * ANG
    * CUC  
  * added:
    * `XCG <"Caribbean Guilder">`
    
_Review Directions_

The changes can be reviewed in PR: [#3610] (https://github.com/finos/common-domain-model/pull/3610)
