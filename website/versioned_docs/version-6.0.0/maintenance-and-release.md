---
title: Maintenance and Release
---

## Reviewing model changes

Contributions are reviewed by the Contribution Review Working Group and once approved, the [CDM Maintainers](/docs/maintainers) will release them following the guidelines in this document.  The [CDM Maintainers](/docs/maintainers) document includes a description of the overall change approval process.

### Review checklist

Before starting to review a contribution, the CDM Maintainer should go
through the following review checklist:

-   Review Pull Request to assert that:
    -   Model changes fulfil the proposed design and use-case
        requirements
    -   Synonyms have been updated and output (JSON) looks correct
    -   Contributed model version is not stale and does not conflict
        with any recent changes
    -   Changes are in accordance with the CDM governance guidelines, including the change control guidelines in the change control guidelines

---
**Note:**
It is not yet possible to verify that mapping, validation and
qualification expectations have been maintained by looking at the output
of the Pull Request and CDM build only. 

---
-   CDM build process completed with no errors or test failures
-   Review additional samples provided (if use-case is not covered by
    existing samples)
-   All model components positioned in the correct namespace
-   All model components have descriptions
-   Additional documentation provided, if necessary.
-   Release note provided

Any review feedback should be sent to the Contributor as required via
Slack, email or in direct meetings.

---
**Note:**

Depending on the size, complexity or impact of a contribution, the CDM
Maintainer can recommend for the contribution to be presented with an
appropriate level of detail to the CDM Architecture and Review
Committee for further feedback. The CDM Maintainer will work with the
Contributor to orchestrate that additional step. The additional feedback
may recommend revisions to the proposed changes. When it is the case the
review process will iterate on the revised proposal.

## Introduction

Before the Pull Request can be merged into the CDM's main branch, some
work is usually required by the Maintainer to preserve the integrity of
the model source code and of its downstream dependencies.

### Post-review technical tasks

A number of technical tasks may need to be performed on the Pull Request
once it is approved:

-   **Stale CDM version**: Contribution is based on an old CDM version
    and model changes conflict with more recent changes. If the
    conflicting change is available in Rosetta, the contributor should
    be asked to update their contribution to the latest version and
    resubmit. If the conflicting change is not yet available in Rosetta,
    this merge will need to be handled by the CDM Maintainer.
-   **Failed unit tests**: Java unit tests in the CDM project may fail
    due to problems in the contributed changes. Alternatively it may be
    that the test expectations need to be updated. The Maintainer should
    determine the cause of the test failure and notify either the
    Contributor or work on adjusting the test expectations.
-   **Additional documentation**: If the contributor provided additional
    documentation, the Maintainer should update the CDM documentation by
    editing the *documentation.rst* file in GitHub. 
-   **Documentation code snippets**: To avoid stale documentation, the
    CDM build process verifies that any code snippets in the
    documentation exists and is in line with the model itself. The
    Maintainer should adjust or include any code snippets by editing the
    *documentation.rst* file on GitHub.
-   **Code generation**: Model changes may cause code generator failures
    (e.g., Java, C#, Scala, Kotlin etc.). In the unlikely event of code
    generation failures, these will need to be addressed by the
    Maintainer.

### Downstream dependencies

The CDM has a number of dependent projects that are required for the
model to be successfully distributed. It is possible that model changes
may cause these downstream projects to fail. The Maintainer will need to
test and, if necessary, update those before the changes can be released.

-   **Translate**: The regression tests in this project compare the
    contributed model against the expected number of mapping, validation
    and qualification results. Due to the contributed model changes, it
    is likely that there will be expectation mismatches that cause this
    build to fail.
-   **CDM Homepage**: compile and test.
-   **CDM Java Examples**: compile and test.

---
**Note:**
In most cases, the post-review technical tasks and downstream
dependencies require software engineering expertise in addition to CDM
expertise. Additional technical support from the CDM Maintainer team may
need to be called upon to address those.

---

The change can be merged into the main CDM code base only upon:

-   approval by CDM Maintainers and/or CDM Architecture and Review
    Committee,
-   successful completion of all the above technical tasks, and
-   successful builds of the CDM and all its downstream dependencies.

## Releasing model changes

Once the contributed model change has been merged, a new release can be
built, tested and deployed. The Maintainer will work with the CDM Owners
and the Contributor on a deployment timeline.

The following release checklist should be verified before deploying a
new model:

-   Update the CDM version number, using the semantic
    versioning format
-   Build release candidate, and test
-   Build documentation website release candidate, and test
-   Deploy release candidate and notify channels if need be
-   (Currently done at a later stage) Update the latest CDM version
    available in Rosetta

---
**Note:**
When the release process is handled through Rosetta Deploy, the
Maintainer should contact the Rosetta support team to request that
deployment and discuss a timeline for the release.

##  Release Build Approval Guidelines

This section covers scheduling of minor, development, and patch releases, and approvals for all builds and releases.

## Development Release Scheduling and Approvals
* Development releases may be scheduled by the maintainers to optimize development resources, based on the queue of approved PRs
  * There is no particular desired/expected release frequency; releases may be cut as soon as there is an approved PR, or several PRs may be consolidated into a single release at the convenience of the maintainers and dev staff
  * *Rationale:*  Development releases are expected to change in functionality, and getting changes out as quickly as practical is usually desirable.
  * Each development release shall require the approval of one maintainer once all the PRs are approved, and the test cases all pass successfully.
* Development releases shall be reported in brief to the CRWG and the SWG

## Major Production Release Build & Release Approvals

* Major production releases will be scheduled by the SWG as described above
* Each major production release shall require the approval of two maintainers after the following are complete:
  * The scope of the major production release is finalized and ratified by the SWG
  * All approved PRs for the major production release are complete
  * The SWG reviews the final list of enhancements in the release and signs off on releasing the development version into production

## Minor Production Release Scheduling and Approvals

* Minor production releases may be scheduled by the maintainers based on the queue of approved PRs
* Minor production releases to introduce enhancements should be combined  to minimize the number of production releases, targeting minor production releases to be issued around four weeks or so as long as there is a queue of approved PRs.  (This frequency can be increased in times of urgent need for new functionality).
  * *Rationale:*   Minimizing the number of production releases will help with supportability, by reducing the number of releases that end users wishing to remain current need to consider, and reducing communications overhead.
* Each minor production release shall require the approval of two maintainers.
* Minor production releases shall be reported in brief to the CRWG and the SWG, 
* A roadmap of anticipated minor production releases shall be reported by the maintainers to the CRWG based on PRs that are in process.

## Production Patch Release Scheduling and Approvals

* Production patch releases to correct defects without releasing new functionality may be scheduled by the maintainers based on the presence of approved defect correction PRs, or other non-functional PRs (e.g. security remediations).
* Production patch releases require the approval of one maintainer
* Production patch releases shall be reported to the CRWG.

 ## Summary of Release Approval Requirements

| Type of Release      | Approval Requirement |  Notes                                        |
| ---------------------| -------------------- |  -------------------------------------------  |
| Major Release (6.0.0)| 2 maintainers        | Scheduling via SWG; Include analysis of the changes from last major release as part of the approval  |
| Minor Release (6.1.0)| 2 maintainers        | Scheduling is up to the maintainers, but aim to keep to around every 4 weeks and no more than fortnightly       |
| Patch Release (6.1.1)| 1 maintainer         | Scheduling is up to the maintainer            |
| Development Release (6.0.0-dev.13)| 1 maintainer         | Scheduling is up to the maintainer            |

![](/img/CDM–Build-Release-Process.png)
