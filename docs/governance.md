---
title: Governance
---

# Governance

The Common Domain Model is an open standard project hosted under FINOS, the [Fintech Open Source Foundation](https://community.finos.org/docs/governance/Standards-Projects), starting in February 2023.

The standard is developed through the [Community Specification](https://community.finos.org/docs/governance/#open-standard-projects) open governance process, and underlying code assets are released under the [Community Specification License 1.0](https://github.com/finos/standards-project-blueprint/blob/master/governance-documents/4._License.md). For versions before 4.0.0 and other license details, check [Notice.md](https://github.com/finos/common-domain-model/blob/master/NOTICE.md).

For more information on discussions and announcements subscribe to our mailing list using the following [link](mailto:cdm+subscribe@lists.finos.org).

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

a. [Maintainer](maintainers.md). “Maintainers” are responsible for organizing activities around developing, maintaining, and updating the specification(s) developed by the Working Group. Maintainers are also responsible for determining and documenting evidence of consensus as well as coordinating appeals. In the event a clear consensus is not reached, Maintaners may call for a simple majority vote of Participants to determine outcomes.
Each Working Group will designate one or more Maintainers for that Working Group. A Working Group may select a new or additional Maintainer(s) upon Approval of the Working Group Participants.
Information about appointment of a CDM Maintainer can be found [here](maintainers.md).

b. Editor. “Editors” help to alleviate the workload of maintainers, key contributors are granted Editor status. As Editors, they have the authority to review and implement pull requests not expressed in code, test and release new functionalities, resolve bugs and implement approaved improvements. Presently, individuals from TradeHeader, Fragmos Chain, and FT Advisory hold Editor status. If you are affiliated with these organizations and are not yet designated as an Editor, please reach out to the CDM maintainers via email. If you belong to a different organization and wish to become a contributor, you can submit a proposal to the maintainers outlining your request for Editor status. Upon review, further steps will be communicated to facilitate your inclusion as an Editor.

c. Participants. “Participants” are those that have made Contributions to the Working Group subject to the [Community Specification License](/LICENSE.md). Participants are automatically abiding by the IP policy of the standard by just participating in a meeting or by actively "enrolling" in the standard.

d. Discussion Groups. The Working Group may form one or more "Discussion Groups" to organize collaboration around a particular aspect of a specification. Discussion Groups are for discussion only -- Approval of all portions of a specification is subject to the consensus-based decision making process.

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

The Steering Working Group has the role of defining major releases of CDM and shaping their content.  The [major release scheduling guidelines](major-release-scheduling-guidelines.md) page  discusses the objectives for defining major releases and guidelines that the Steering Working Group (SWG) must follow in scheduling major releases.


## 6. Pull Request Classification and Approval Guidelines

Details on how pull requests will be classified can be found in our [Change control guidelines](change-control-guidelines.md/#Pull-Request-Classification-and-Approval-Guidelines) page.

## 7. Release Build Approval Guidelines

This section covers scheduling of minor, development, and patch releases, and approvals for all builds and releases. Please click [here](maintenance-and-release.md/#Development-Release-Scheduling-And-Approvals) to be redirected.

