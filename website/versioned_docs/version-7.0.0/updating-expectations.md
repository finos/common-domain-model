---
title: Updating Expectations
---

## Updating expectations

When a contribution changes the CDM model, the recorded results that the CDM's regression tests check against usually need to be regenerated to match. This process is referred to as *updating expectations*, and it is a required step before most Pull Requests can be contributed to the CDM.

### What are expectations?

The CDM follows a test-driven development approach: a set of sample data, organised into *Test Packs*, is run through the model's translation, validation, qualification and function logic, and the results produced are captured as *expected results* (or *expectations*). These expectations are stored as JSON files alongside the samples, under `rosetta-source/src/main/resources`, and are compared against the model's actual output every time the CDM is built.

- Test Pack samples include FpML documents and other industry data used to exercise the model's mapping (translation) logic.
- Function input and output samples exercise the CDM's standardised functions.
- Every regression test compares the current, actual output for a given sample against its stored expectation, and fails the build if the two differ.

### Why expectations need to be kept up to date

Editing a data type, a mapping, a validation rule, a qualification rule or a function changes the output the model produces for the existing samples. Once that happens, the expectations recorded against the previous version of the model no longer match, and the regression tests fail, even when the model change is correct and intended.

Updating expectations regenerates those recorded results from the current state of the model and its samples, so the Test Pack reflects the intended behaviour rather than the previous one. This is why the [development guidelines](dev-guidelines.md) require that all translate regression test expectations for mapping, validation and qualification are maintained or improved before a change is contributed.

---
**Note:**
Updating expectations rewrites files rather than checking them. A Contributor should always review the resulting diff (`git status` and `git diff`) to confirm the changes are the ones expected, and note in the contribution whether the mapping, validation or qualification success numbers have changed, since these numbers should not decrease.

---

### Updating expectations with Maven

From the root of the repository, run:

```
mvn clean install -U -DskipTests -P format,update-expectations
```

This command combines two Maven profiles:

- `format`, defined in the `rosetta-source` module, runs at the `validate` phase and reformats the `.rosetta` model source files.
- `update-expectations`, defined in the `tests` module, runs at the `process-test-classes` phase and executes `CdmTestPackCreator`, which re-ingests the Test Pack samples and rewrites the corresponding expectation and function sample files under `rosetta-source/src/main/resources`.

The remaining options serve the following purpose:

- `clean install` rebuilds every module in the reactor, so the regenerated expectations reflect the model as it currently stands, including any local changes.
- `-U` forces Maven to check for updated snapshot dependencies, keeping the build aligned with the latest development version of the CDM.
- `-DskipTests` skips running the regression test suite itself, since the purpose of this command is to regenerate expectations rather than assert against them.

### Updating expectations from the Maven panel

The same result can be achieved from an IDE's Maven tool window instead of the command line, for example in IntelliJ IDEA:

1.  Open the Maven tool window (**View > Tool Windows > Maven**).
2.  Under **Profiles**, tick `format` and `update-expectations`.
3.  Under **Common Domain Model > Lifecycle**, select `clean` and `install`, then run them, for example with **Run Maven Build**.

![Maven tool window in IntelliJ IDEA, showing the format and update-expectations profiles ticked and the clean and install lifecycle phases selected](/img/updating-expectations-maven-profiles.png)

This is equivalent to running `mvn clean install -P format,update-expectations` from the command line. The `-U` and `-DskipTests` options can be added from the same tool window, in the Maven **Runner** settings or as additional command line arguments for the run.

### Updating expectations with Rosetta

Setting up a local Java and Maven development environment is not the only way to work with the CDM's expectations. [Rosetta](https://rosetta-technology.io), the modelling platform for the Rune DSL, is available free of charge at its community tier and provides testing and validation tooling for the CDM without requiring a local build.

---
**Note:**
As with any CDM application component, FINOS encourages the adoption of CDM by software providers but does not endorse any CDM application component. Please refer to the [Rosetta Documentation](https://docs.rosetta-technology.io/rosetta/rosetta-products/) for details of its current testing and validation capabilities.

---

### Next steps

- Review the [development guidelines](dev-guidelines.md) for the full contribution checklist.
- Review the [editing the model](editing.md) page, which places updating expectations within the wider modelling checklist.
