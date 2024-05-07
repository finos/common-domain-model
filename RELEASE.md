# _Product Model - FpML mappings - Link ID_

_Background_

The version 2-17 of the FpML coding schemes was recently published. This new version includes some changes that are already present in the corresponding enumerations of the CDM model, but the synonym mappings from FpML to CDM do not cover the latest changes. This release introduces a mapper to support the mappings of `linkId`, to cover the changes in v2-17 of FpML coding schemes.

_What is being released?_

- Added mapping coverage, using a mapper, to support the new schemes of the FpML's `linkId`.

_Review directions_

In Rosetta, select the Textual view and inspect each of the changes identified above.

In Rosetta, open the Translate tab and review the following test pack samples:

- fpml-5-12 -> processes:
   - custom-basket-linkId-ptrr-comp.xml
   - custom-basket-linkId-rtn.xml

The changes can be reviewed in PR: [#2852](https://github.com/finos/common-domain-model/pull/2852)
