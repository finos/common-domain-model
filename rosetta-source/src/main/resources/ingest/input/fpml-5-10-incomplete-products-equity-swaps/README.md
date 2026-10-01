# Incomplete samples

The samples in this folder are kept **outside** the main test-pack directories because their ingestion from FpML into CDM is **not yet complete**. A sample is considered incomplete when any of the following holds:

- One or more product attributes expressed in the FpML source cannot currently be represented in the CDM model.
- The corresponding CDM elements exist, but the ingestion / translation logic that would populate them from the FpML source has not yet been written (or covers only a subset of the product's features).

As a consequence, the generated CDM output under `ingest/output/fpml-confirmation-to-trade-state/<this-folder-name>/` may be missing fields, may carry an approximate product qualification, or may qualify to a more general product type than the sample strictly represents.

## Why these samples are kept in the repository

- They catalogue the **known gaps** between FpML and CDM for this asset class and act as a backlog for future mapping or model work.
- They give contributors a concrete starting point: the FpML input and the current CDM output can be diffed directly against the target behaviour when a gap is closed.
- Over time, samples may be **promoted** out of the `incomplete-*` folder into the standard test-pack once the underlying model and mapping work has landed.

## What these samples are **not**

- They are **not** regression / failure fixtures. The current output reflects today's partial mapping, not a frozen expected behaviour.
- They are **not** reference examples of correctly modelled products. Please do not cite the generated CDM output as authoritative.

## Reporting issues

- A **mislabelled or structurally incorrect** sample **outside** any `incomplete-*` folder should be raised as a bug — those are in scope for immediate correction.
- A gap on a sample **inside** an `incomplete-*` folder is expected by design. It can still be useful to open an issue or discussion so the gap can be scoped and prioritised, but it is not treated as a regression.
