# Community Specification Governance Policy 1.0

This document provides the governance policy for specifications and other documents developed using the Community Specification process in a repository (each a “Working Group”). Each Working Group must adhere to the requirements in this document.

## 1. Roles

Each Working Group may include the following roles. Additional roles may be adopted and documented by the Working Group.

* 1.1. Maintainer. “Maintainers” are responsible for organizing activities around developing, maintaining, and updating the specification(s) developed by the Working Group. Maintainers are also responsible for determining consensus and coordinating appeals. Each Working Group will designate one or more Maintainers for that Working Group. A Working Group may select a new or additional Maintainer(s) upon Approval of the Working Group Participants.

* 1.2. Editor. “Editors” help to alleviate the workload of maintainers, key contributors are granted Editor status. As Editors, they have the authority to label Pull Requests and issues. Presently, individuals from TradeHeader, Fragmos Chain, and FT Advisory hold Editor status. If you are affiliated with these organizations and are not yet designated as an Editor, please reach out to the [CDM maintainers via email](https://lists.finos.org/g/cdm-maintainers). If you belong to a different organization and wish to become a contributor, you can submit a proposal to the maintainers outlining your request for Editor status. Upon review, further steps will be communicated to facilitate your inclusion as an Editor.
  
* 1.3. Participants. “Participants” are those that have made Contributions to the Working Group subject to the Community Specification License. Participants are automatically abiding by the IP policy of the standard by just participating in a meeting or by actively "enrolling" in the standard.

* 1.4. Discussion Groups. The Working Group may form one or more "Discussion Groups" to organize collaboration around a particular aspect of a specification. Discussion Groups are for discussion only -- Approval of all portions of a specification is subject to the consensus-based decision making process.


## 2. Decision Making

* 2.1. Consensus-Based Decision Making. Working Groups make decisions through a consensus process (“Approval” or “Approved”). While the agreement of all Participants is preferred, it is not required for consensus. Rather, the Maintainer will determine consensus based on their good faith consideration of a number of factors, including the dominant view of the Working Group Participants and nature of support and objections. The Maintainer will document evidence of consensus in accordance with these requirements.

* 2.2. Appeal Process. Decisions may be appealed via a pull request or an issue, and that appeal will be considered by the Maintainer in good faith, who will respond in writing within a reasonable time.


## 3. Ways of Working

Inspired by [ANSI’s Essential Requirements for Due Process](https://share.ansi.org/Shared%20Documents/Standards%20Activities/American%20National%20Standards/Procedures,%20Guides,%20and%20Forms/2020_ANSI_Essential_Requirements.pdf), Community Specification Working Groups must adhere to consensus-based due process requirements. These requirements apply to activities related to the development of consensus for approval, revision, reaffirmation, and withdrawal of Community Specifications. Due process means that any person (organization, company, government agency, individual, etc.) with a direct and material interest has a right to participate by: a) expressing a position and its basis, b) having that position considered, and c) having the right to appeal. Due process allows for equity and fair play. The following constitute the minimum acceptable due process requirements for the development of consensus.

* 3.1. Openness. **Participation shall be open to all persons who are directly and materially affected by the activity in question. There shall be no undue financial barriers to participation. Voting membership on the consensus body shall not be conditional upon membership in any organization, nor unreasonably restricted on the basis of technical qualifications or other such requirements. Membership in a Working Group’s parent organization, if any, may be required.

* 3.2. Lack of Dominance. **The development process shall not be dominated by any single interest category, individual or organization. Dominance means a position or exercise of dominant authority, leadership, or influence by reason of superior leverage, strength, or representation to the exclusion of fair and equitable consideration of other viewpoints.

* 3.3. Balance. **The development process should have a balance of interests. Participants from diverse interest categories shall be sought with the objective of achieving balance.

* 3.4. Coordination and Harmonization. **Good faith efforts shall be made to resolve potential conflicts between and among deliverables developed under this Working Group and existing industry standards.

* 3.5. Consideration of Views and Objections. Prompt consideration shall be given to the written views and objections of all Participants.

* 3.6. Written procedures. This governance document and other materials documenting the Community Specification development process shall be available to any interested person.

## 4. Specification Development Process

* 4.1. Pre-Draft. Any Participant may submit a proposed initial draft document as a candidate Draft Specification of that Working Group. The Maintainer will designate each submission as a “Pre-Draft” document.

* 4.2. Draft. Each Pre-Draft document of a Working Group must first be Approved to become a” Draft Specification”. Once the Working Group approves a document as a Draft Specification, the Draft Specification becomes the basis for all going forward work on that specification.

* 4.3. Working Group Approval. Once a Working Group believes it has achieved the objectives for its specification as described in the Scope, it will Approve that Draft Specification and progress it to “Approved Specification” status, such status to be effective forty-five days following such Approval. The Maintainer will update the NOTICES.MD file to indicate both the Approval of the Draft Specification and the date upon which its “Approved Specification” status will be effective which shall in no case be earlier than forty-five days following the date the Notices.md file was updated.

* 4.4. Publication and Submission. Upon the designation of a Draft Specification as an Approved Specification, the Maintainer will publish the Approved Specification in a manner agreed upon by the Working Group Participants (i.e., Working Group Participant only location, publicly available location, Working Group maintained website, Working Group member website, etc.). The publication of an Approved Specification in a publicly accessible manner must include the terms under which the Approved Specification is being made available under.

* 4.5. Submissions to Standards Bodies. No Draft Specification or Approved Specification may be submitted to another standards development organization without Working group Approval. Upon reaching Approval, the Maintainer will coordinate the submission of the applicable Draft Specification or Approved Specification to another standards development organization. Working Group Participants that developed that Draft Specification or Approved Specification agree to grant the copyright rights necessary to make those submissions.


## 5. Non-Confidential, Restricted Disclosure

Information disclosed in connection with any Working Group activity, including but not limited to meetings, Contributions, and submissions, is not confidential, regardless of any markings or statements to the contrary. Notwithstanding the foregoing, if the Working Group is collaborating via a private repository, the Participants will not make any public disclosures of that information contained in that private repository without the Approval of the Working Group.

## 6. Major release scheduling guidelines

## 6.1 Objectives of defining major releases

* To identify and communicate to users of CDM when changes will happen that could affect them in a profound way, e.g.
  - Changes to existing functionality that may create challenges for upgrading [testing]
  - Changes to technology architecture that may create challenges for upgrading
  - Introduction of major new functionality that may affect how users use CDM going forward
* To help developers of CDM understand the roadmap for the most critical changes to the CDM, so they can better plan their work
* To promote planned and new CDM capabilities to encourage adoption
  
## 6.2 Objectives of defining guidelines for scheduling and approving major releases

* To ensure that major releases are planned, scheduled, and approved in a predictable, consistent, and transparent way
  - Ensure smoother development
  - Reduce conflict
* To ensure that we follow industry best-practices for evolving software.
  
## 6.3 Overall Principles for Scheduling Major Releases

* Major releases shall be planned ahead of time and these plans reviewed and approved by the SWG  so that consumers of CDM are aware of the planned changes and can plan for those changes.
There is a balance between moving too quickly (and creating a lot of confusing changes, potentially discouraging adoption) and moving too slowly (and not addressing major issues in a timely fashion).  * The SWG will be tasked with assessing and maintaining that balance and communicating its decisions.  That balance is likely to change over time as the CDM software matures; likely major release frequency will slow down in the future.
* Part of the role of the guidelines will be to help the CDM SWG to resist pressure to create too many major releases.  However, the guidelines need to provide the SWG with enough flexibility to address major challenges relatively quickly and flexibly when required.
  * Defining the guidelines is important to implement the above objective
    
## 6.4 Detailed Guidelines – Scheduling Major Releases

* No major release will be planned/scheduled  (decision and content) without formal approval at a meeting of the SWG
  * *Rationale:*  Designation of a major release is an important decision that requires transparency and control
 
* The intention is that major releases shall be planned and reviewed at the SWG at least 3 months ahead of the anticipated release date.  
  * *Rationale:*  Giving the community advance warning of major changes will help CDM users plan for how they will use CDM and avoid major surprises.  It will also help CDM developers plan their own changes
    
* It is anticipated that for at least the next several years (say 4-5) at least one major release will be planned each year.
  * *Rationale:* we anticipate that there will be an accumulation of desired changes that cannot be accommodated within a minor release and we wish to ensure that these can be addressed without undue delay
    
*Any addition to the scope/contents of (or technical change to) a planned major release requires SWG approval
  * *Rationale:*   similar to the above guideline on scheduling major releases
    
* If planned scope items for a major release are not available in time for the planned release date, the SWG will need to decide whether to slip the release date or drop the item, based on industry priorities
  
* These guidelines can be overridden in exceptional circumstances by a formal vote of the SWG.
  * *Rationale:*  Sometimes unanticipated issues will come up and we need the ability to move quickly in these cases. However, there should be an explicit decision process when breaking a guideline.
    
* These guidelines can be amended by the SWG following a formal review process

## 6.5 Detailed Guidelines – Long Term Planning and Outreach

* Ideally the SWG will establish plans for upcoming major releases for at least the following 9-12 months
  * *Rationale:*  this provides transparency for the users and potential users of CDM (supporting adoption)
    
* Major release schedules shall be published on the CDM GitHub repository once approved by the SWG (in https://github.com/finos/common-domain-model/blob/master/ROADMAP.md)
  * *Rationale:* as above

## 6.6 Detailed Guidelines – Changes vs.  Major Versions

* Breaking changes (as defined in the change control guidelines) can only be implemented in a major version
  - *Rationale:* this is required to ensure that within a single major version there is stability across minor versions.
    
* Changes (PRs) will be categorized into those that can only be done in major releases (because they contain breaking changes) and others.  PRs requiring a major release shall only be approved for major releases.
  - *Rationale:* this is necessary to ensure that the meaning of major releases is enforced
    
* Even in a new major version, changes that are contrary to the change control guidelines will not be approved unless the SWG executes an exception process.
  - *Rationale:* this is required to ensure that CDM provides stability across major versions, in terms of functionality that is supported

* When a major version includes breaking changes, the SWG will endeavour to ensure that appropriate migration guides and transition plans are in place
  - *Rationale:*  this is to support CDM users in migrating to new versions of CDM



