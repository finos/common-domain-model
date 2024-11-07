---
title: Governance
---

# Governance

The Common Domain Model is an open standard project hosted under FINOS, the [Fintech Open Source Foundation](https://community.finos.org/docs/governance/Standards-Projects), starting in February 2023.

The standard is developed through the [Community Specification](https://community.finos.org/docs/governance/#open-standard-projects) open governance process, and underlying code assets are released under the [Community Specification License 1.0](https://github.com/finos/standards-project-blueprint/blob/master/governance-documents/4._License.md). For versions before 4.0.0 and other license details, check [Notice.md](https://github.com/finos/common-domain-model/blob/master/NOTICE.md).

For a more detailed overview of the existing Working Group and standard Participants, Editors and Maintainers, please see [Governance.md](https://github.com/finos/standards-project-blueprint/blob/master/governance-documents/5._Governance.md). For more information on discussions and announcements subscribe to our mailing list using the following [link](mailto:cdm+subscribe@lists.finos.org).

A proposal can be defined at a conceptual level or a logical level (i.e.
in code). In each case, the proposal must be developed in line with the
CDM [design-principles](/docs/cdm-overview#design-principles) and
[agile-development-approach](/docs/cdm-overview#agile-development-approach) and submitted
to FINOS staff and the Architecture & Review Committee for approval. In
some instances, the proposal may not be immediately approved but may be
assigned to an existing or new Working Group for the purpose of
reviewing, revising or extending the proposal.

Once approved, the amendment will be scheduled to be merged with the
CDM's main code branch by the CDM Maintainers.

This document provides the governance policy for specifications and other documents developed using the Community Specification process in a repository (each a “Working Group”). Each [Working Group](working-groups.md) must adhere to the requirements.


### Roles
The CSL specifies [three different contribution roles](https://github.com/finos/standards-project-blueprint/blob/master/governance-documents/5._Governance.md#1roles) for each specific Working Group:

* Maintainers - those who drive consensus within the working group
* Editors - those who codify ideas into a formal specification
* Participants - anyone who provides contributions to the project under a signed CSL CLA. A great way to sign the CLA is to open a Pull Request to add your name to the [Participants.md](https://github.com/finos/standards-project-blueprint/blob/master/governance-documents/participants.md) file. 



## 1. Decision Making

* 1.1. Consensus-Based Decision Making. [Working Groups](working-groups.md) make decisions through a consensus process (“Approval” or “Approved”). While the agreement of all Participants is preferred, it is not required for consensus. An individual's [role](working-groups.md) will determine the extent of their decision making abilities. For example, the Maintainer will determine consensus based on their good faith consideration of a number of factors, including the dominant view of the Working Group Participants and nature of support and objections. 

* 1.2. Appeal Process. Decisions may be appealed via a pull request or an issue, and that appeal will be considered by the Maintainer in good faith, who will respond in writing within a reasonable time.


## 2. Ways of Working

Inspired by [ANSI’s Essential Requirements for Due Process](https://share.ansi.org/Shared%20Documents/Standards%20Activities/American%20National%20Standards/Procedures,%20Guides,%20and%20Forms/2020_ANSI_Essential_Requirements.pdf), Community Specification Working Groups must adhere to consensus-based due process requirements. These requirements apply to activities related to the development of consensus for approval, revision, reaffirmation, and withdrawal of Community Specifications. Due process means that any person (organization, company, government agency, individual, etc.) with a direct and material interest has a right to participate by: a) expressing a position and its basis, b) having that position considered, and c) having the right to appeal. Due process allows for equity and fair play. The following constitute the minimum acceptable due process requirements for the development of consensus.

* 2.1. Openness. **Participation shall be open to all persons who are directly and materially affected by the activity in question. There shall be no undue financial barriers to participation. Voting membership on the consensus body shall not be conditional upon membership in any organization, nor unreasonably restricted on the basis of technical qualifications or other such requirements. Membership in a Working Group’s parent organization, if any, may be required.

* 2.2. Lack of Dominance. **The development process shall not be dominated by any single interest category, individual or organization. Dominance means a position or exercise of dominant authority, leadership, or influence by reason of superior leverage, strength, or representation to the exclusion of fair and equitable consideration of other viewpoints.

* 2.3. Balance. **The development process should have a balance of interests. Participants from diverse interest categories shall be sought with the objective of achieving balance.

* 2.4. Coordination and Harmonization. **Good faith efforts shall be made to resolve potential conflicts between and among deliverables developed under this Working Group and existing industry standards.

* 2.5. Consideration of Views and Objections. Prompt consideration shall be given to the written views and objections of all Participants.

* 2.6. Written procedures. This governance document and other materials documenting the Community Specification development process shall be available to any interested person.

## 3. Specification Development Process

* 3.1. Pre-Draft. Any Participant may submit a proposed initial draft document as a candidate Draft Specification of that Working Group. The Maintainer will designate each submission as a “Pre-Draft” document.

* 3.2. Draft. Each Pre-Draft document of a Working Group must first be Approved to become a” Draft Specification”. Once the Working Group approves a document as a Draft Specification, the Draft Specification becomes the basis for all going forward work on that specification.

* 3.3. Working Group Approval. Once a Working Group believes it has achieved the objectives for its specification as described in the Scope, it will Approve that Draft Specification and progress it to “Approved Specification” status, such status to be effective forty-five days following such Approval. The Maintainer will update the NOTICES.MD file to indicate both the Approval of the Draft Specification and the date upon which its “Approved Specification” status will be effective which shall in no case be earlier than forty-five days following the date the Notices.md file was updated.

* 3.4. Publication and Submission. Upon the designation of a Draft Specification as an Approved Specification, the Maintainer will publish the Approved Specification in a manner agreed upon by the Working Group Participants (i.e., Working Group Participant only location, publicly available location, Working Group maintained website, Working Group member website, etc.). The publication of an Approved Specification in a publicly accessible manner must include the terms under which the Approved Specification is being made available under.

* 3.5. Submissions to Standards Bodies. No Draft Specification or Approved Specification may be submitted to another standards development organization without Working group Approval. Upon reaching Approval, the Maintainer will coordinate the submission of the applicable Draft Specification or Approved Specification to another standards development organization. Working Group Participants that developed that Draft Specification or Approved Specification agree to grant the copyright rights necessary to make those submissions.


## 4. Non-Confidential, Restricted Disclosure

Information disclosed in connection with any Working Group activity, including but not limited to meetings, Contributions, and submissions, is not confidential, regardless of any markings or statements to the contrary. Notwithstanding the foregoing, if the Working Group is collaborating via a private repository, the Participants will not make any public disclosures of that information contained in that private repository without the Approval of the Working Group.

## 5. Major release scheduling guidelines

The Steering Working Group has the role of defining major releases of CDM and shaping their content.  This section discusses the objectives for defining major releases and guidelines that the Steering Working Group (SWG) must follow in scheduling major releases.

### 5.1 Objectives of defining major releases

* To identify and communicate to users of CDM when changes will happen that could affect them in a profound way, e.g.
  - Changes to existing functionality that may create challenges for upgrading [testing]
  - Changes to technology architecture that may create challenges for upgrading
  - Introduction of major new functionality that may affect how users use CDM going forward
* To help developers of CDM understand the roadmap for the most critical changes to the CDM, so they can better plan their work
* To promote planned and new CDM capabilities to encourage adoption
  
### 5.2 Objectives of defining guidelines for scheduling and approving major releases

* To ensure that major releases are planned, scheduled, and approved in a predictable, consistent, and transparent way
  - Ensure smoother development
  - Reduce conflict
* To ensure that we follow industry best-practices for evolving software.
  
### 5.3 Overall Principles for Scheduling Major Releases

* Major releases shall be planned ahead of time and these plans reviewed and approved by the SWG  so that consumers of CDM are aware of the planned changes and can plan for those changes.
There is a balance between moving too quickly (and creating many changes, potentially discouraging adoption) and moving too slowly (and not addressing major issues in a timely fashion).  The SWG will be tasked with assessing and maintaining that balance and communicating its decisions.  That balance is likely to change over time as the CDM software matures; likely major release frequency will slow down in the future.
* Part of the role of the guidelines will be to help the CDM SWG to resist pressure to create too many major releases.  However, the guidelines need to provide the SWG with enough flexibility to address major challenges relatively quickly and flexibly when required.
  * Defining the guidelines is important to implement the above objective
    
### 5.4 Detailed Guidelines – Scheduling Major Releases

* No major release will be planned/scheduled  (decision and content) without formal approval at a meeting of the SWG
  * *Rationale:*  Designation of a major release is an important decision that requires transparency and control
 
* The intention is that major releases shall be planned and reviewed at the SWG at least 3 months ahead of the anticipated release date.  
  * *Rationale:*  Giving the community advance warning of major changes will help CDM users plan for how they will use CDM and avoid major surprises.  It will also help CDM developers plan their own changes
    
* It is anticipated that for at least the next several years (say 4-5) at least one major release will be planned each year.
  * *Rationale:* we anticipate that there will be an accumulation of desired changes that cannot be accommodated within a minor release and we wish to ensure that these can be addressed without undue delay
    
* Any addition to the scope/contents of (or technical change to) a planned major release requires SWG approval
  * *Rationale:*   similar to the above guideline on scheduling major releases
    
* If planned scope items for a major release are not available in time for the planned release date, the SWG will need to decide whether to slip the release date or drop the item, based on industry priorities
  
* These guidelines can be overridden in exceptional circumstances by a formal vote of the SWG.
  * *Rationale:*  Sometimes unanticipated issues will come up and we need the ability to move quickly in these cases. However, there should be an explicit decision process when breaking a guideline.
    
* These guidelines can be amended by the SWG following a formal review process

### 5.5 Detailed Guidelines – Long Term Planning and Outreach

* Ideally the SWG will establish plans for upcoming major releases for at least the following 9-12 months
  * *Rationale:*  this provides transparency for the users and potential users of CDM (supporting adoption)
    
* Major release schedules shall be published on the CDM GitHub repository once approved by the SWG (in https://github.com/finos/common-domain-model/blob/master/ROADMAP.md)
  * *Rationale:* as above

### 5.6 Detailed Guidelines – Changes vs.  Major Versions

* Breaking changes (as defined in the change control guidelines) can only be implemented in a major version
  - *Rationale:* this is required to ensure that within a single major version there is stability across minor versions.
    
* Changes (PRs) will be categorized into those that can only be done in major releases (because they contain breaking changes) and others.  PRs requiring a major release shall only be approved for major releases.
  - *Rationale:* this is necessary to ensure that the meaning of major releases is enforced
    
* Even in a new major version, changes that are contrary to the change control guidelines will not be approved unless the SWG executes an exception process.
  - *Rationale:* this is required to ensure that CDM provides stability across major versions, in terms of functionality that is supported

* When a major version includes breaking changes, the SWG will endeavour to ensure that appropriate migration guides and transition plans are in place
  - *Rationale:*  this is to support CDM users in migrating to new versions of CDM

## 6.0 Change Control Guidelines

This section discusses how changes to the CDM are controlled within and between releases, in particular:

* Principles
  * What we are trying to achieve with the change control guidelines; 
  * What constraints/objectives we have for putting these guidelines in place
* Rules
  * The specific rules we want to define and enforce to meet the principles
* Evaluation methods
  * How we want to ensure that the rules are evaluated and enforced during development
  * This includes development processes (e.g. review and approval) as well as automated tooling (e.g. regression test cases)

## 6.1 Change Control Principles

* We are trying to ensure rapid, smooth, and predictable evolution of the model by controlling when and how breaking changes are introduced
  * We want to allow changes where needed, with defined process to make those changes, to meet evolved and improved understanding of the business and technical requirements.
  * We want to give ourselves some freedom to make changes more easily when there are newly introduced components/structures that may not be fully mature, but we don’t want to spend a lot of effort on planning for that. We will do this using the pull request approval guidelines for bug fixes, giving some scope for correcting recently introduced changes.
* Prohibiting breaking changes within a major version should allow users to upgrade to minor versions more quickly and easily, and plan for when to implement larger changes
  * By  limiting and control the amount of change to key business models and technology structures,  CDM users can have confidence that functionality they develop using CDM will continue to work with new versions of CDM with minimal effort, at least for a defined period of time

## 6.2 Change Control Rules

* Unless explicitly indicated otherwise, components of CDM (such as data types and functions) will be under change control once released into production.
* Within multiple minor releases of a single major release, the following will not be changed:
  * Within business objects, any object that is valid in version M.N should be representable and valid in version M.N+1 .
    * For example, existing data fields may not be changed in type, reduced in cardinality, or removed, and new mandatory data fields may not be added.
    * Change to the name of any model element (e.g. types, attributes, enums, functions or reporting rules) is prohibited
    * Change to the DSL that results in any existing expression becoming invalid is prohibited
    * All validations that pass in version M.N should also pass in version M.N+1
  * Function signatures may not be changed in such a way as to invalidate previous callers (e.g addition of new mandatory parameters, or removal/change of existing parameters.)
    * Change to the DSL that results in change to any of the generated code's public interfaces is prohibited
  * Test cases that passed in a prior version shall continue to work.
  * We allow some minor exceptions to these rules for newly introduced functionality that may not be fully formed, as part of the PR process for defect corrections
  * Functionality shall not be removed between major versions without advance notice


Please note that full, bidirectional interoperability between minor versions is not required.  If an application uses functionality in version M.N, it does not need to fully interoperate with version M.N-1, assuming that the older version does not include that functionality.  However, if an application uses functionality found in version M.N, it should be able to interoperate with version M.N+1.

## 6.3 Change Control Evaluation and Enforcement

* Designers and contributors to CDM are responsible for being aware of and following the change control guidelines.  This includes flagging pull requests when they involve breaking changes to controlled objects.
  * Backward incompatible changes shall be documented and include a migration guide (remap from old structures and functions to the new)
* Reviewers will be responsible for assessing (“double checking”) whether any changes may violate the change control guidelines, and flag questionable changes for further review.  
  * Part of the role of the Contribution Review Working Group (CRWG) and of the maintainers is to enforce these guidelines for any change.
* There will be a set of regression test cases developed for each supported major version.  Subsequent CDM minor and major versions will be tested against these test cases and a report prepared indicating which cases succeed and fail, and this will be compared against the guidelines.  For example:
  * CDM version 6.2 will be tested against the 6.1 test cases; all should succeed, unless included in the exception/noncontrolled list.
  * CDM version 6.0 will be tested against the latest 5.x test cases; the list of failures should be compared against the approved scope of change for 6.0.  (NB: performing this test might involve making some technical changes to the 5.0 test cases to work with the 6.0 technical architecture if that has changed, but the functionality should not otherwise be changed.)



## 7.0 Pull Request Classification and Approval Guidelines

This section discusses how pull requests will be classified, reviewed, and approved.

## 7.1 PR Classification

Pull requests shall be classified into one of the following complexity categories:
* Model change - bug fix – change to existing logic (without major redesign) to cause it to implement the original intended behaviour and design; generally used to address an oversight in a previous contribution.   
  * *Backward compatibility:*  For defect corrections to production versions, the defect correction shall generally be backward-compatible with the existing design unless the existing design is newly introduced and so severely compromised that it cannot function unless something is changed.
* Model change - Enhancement – new functionality or change to existing functionality required to meet a new business requirement.  
  * *Backward compatibility:*  If the change includes backward-incompatible changes, this shall be flagged as such and the change shall be targeted for a development version.
* Technical change.  This is used to cover a variety of cases that don't affect the model itself, including updates to dependencies, mapping changes, test cases, etc.

Pull requests shall be tagged in GitHub as described in discussion [#2789](https://github.com/finos/common-domain-model/discussions/2789) to implement the approval process.



## 7.2 Summary of PR approval requirements

* PRs shall be classified into Model defect corrections (bug fixes to correct existing functionality) vs. Model enhancements (new designs or capabilities) vs. technical.
* There shall be an indication of whether a PR includes any backward-incompatible changes.
* Approval has to be by a separate person from the submitter (This is enforced by GitHub; maintainers shall not attempt to circumvent this control.)


| Type of PR          | Backward Compatible |  Backward Incompatible                        |
| --------------------| --------------------|  ---------------------------------------------|
| Model - Bug fix     |1 maintainer – separate from the submitter, preferably from a separate organization | 2 maintainers; must have been reviewed by the CRWG; if for a production version, SWG must approve; only used for recently introduced functionality  |
| Model - Enhancement | 2 maintainers; must have been approved by a WG or the CRWG | 2 maintainers; must be on roadmap or approved by SWG; must have been approved by a WG or the CRWG; must go into a dev version; at least one maintainer must be from a separate organization   |
| Technical - e.g. dependency update, change to mapping, reference data, documentation, changes to samples…. | At least one;  additional review up to the maintainer’s discretion – e.g. might need to consult the Technology Architecture Working Group (TAWG) | Must be approved by the TAWG; must go into a dev version |





## 8.0 Release Build Approval Guidelines

This section covers scheduling of minor, development, and patch releases, and approvals for all builds and releases.

## 8.1 Development Release Scheduling and Approvals
* Development releases may be scheduled by the maintainers to optimize development resources, based on the queue of approved PRs
  * There is no particular desired/expected release frequency; releases may be cut as soon as there is an approved PR, or several PRs may be consolidated into a single release at the convenience of the maintainers and dev staff
  * *Rationale:*  Development releases are expected to change in functionality, and getting changes out as quickly as practical is usually desirable.
  * Each development release shall require the approval of one maintainer once all the PRs are approved, and the test cases all pass successfully.
* Development releases shall be reported in brief to the CRWG and the SWG

## 8.2 Major Production Release Build & Release Approvals

* Major production releases will be scheduled by the SWG as described above
  * *(TODO:  insert a diagram of the promotion process)*
* Each major production release shall require the approval of two maintainers after the following are complete:
  * The scope of the major production release is finalized and ratified by the SWG
  * All approved PRs for the major production release are complete
  * The SWG reviews the final list of enhancements in the release and signs off on releasing the development version into production

## 8.3 Minor Production Release Scheduling and Approvals

* Minor production releases may be scheduled by the maintainers based on the queue of approved PRs
* Minor production releases to introduce enhancements should be combined  to minimize the number of production releases, targeting minor production releases to be issued around four weeks or so as long as there is a queue of approved PRs.  (This frequency can be increased in times of urgent need for new functionality).
  * *Rationale:*   Minimizing the number of production releases will help with supportability, by reducing the number of releases that end users wishing to remain current need to consider, and reducing communications overhead.
* Each minor production release shall require the approval of two maintainers.
* Minor production releases shall be reported in brief to the CRWG and the SWG, 
* A roadmap of anticipated minor production releases shall be reported by the maintainers to the CRWG based on PRs that are in process.

## 8.4  Production Patch Release Scheduling and Approvals

* Production patch releases to correct defects without releasing new functionality may be scheduled by the maintainers based on the presence of approved defect correction PRs, or other non-functional PRs (e.g. security remediations).
* Production patch releases require the approval of one maintainer
* Production patch releases shall be reported to the CRWG.

 ## 8.5 Summary of Release Approval Requirements

| Type of Release      | Approval Requirement |  Notes                                        |
| ---------------------| -------------------- |  -------------------------------------------  |
| Major Release (6.0.0)| 2 maintainers        | Scheduling via SWG; Include analysis of the changes from last major release as part of the approval  |
| Minor Release (6.1.0)| 2 maintainers        | Scheduling is up to the maintainers, but aim to keep to around every 4 weeks and no more than fortnightly       |
| Patch Release (6.1.1)| 1 maintainer         | Scheduling is up to the maintainer            |
| Development Release (6.0.0-dev.13)| 1 maintainer         | Scheduling is up to the maintainer            |
