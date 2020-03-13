# *Model Optimisation: Externalise the DTCC Synonyms into its own namespace*

_What is being released_

The DTCC synonyms have been moved to a seperate namespace `cdm.synonyms.dtcc` allowing both DTCC_9 and DTCC_11 to inherit from the FPML synonym. This removes a lot of duplication of mapping logic within the CDM.

 - `synonym source DTCC_BASE extends FpML_5_10` : All common synomyms for `DTCC` that inherit from `FpML_5_10`
 - `synonym source DTCC_9_0 extends DTCC_BASE` : Synonyms that only apply to `DTCC_9_0`
 - `synonym source DTCC_11_0 extends DTCC_BASE` : Synonyms that only apply to `DTCC_11_0`

_Review Directions_

In the Textual Browser, search (use control-f) for the text `synonym source DTCC_BASE` and see all of the synonym mapping.
Observe that there are no longer any DTCC synonyms defined within the types (i.e. attributes of `type Contract` do not have any `DTCC` synonyms.
