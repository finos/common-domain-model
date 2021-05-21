# *Product Model - Mapping to FpML: Add new Record-Keeping samples*

_What is being released?_

New FpML samples (non-public execution report) are added to the set of Record-Keeping samples:

- Rates: FRA, OIS Swap, Inflation Swap, CapFloor
- FX: Simple Exotic Barrier and Digital
- Commodity: Oil Swap

Although synonyms have been adjusted to map a large number of fields in those documents, some more work will be required to allow a complete coverage. Example of fields missing:

- Rate option for Inflation Swap
- Cap rate schedule, when a spread schedule is also present
- Special features for FX Simple Exotic

These samples are included as part of a broader addition of samples for DRR, and having those documents at least partially mapped in CDM allows to proceed with modelling of the reporting logic (for instance, having the premium fields captured for options).

The FpML synonyms and paths have also been cleaned-up to provide a more consistent treatment across product types / asset classes, making the mappings more readable and more easily extensible (for instance in the treatment of option premium).

_Review directions_

In the CDM Portal, select the Ingestion panel and review the following samples:

- `record-keeping` > `record ex03 capfloor with premium`
- `record-keeping` > `record ex04 fra`
- `record-keeping` > `record ex05 inflation swap`
- `record-keeping` > `record ex06 ois swap`
- `record-keeping` > `record ex07 capfloor with spread`
- `record-keeping` > `record ex21 fx simpleexotic barrier`
- `record-keeping` > `record ex22 fx simpleexotic digital`
- `record-keeping` > `record ex31 commodity oil swap`

# *Documentation - How to Contribute and Doc Style Guide sections*

_What is being released?_

The documentation has been updated to accomodate an increasing number of contributors to the CDM from the industry community.

The documentation now contains an "How to Contribute" section that details the practical responsibilities of both the Contributor and the Reviewer in the process, in accordance with the CDM governance guidelines, and the subsequent deployment process.

As any contribution needs to include its own documentation update and release notes, a "Documentation Style Guide" section has been provided to ensure on-going consistency in the CDM documentation which may be sourced from many different writers.

_Review directions_

Review the new sections in the CDM documentation:

- [How to Contribute](https://docs.rosetta-technology.io/cdm/documentation/source/contributing.html)
- [Documentation Style Guide](https://docs.rosetta-technology.io/cdm/documentation/source/documentation-style-guide.html)
