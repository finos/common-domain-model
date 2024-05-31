# *Product Model - FpML Mapping Update*

_Background_

This release extends the FpML mapping coverage for FRAs.

_What is being released?_

- FpML mapper updated to map FRA payment frequency from the FpML index tenor on to the fixed leg instead of the floating leg

_Review Directions_

In Rosetta, select the Translate tab and review the following samples:

- fpml-5-10 > products > rates > ird-ex08-fra.xml
- fpml-5-10 > products > rates > ird-ex08-fra-no-discounting.xml

Changes can be reviewed in PR [#2947](https://github.com/finos/common-domain-model/pull/2947)
