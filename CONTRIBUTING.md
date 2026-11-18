# Contributing to the Common Domain Model

This page has two parts. The [new contributor quickstart](#new-contributor-quickstart) takes you from your first look at the project to your first merged pull request. The Community Specification Contribution Policy after it governs every contribution, including your first.

## New contributor quickstart

Most first contributions fix the documentation or the website. These need one maintainer's approval and no working group review, so they are the quickest way to learn how the project works. A change to the model itself is first approved by a working group. The steps below show how to tell the two apart, and where to ask when you are not sure.

### 1. Check that a contributor license agreement covers you

The [Linux Foundation EasyCLA](https://community.finos.org/docs/governance/Software-Projects/easycla) bot checks every commit in a pull request to this repository. If anyone who wrote a commit has not signed a contributor license agreement (CLA), the pull request is blocked until they do.

- **If you own what you write**, for example because you are not contributing for an employer, sign an Individual CLA. Open your pull request, follow the link the bot posts on it, and choose *Individual*.
- **If your employer owns your work**, your employer needs a Corporate CLA with FINOS, and its CLA manager must add you to the list of approved contributors. You then confirm your affiliation through the link the bot posts on your first pull request.
- **Coverage on another FINOS project does not carry over.** The CDM is a standards project with [its own EasyCLA group](https://community.finos.org/docs/governance/Software-Projects/easycla#standards-projects), so you need approval for the CDM specifically.
- **Co-authors are checked too.** A `Co-authored-by` line, such as one added by an AI coding assistant, names an author who has no CLA, and blocks the pull request.

If you are not sure whether you or your firm are covered, email [help@finos.org](mailto:help@finos.org). Your firm does not need to be a [FINOS member](https://community.finos.org/docs/governance/#membership) for you to contribute.

**Enrol as a participant.** The CDM is an [open standard project](https://community.finos.org/docs/governance/#open-standard-projects) under the Community Specification License. Its rules apply to everyone who takes part, including people who only attend working group meetings. To enrol, open a pull request that adds your name, organisation and date of enrollment to [Participants.md](https://github.com/finos/common-domain-model/blob/master/Participants.md), with the text of the [enrollment template](https://github.com/finos/common-domain-model/blob/master/.github/PULL_REQUEST_TEMPLATE/default.md) as its description. This makes a good first pull request: it is one line, and it runs the EasyCLA check for you.

**Material from elsewhere.** If your change copies code, data or text from another source, check its license against the [FINOS license categories](https://community.finos.org/docs/governance/Software-Projects/license-categories) before you contribute it, and say in the pull request where it came from.

### 2. Follow the project

- **Watch this repository** to be notified of new issues, pull requests and discussions. GitHub explains [how to watch a repository](https://docs.github.com/en/account-and-profile/managing-subscriptions-and-notifications-on-github/setting-up-notifications/configuring-notifications#configuring-your-watch-settings-for-an-individual-repository); choose *Custom* to be notified of only some of them.
- **Join the mailing list** by emailing [cdm+subscribe@lists.finos.org](mailto:cdm+subscribe@lists.finos.org).
- **Attend a working group.** Each group's page on the [list of working groups](https://cdm.finos.org/docs/working-groups) has a registration link, and every meeting is on the [FINOS community calendar](https://www.finos.org/calendar).
- **Learn the model.** [Introduction to the CDM](https://training.linuxfoundation.org/training/introduction-to-the-common-domain-model-cdm-lfel1016/) is a free 90-minute course.

### 3. Choose a first contribution

None of these changes the model, so none needs working group approval.

| Contribution | Where it lives | How to start |
| --- | --- | --- |
| Fix a typo, broken link or unclear sentence in the documentation | `docs/`, and `website/versioned_docs/` if a published release has the same mistake ([which folder?](#which-documentation-folder-to-edit)) | Open a pull request |
| Fix a broken or unclear reference between pages | `docs/`, and `website/versioned_docs/` if a published release has the same mistake ([which folder?](#which-documentation-folder-to-edit)) | Open a pull request |
| Fix the website: navigation, home page or layout | `website/` | Open a pull request; open an issue first if it changes how the site is organised |
| Add steps to reproduce an open bug | The bug's issue | Comment on the issue |
| Draft an outline for an approved documentation issue | The issue | Comment on the issue with your outline |
| Write an example walkthrough, start a glossary, or improve this guide | `docs/`, `CONTRIBUTING.md` | Open a pull request; open an issue first if you are adding a page |
| Enrol as a participant | `Participants.md` | Open a pull request |

To find something to work on, look at the open issues labelled [documentation](https://github.com/finos/common-domain-model/issues?q=is%3Aissue+is%3Aopen+label%3Adocumentation).

### 4. Discussion, issue or pull request?

- **You have a question, or you are not sure whether something is a bug:** start a [Q&A discussion](https://github.com/finos/common-domain-model/discussions/categories/q-a).
- **You can describe a specific problem or change:** open an [issue](https://github.com/finos/common-domain-model/issues/new/choose). Choose *Bug* for something that behaves wrongly, *Feature* for a change to what the model can represent, or *Task* for one piece of an existing feature.
- **You are fixing the documentation or the website without changing what the model means:** open a pull request directly. You do not need an issue.
- **You are changing the model:** open an issue first, and wait for a working group to approve it before you open a pull request.

### 5. Know what your change touches

The folder a file lives in tells you what kind of change you are making, and how it is reviewed.

| Folder | What it holds | Review |
| --- | --- | --- |
| `docs/` | The documentation for the development version on `master`, shown as **Next** in the site's version menu | One maintainer |
| `website/versioned_docs/` | The documentation for each published release, one folder per version | One maintainer |
| The rest of `website/` | The site's configuration, home page, navigation and styles | One maintainer |
| `CONTRIBUTING.md`, `README.md`, `Participants.md` | Project information. The **Contribute** link on [cdm.finos.org](https://cdm.finos.org) opens this file | One maintainer |
| `rosetta-source/src/main/rosetta/` | The model itself, written in the [Rune DSL](https://rune.finos.org/docs/modelling-components/) | See the [approval rules for model changes](https://cdm.finos.org/docs/change-control-guidelines#summary-of-pr-approval-requirements) |
| `rosetta-source/src/main/resources/`, `tests/`, `examples/` | Code lists, mappings from other formats such as FpML, tests and their expected results, and Java examples | One maintainer, who may consult the Technology Architecture Working Group |

**The build checks code blocks in `docs/` against the model.** A code block in `docs/` must match the model's text exactly, or the build fails. Tag any code block that is not model code as `json`, `xml`, `Java`, `MD` or `sourcecode`.

#### Which documentation folder to edit

The site opens on the latest published release, at `cdm.finos.org/docs/...`, and its pages come from that release's folder, currently `website/versioned_docs/version-7.0.0/`. The development version on `master` is listed as **Next** in the site's version menu, at `cdm.finos.org/docs/next/...`, and its pages come from `docs/`.

- **A change that applies only to the development version**, such as documenting a model change that has not been released yet, goes in `docs/` only.
- **A fix for a mistake that a published release shows too** goes in `docs/` and in that release's folder, so that the published page is corrected and the fix carries into the next release.

For example, to fix a typo on the Event Model page:

| Where the typo appears | Files to edit |
| --- | --- |
| Only on the **Next** page, `cdm.finos.org/docs/next/event-model` | `docs/event-model.md` |
| On the 7.0.0 page, `cdm.finos.org/docs/event-model`, and on the **Next** page | `website/versioned_docs/version-7.0.0/event-model.md` and `docs/event-model.md` |

### 6. Rosetta Design or GitHub?

You can change the model in either. Documentation and website changes are made through GitHub.

- **[Rosetta Design](https://rosetta-technology.io/design)** runs in the browser, so you need nothing installed. It checks your syntax as you type and compiles the model, and when you contribute your workspace it opens the pull request for you. Its community tier is free. It suits model changes, especially if you do not write Java. See the [Rosetta documentation](https://docs.rosetta-technology.io/rosetta/rosetta-products/0-welcome-to-rosetta) to get started.
- **GitHub** suits everything else, and model changes if you prefer to work locally. Fork this repository, make your change on a branch, and open a pull request from your fork. To edit the model locally, the [Rune plugin for VS Code](https://github.com/finos/rune-dsl/tree/main/rune-ide/vscode) checks your syntax, and the [Java setup guide](https://cdm.finos.org/docs/download#setup) covers the build.

To preview a website or documentation change, run the site locally (this needs Node.js 18 or later) and open `http://localhost:3000`:

```sh
cd website
npm ci
npm start
```

Rosetta is one of several modelling platforms that support the CDM; FINOS does not endorse any of them.

### 7. Ask before you take on a larger change

- **To pick up an existing issue,** comment on it to say you are taking it, so that nobody else starts the same work.
- **For a change to the model,** bring the issue to the [working group](https://cdm.finos.org/docs/working-groups) for its area. If you do not know which group that is, ask in a [Q&A discussion](https://github.com/finos/common-domain-model/discussions/categories/q-a), or bring it to the [Contribution Review Working Group](https://cdm.finos.org/docs/CDM-Contribution-Review-WG). When the group approves the issue, it assigns a maintainer to review your pull request.
- **For a large change, or a change to core types,** follow [before you start modelling](https://cdm.finos.org/docs/dev-guidelines#before-you-start-modelling): agree a use case and a high-level design with the working group before you write the change.

### 8. After you open a pull request

1. The EasyCLA bot checks every commit's authors.
2. The build compiles the model and runs the tests.
3. Release Management triages the pull request: it adds labels and links the pull request to its issue.
4. Maintainers review it. A documentation or technical change needs one maintainer's approval. For a model change, Release Management prepares a Release PR from your change, and maintainers review and merge that one under the [approval rules for model changes](https://cdm.finos.org/docs/change-control-guidelines#summary-of-pr-approval-requirements).
5. Answer review comments, and push any fixes to the same branch.

Once merged, a change to `website/` appears on [cdm.finos.org](https://cdm.finos.org) the next time the site is deployed. A change to `docs/` appears under **Next** until the next release publishes it.

---

# Community Specification Contribution Policy 1.0

This document provides the contribution policy for the CDM Standard and is based on the [Community Specification Contribution Policy 1.0](https://github.com/finos/standards-project-blueprint/blob/main/CONTRIBUTING.md). Additional or alternate contribution policies may be adopted and documented by the Working Group.

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
