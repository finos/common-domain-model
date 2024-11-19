---
title: Governance
---

# Governance

The Common Domain Model is an open standard project hosted under FINOS, the [Fintech Open Source Foundation](https://community.finos.org/docs/governance/Standards-Projects), starting in February 2023.

The standard is developed through the [Community Specification](https://community.finos.org/docs/governance/#open-standard-projects) open governance process, and underlying code assets are released under the [Community Specification License 1.0](https://github.com/finos/standards-project-blueprint/blob/master/governance-documents/4._License.md). For versions before 4.0.0 and other license details, check [Notice.md](https://github.com/finos/common-domain-model/blob/master/NOTICE.md).

For a more detailed overview of the existing Working Group and standard Participants, Editors and Maintainers, please see the [Governance](governance.md) page. For more information on discussions and announcements subscribe to our mailing list using the following [link](mailto:cdm+subscribe@lists.finos.org).

A proposal can be defined at a conceptual level or a logical level (i.e.
in code). In each case, the proposal must be developed in line with the
CDM [design principles](design-principles.md) and
[agile development approach](development-approach.md) and submitted
to FINOS staff and the Architecture & Review Committee for approval. In
some instances, the proposal may not be immediately approved but may be
assigned to an existing or new Working Group for the purpose of
reviewing, revising or extending the proposal.

Once approved, the amendment will be scheduled to be merged with the
CDM's main code branch by the CDM Maintainers.

This document provides the governance policy for specifications and other documents developed using the Community Specification process in a repository (each a “Working Group”). Each [Working Group](working-groups.md) must adhere to the requirements.

Click [here](cdm-org-structure.md) to view the diagram showing the Working Groups' organisational structure. 

### Roles

1.1. [Maintainer](maintainers.md). “Maintainers” are responsible for organizing activities around developing, maintaining, and updating the specification(s) developed by the Working Group. Maintainers are also responsible for determining and documenting evidence of consensus as well as coordinating appeals. In the event a clear consensus is not reached, Maintaners may call for a simple majority vote of Participants to determine outcomes.
Each Working Group will designate one or more Maintainers for that Working Group. A Working Group may select a new or additional Maintainer(s) upon Approval of the Working Group Participants.
Information about appointment of a CDM Maintainer can be found [here](appointment-of-maintainers.md)

1.2. Editor. “Editors” help to alleviate the workload of maintainers, key contributors are granted Editor status. As Editors, they have the authority to review and implement pull requests not expressed in code, test and release new functionalities, resolve bugs and implement approaved improvements. Presently, individuals from TradeHeader, Fragmos Chain, and FT Advisory hold Editor status. If you are affiliated with these organizations and are not yet designated as an Editor, please reach out to the CDM maintainers via email. If you belong to a different organization and wish to become a contributor, you can submit a proposal to the maintainers outlining your request for Editor status. Upon review, further steps will be communicated to facilitate your inclusion as an Editor.

1.3. Participants. “Participants” are those that have made Contributions to the Working Group subject to the [Community Specification License](common-domain-model/LICENSE.md). Participants are automatically abiding by the IP policy of the standard by just participating in a meeting or by actively "enrolling" in the standard.

1.4. Discussion Groups. The Working Group may form one or more "Discussion Groups" to organize collaboration around a particular aspect of a specification. Discussion Groups are for discussion only -- Approval of all portions of a specification is subject to the consensus-based decision making process.

Changes to the CDM may be proposed by CDM Working Groups, individual corporate contributors, or individual contributors.



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


## 7.0 Release Build Approval Guidelines

This section covers scheduling of minor, development, and patch releases, and approvals for all builds and releases.

## 7.1 Development Release Scheduling and Approvals
* Development releases may be scheduled by the maintainers to optimize development resources, based on the queue of approved PRs
  * There is no particular desired/expected release frequency; releases may be cut as soon as there is an approved PR, or several PRs may be consolidated into a single release at the convenience of the maintainers and dev staff
  * *Rationale:*  Development releases are expected to change in functionality, and getting changes out as quickly as practical is usually desirable.
  * Each development release shall require the approval of one maintainer once all the PRs are approved, and the test cases all pass successfully.
* Development releases shall be reported in brief to the CRWG and the SWG

## 7.2 Major Production Release Build & Release Approvals

* Major production releases will be scheduled by the SWG as described above
  * *(TODO:  insert a diagram of the promotion process)*
* Each major production release shall require the approval of two maintainers after the following are complete:
  * The scope of the major production release is finalized and ratified by the SWG
  * All approved PRs for the major production release are complete
  * The SWG reviews the final list of enhancements in the release and signs off on releasing the development version into production

## 7.3 Minor Production Release Scheduling and Approvals

* Minor production releases may be scheduled by the maintainers based on the queue of approved PRs
* Minor production releases to introduce enhancements should be combined  to minimize the number of production releases, targeting minor production releases to be issued around four weeks or so as long as there is a queue of approved PRs.  (This frequency can be increased in times of urgent need for new functionality).
  * *Rationale:*   Minimizing the number of production releases will help with supportability, by reducing the number of releases that end users wishing to remain current need to consider, and reducing communications overhead.
* Each minor production release shall require the approval of two maintainers.
* Minor production releases shall be reported in brief to the CRWG and the SWG, 
* A roadmap of anticipated minor production releases shall be reported by the maintainers to the CRWG based on PRs that are in process.

## 7.4  Production Patch Release Scheduling and Approvals

* Production patch releases to correct defects without releasing new functionality may be scheduled by the maintainers based on the presence of approved defect correction PRs, or other non-functional PRs (e.g. security remediations).
* Production patch releases require the approval of one maintainer
* Production patch releases shall be reported to the CRWG.

 ## 7.5 Summary of Release Approval Requirements

| Type of Release      | Approval Requirement |  Notes                                        |
| ---------------------| -------------------- |  -------------------------------------------  |
| Major Release (6.0.0)| 2 maintainers        | Scheduling via SWG; Include analysis of the changes from last major release as part of the approval  |
| Minor Release (6.1.0)| 2 maintainers        | Scheduling is up to the maintainers, but aim to keep to around every 4 weeks and no more than fortnightly       |
| Patch Release (6.1.1)| 1 maintainer         | Scheduling is up to the maintainer            |
| Development Release (6.0.0-dev.13)| 1 maintainer         | Scheduling is up to the maintainer            |
