# Community Specification Contribution Policy 1.0

This document provides the contribution policy for the CDM Standard and is based on the [Community Specification Contribution Policy 1.0](https://github.com/finos/standards-project-blueprint/blob/master/governance-documents/6._Contributing.md). Additional or alternate contribution policies may be adopted and documented by the Working Group.

_NOTE:_ Commits and pull requests to FINOS repositories will only be accepted from those contributors with an active, executed Individual Contributor License Agreement (ICLA) with FINOS, _OR_ who are covered under an existing and active Corporate Contribution License Agreement (CCLA) executed with FINOS. Commits from individuals not covered under an ICLA or CCLA will be flagged and blocked by the [Linux Foundation EasyCLA tool](https://easycla.lfx.linuxfoundation.org/#/). Please note that some CCLAs require individuals/employees to be explicitly named on the CCLA.

_Need an ICLA? Unsure if you are covered under an existing CCLA? Email help@finos.org._


## 1. Contribution Guidelines

This Working Group accepts contributions via pull requests. The following section outlines the process for merging contributions to the specification

* 1.1. Issues. Issues are used as the primary method for tracking anything to do with this specification Working Group.

* 1.1.1. Issue Types. There are three types of issues (each with their own corresponding label):

* 1.1.1.1. Discussion. These are support or functionality inquiries that we want to have a record of for future reference. Depending on the discussion, these can turn into "Spec Change" issues.

* 1.1.1.2. Proposal. Used for items that propose new ideas or functionality that require a larger discussion. This allows for feedback from others before a specification change is actually written. All issues that are proposals should both have a label and an issue title of "Proposal: [the rest of the title]." A proposal can become a "Spec Change" and does not require a milestone.

* 1.1.1.3. Spec Change: These track specific spec changes and ideas until they are complete. They can evolve from "Proposal" and "Discussion" items, or can be submitted individually depending on the size. Each spec change should be placed into a milestone.

## 2. Issue Lifecycle

The issue lifecycle is mainly driven by the Maintainer. All issue types follow the same general lifecycle. Differences are noted below.

## 2.1. Issue Creation

## 2.2. Triage

* The CDM Maintainers will apply the proper labels for the issue. This may include labels for priority, type, and metadata.
* (If needed) Clean up the title to succinctly and clearly state the issue. Also ensure that proposals are prefaced with "Proposal".

## 2.3. Discussion

*  "Spec Change" issues should be connected to the pull request that resolves it.
*  Whoever is working on a "Spec Change" issue should either assign the issue to   themselves or make a comment in the issue saying that they are taking it.
*  "Proposal" and "Discussion" issues should stay open until resolved.

## 2.4. Issue Closure

## 3. How to Contribute a Patch

The Working Group uses pull requests to track changes. To submit a change to the specification:

## 3.1. Fork the Repo

* ([https://github.com/finos/common-domain-model/fork](https://github.com/finos/FDC3/fork))

## 3.2. Create your feature branch

* git checkout -b feature/fooBar

## 3.3. Commit your changes

* git commit -am 'Describe what you changed'

## 3.4. Push to the branch

* git push origin feature/fooBar

## 3.5. Create a Pull Request

* For help creating a pull request from your fork, [see Github's documentation](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request-from-a-fork)

## 4. Pull Request Workflow

The next section contains more information on the workflow followed for Pull Requests.

## 4.1. Pull Request Creation

* We welcome pull requests that are currently in progress. They are a great way to keep track of important work that is in-flight, but useful for others to see. If a pull request is a work in progress, it should be prefaced with "WIP: [title]". You should also add the wip label Once the pull request is ready for review, remove "WIP" from the title and label.
* It is preferred, but not required, to have a pull request tied to a specific issue. Prefix your PR's description with resolves #&lt;issue number> to link it to your issue. There can be circumstances where if it is a quick fix then an issue might be overkill. The details provided in the pull request description would suffice in this case.

## 4.2. Triage

* The Editor in charge of triaging will apply the proper labels for the issue. This should include at least a size label, a milestone, and awaiting review once all labels are applied (e.g. deprecation).

## 4.3. Reviewing/Discussion

* All reviews will be completed using the review tool.
* A "Comment" review should be used when there are questions about the spec that should be answered, but that don't involve spec changes. This type of review does not count as approval.
* A "Changes Requested" review indicates that changes to the spec need to be made before they will be merged.
* Reviewers should update labels as needed (such as needs rebase).
* When a review is approved, the reviewer should add LGTM as a comment.
* Final approval is required by a designated Editor. Merging is blocked without this final approval. Editors will factor reviews from all other reviewers into their approval process.

## 4.4. Responsive

Pull request owners should try to be responsive to comments by answering questions or changing text. Once all comments have been addressed, the pull request is ready to be merged.

## 4.5. Merge or Close

* A pull request should stay open until a Maintainer has marked the pull request as approved.
* Pull requests can be closed by the author without merging.
* Pull requests may be closed by a Maintainer if the decision is made that it is not going to be merged.

## 5. Adoption of Contributions

Contributions merged into the master branch of the CDm repository will form part of the next pre-draft of the CDM Standard (as defined by the CDM Governance document), which must be approved by the Standard Working Group voting participants before it is accepted as a draft and subsequently released as the next version of the Standard.
